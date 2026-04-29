package com.platform.ops.service;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.platform.ops.entity.EnvironmentEntity;
import com.platform.ops.mapper.EnvironmentMapper;
import com.platform.ops.mapper.InstanceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Properties;

@Service
public class InstanceService {
    @Value("${nacos.server-addr}")
    private String nacosServer;

    @Autowired
    private InstanceMapper instanceMapper;

    @Autowired
    private EnvironmentMapper environmentMapper;

    private NamingService namingService;

    @PostConstruct
    public void init() throws NacosException {
        Properties props = new Properties();
        props.setProperty("serverAddr", nacosServer);
        // 如有认证
        // props.setProperty("accessToken", token);
        namingService = NacosFactory.createNamingService(props);
    }

    /**
     * 从Nacos获取指定环境的实际在线实例，并与CMDB对比
     */
    public List<Instance> getNacosInstances(Long envId) throws NacosException {
        EnvironmentEntity env = environmentMapper.findById(envId);
        if (env == null) throw new RuntimeException("Environment not found");
        String serviceName = getServiceName(env);
        return namingService.getAllInstances(serviceName, env.getNacosNamespace());
    }

    private String getServiceName(EnvironmentEntity env) {
        // 应用code-环境类型，例如 order-service-prod
        // 此处简化，实际需要关联应用表
        return "order-service-dev"; // 临时写死，需改造
    }

    /**
     * 同步CMDB实例状态（对账）
     */
    public void syncInstancesFromNacos(Long envId) throws NacosException {
        List<Instance> nacosInstances = getNacosInstances(envId);
        // 对比并更新CMDB，省略实现...
    }
}