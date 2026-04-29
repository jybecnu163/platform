package com.platform.ops.service;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.platform.ops.entity.ApplicationEntity;
import com.platform.ops.entity.DeployStrategyEntity;
import com.platform.ops.entity.DeployTaskEntity;
import com.platform.ops.entity.EnvironmentEntity;
import com.platform.ops.mapper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

@Service
public class DeployService {
    private static final Logger log = LoggerFactory.getLogger(DeployService.class);
    @Value("${nacos.server-addr}")
    private String nacosServerAddr;
    //    @NacosInjected
//    private NamingService namingService;
    @Autowired
    private DeployTaskMapper deployTaskMapper;
    @Autowired
    private DeployStrategyMapper deployStrategyMapper;
    @Autowired
    private EnvironmentMapper environmentMapper;
    @Autowired
    private ApplicationMapper applicationMapper;
    @Autowired
    private InstanceMapper instanceMapper;
    @Autowired
    private RestTemplate restTemplate;

    private ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void checkConfig() {
        System.out.println("=== Nacos server-addr = " + nacosServerAddr);
    }

    /**
     * 根据命名空间创建 NamingService
     */
    private NamingService createNamingService(String namespace) throws NacosException {
        Properties props = new Properties();
        props.setProperty("serverAddr", nacosServerAddr);
        props.setProperty("namespace", namespace);
        // 如果有认证，可以设置 username/password 或 accessToken
        return NacosFactory.createNamingService(props);
    }

    /**
     * 创建发布任务并异步执行
     */
    public DeployTaskEntity createDeployTask(DeployTaskEntity task) {
        task.setStatus("PENDING");
        deployTaskMapper.insert(task);
        log.info("createDeployTask insert over");
        executeDeploy(task);
        log.info("executeDeploy sync over");
        return task;
    }

    /**
     * 发布引擎核心
     */
    public void executeDeploy(DeployTaskEntity task) {
        new Thread(() -> {
            try {
                task.setStatus("RUNNING");
                deployTaskMapper.update(task);

                // 获取策略
                DeployStrategyEntity strategy = deployStrategyMapper.findByAppIdAndEnvId(task.getAppId(), task.getEnvId());
                if (strategy == null || strategy.getBatchConfig() == null) {
                    throw new RuntimeException("No deploy strategy found");
                }
                List<String> batchList = objectMapper.readValue(strategy.getBatchConfig(), new TypeReference<List<String>>() {
                });
                EnvironmentEntity env = environmentMapper.findById(task.getEnvId());
                ApplicationEntity app = applicationMapper.findById(task.getAppId());
                String serviceName = app.getCode() + "-" + env.getEnvType().toLowerCase();
                String namespace = env.getNacosNamespace();

                // 获取所有在线实例（来自 Nacos） 051c1cce-2566-4887-a9a4-d9455b64d6d8
                NamingService naming = createNamingService(namespace);
                List<Instance> allInstances = naming.getAllInstances(serviceName);
                log.info("naming.getAllInstances:" + serviceName + " allInstances: " + allInstances);
                allInstances = allInstances.stream().filter(Instance::isHealthy).collect(Collectors.toList());
                if (allInstances.isEmpty()) {
                    throw new RuntimeException("No healthy instances for " + serviceName);
                }

                int totalSize = allInstances.size();
                int processed = 0;
                for (String batchStr : batchList) {
                    int batchSize = calculateBatchSize(batchStr, totalSize, processed);
                    if (batchSize == 0) continue;
                    List<Instance> batchInstances = allInstances.subList(processed, Math.min(processed + batchSize, totalSize));
                    processed += batchSize;

                    task.setStatus("BATCH_IN_PROGRESS");
                    deployTaskMapper.update(task);

                    for (Instance inst : batchInstances) {
                        upgradeInstance(inst, task.getVersion(), task.getPackageUrl());
                    }

                    // 自动继续或暂停（这里简化为直接继续）
                    if (processed >= totalSize) break;
                }

                task.setStatus("COMPLETED");
                deployTaskMapper.update(task);

            } catch (Exception e) {
                task.setStatus("FAILED");
                deployTaskMapper.update(task);
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * 升级单个实例：摘流 -> 调 Agent 升级 -> 等待健康 -> 恢复流量
     */

    private void upgradeInstance(Instance instance, String version, String packageUrl) throws Exception {
        String ip = instance.getIp();

        // 1. 直接调 Agent 升级（Agent 内部会完成摘流 → 更新 → 等待就绪 → 恢复流量）
        String agentUrl = "http://" + ip + ":9090/execute/upgrade";
        Map<String, String> req = new HashMap<>();
        req.put("version", version);
        req.put("packageUrl", packageUrl);
        ResponseEntity<String> response = restTemplate.postForEntity(agentUrl, req, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Agent returned error: " + response.getBody());
        }

        // 2. 轮询等待 Agent 的 /health 接口返回 UP（可选，作为双重确认）
        boolean healthy = false;
        for (int i = 0; i < 30; i++) {
            Thread.sleep(2000);
            String healthUrl = "http://" + ip + ":9090/health";
            try {
                ResponseEntity<String> healthResp = restTemplate.getForEntity(healthUrl, String.class);
                if (healthResp.getStatusCode().is2xxSuccessful()
                        && healthResp.getBody().contains("upgrade accepted")) {
                    healthy = true;
                    break;
                }
            } catch (Exception ignored) {
            }
        }
        if (!healthy) {
            throw new RuntimeException("Instance " + ip + " did not become healthy after upgrade");
        }
    }

    private int calculateBatchSize(String batchStr, int total, int processed) {
        if (batchStr.endsWith("%")) {
            int percent = Integer.parseInt(batchStr.replace("%", ""));
            return (int) Math.ceil(total * percent / 100.0);
        } else {
            return Integer.parseInt(batchStr);
        }
    }

    public List<DeployTaskEntity> listTasks(Long appId, Long envId) {
        return deployTaskMapper.findAll();
    }

    public void rollbackDeploy(Long taskId) {
        // 回滚逻辑待补全
    }
}