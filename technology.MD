统一服务治理与运维平台技术说明书
1. 概述
本平台旨在对数百个异构服务进行集中管理，提供服务信息查看、多环境资源管控、发布回滚流水线、弹性伸缩等核心能力。平台由 Portal（前端）、CMDB（配置管理数据库）、中心运维服务（核心引擎）、Nacos（注册中心）与 Agent（服务探针） 协同构建。

1.1 核心目标
一套平台管理所有环境：一套 Portal + 一套后端，管理所有系统及 DEV / TEST / PRE / PROD 环境。

环境隔离：通过 Nacos 命名空间实现逻辑隔离，资源与配置按环境严格区分。

容器化服务治理：支持容器化服务的分批灰度发布、一键回滚、自动健康检查。

自动化伸缩：集成云监控指标，通过 CMDB 策略动态调整服务实例数量。

变更可追溯：所有运维操作记录至 CMDB，操作日志可审计。

1.2 术语定义
系统 (Application)：一个业务服务，如 order-service。

环境 (Environment)：dev / test / pre / prod。

实例 (Instance)：承载服务的容器或 Pod，内部运行业务进程和 Agent。

Nacos 命名空间：对应一个环境，实现服务注册的隔离。

2. 系统架构
2.1 组件交互视图
text
┌──────────────┐
│   运维/开发   │
└──────┬───────┘
       │ 操作 Portal
       ▼
┌──────────────┐  HTTP API   ┌────────────────┐
│   Portal     │ ◄─────────► │ 中心运维服务   │
│ (React/Vite) │            │ (SpringBoot +  │
└──────────────┘            │  MyBatis +     │
                            │  Nacos Client) │
                            └───┬───┬───┬────┘
                                │   │   │
               ┌────────────────┘   │   └──────────────┐
               │                    │                   │
               ▼                    ▼                   ▼
        ┌──────────┐       ┌──────────────┐      ┌────────────┐
        │  CMDB    │       │  Nacos 集群   │      │ 云厂商 API │
        │(MySQL)   │       │ (多命名空间)  │      │ (ESS/CVM)  │
        └──────────┘       └──────┬───────┘      └────────────┘
                                  │
                     注册 / 心跳   │  查询 / 摘流 / 权重
                                  │
                      ┌───────────▼───────────┐
                      │    Agent (Go)        │
                      │    + 业务 Java 进程  │
                      └──────────────────────┘
Portal：Vue/React 前端，通过中心运维服务 REST API 交互。

CMDB：存储静态配置、期望状态、发布记录、伸缩策略等。

中心运维服务：所有运维操作的编排引擎，负责调用 Nacos、Agent、云 API。

Nacos：提供服务注册与发现，通过命名空间隔离环境。

Agent：内嵌于业务容器，向 Nacos 注册并维持心跳，接收来自运维服务的指令（升级、回滚），并管理业务 Java 进程。

2.2 网络与隔离设计
Nacos 共享集群：所有环境共用同一 Nacos 集群，通过 Namespace 实现服务列表隔离。

管理平台网络：部署在内网核心区域，需能访问各环境的 Agent 指令端口（通过专线/VPN 或 Docker 网络）。

云资源隔离：不同环境使用独立 VPC 或安全组。

3. CMDB 数据模型（核心表）
3.1 应用表 applications
字段	类型	说明
id	BIGINT	主键
name	VARCHAR(255)	系统名称
code	VARCHAR(100)	唯一标识
owner	VARCHAR(100)	负责人
repo_url	VARCHAR(500)	代码仓库
biz_line	VARCHAR(100)	业务线
created_at	TIMESTAMP	创建时间
3.2 环境表 environments
字段	类型	说明
id	BIGINT	主键
app_id	BIGINT	关联应用
env_type	ENUM	DEV/TEST/PRE/PROD
nacos_namespace	VARCHAR(200)	Nacos 命名空间 ID
scaling_group_id	VARCHAR(200)	云弹性伸缩组 ID（可空）
isolation	VARCHAR(50)	SHARED / EXCLUSIVE
3.3 实例表 instances
字段	类型	说明
id	BIGINT	主键
env_id	BIGINT	所属环境
ip	VARCHAR(50)	实例 IP
hostname	VARCHAR(100)	主机名
status	ENUM	ONLINE/OFFLINE/FAULT
agent_version	VARCHAR(50)	Agent 版本
last_heartbeat	TIMESTAMP	最后心跳时间
app_version	VARCHAR(50)	当前业务版本
metadata	JSON	扩展字段（规格等）
3.4 发布策略表 deploy_strategies
字段	类型	说明
id	BIGINT	主键
app_id	BIGINT	关联应用
env_id	BIGINT	关联环境
strategy_type	VARCHAR(50)	BATCH / ROLLING
batch_config	JSON	分批配置，如 ["10%","30%","100%"]
health_check_url	VARCHAR(500)	业务探活地址
auto_promote	TINYINT(1)	批次间是否自动继续
rollback_enabled	TINYINT(1)	是否允许一键回滚
3.5 发布任务表 deploy_tasks
字段	类型	说明
id	BIGINT	主键
app_id	BIGINT	关联应用
env_id	BIGINT	关联环境
version	VARCHAR(100)	目标版本
package_url	VARCHAR(500)	制品下载地址
status	ENUM	PENDING / RUNNING / BATCH_IN_PROGRESS / PAUSED / COMPLETED / FAILED / ROLLING_BACK / ROLLBACK_COMPLETED
rollback_to_version	VARCHAR(100)	回滚目标版本
creator	VARCHAR(100)	操作人
created_at	TIMESTAMP	创建时间
updated_at	TIMESTAMP	更新时间
3.6 伸缩策略表 scaling_policies
字段	类型	说明
id	BIGINT	主键
env_id	BIGINT	关联环境
metric	VARCHAR	cpu / memory
threshold_up	INT	扩容阈值（%）
threshold_down	INT	缩容阈值（%）
scale_out_count	INT	扩容数量
scale_in_count	INT	缩容数量
duration	INT	触发持续分钟
cooldown	INT	冷却秒
min_instances	INT	最小实例数
max_instances	INT	最大实例数
3.7 伸缩历史表 scaling_history
记录每一次扩缩容的详细信息（时间、动作、变化数量、触发方式、结果等）。

4. 核心流程
4.1 分批灰度发布流程
创建发布单：用户在 Portal 选择系统、环境、目标版本和灰度策略，调用 POST /api/deployments。

状态流转：PENDING →（审批通过）→ RUNNING → BATCH_IN_PROGRESS。

执行批次：中心运维服务读取策略和实例列表，对每批次执行：
a. 摘流：调用 Nacos API 设置实例权重为 0（或注销实例），等待排水。
b. 下发升级指令：调用 Agent 的 POST /execute/upgrade，传递版本号和制品 URL。
c. Agent 升级：Agent 下载新 jar → 优雅停止旧 Java 进程 → 启动新进程 → 健康自检。
d. 健康验证：Agent 内部轮询业务 /health 直至返回 200，然后更新 Nacos 元数据。
e. 引流：中心运维服务检测到实例健康后，通过 Nacos 恢复权重（或重新注册）。

批次确认：每批完成后暂停（可由策略 auto_promote 决定自动继续），等待人工确认或监控指标正常后继续。

完成或失败：全批次成功则 COMPLETED；单个实例失败则标记 FAILED 并可触发回滚。

4.2 一键回滚流程
用户对某个发布任务点击“一键回滚”。

中心运维服务创建回滚任务（状态 ROLLING_BACK），找到上一成功版本（prevVersion）。

对已升级的实例批量执行：

Agent 摘流 → 停止当前进程 → （如果保留了本地备份则直接恢复，否则重新下载旧版本 jar）→ 启动旧版本进程 → 健康检查 → 恢复流量。

更新 CMDB 实例版本信息和任务状态为 ROLLBACK_COMPLETED。

4.3 自动弹性伸缩流程
策略配置：用户在 Portal 中为环境设定伸缩策略（如 CPU > 70% 持续3分钟扩容2台），保存到 CMDB，并通过运维服务下发至云伸缩组。

触发扩容：云监控触发伸缩组，启动新实例（容器），容器内 Agent 自动启动并注册到 Nacos。

生命周期钩子：云伸缩组通过 webhook 通知中心运维服务新实例启动。

运维服务介入：等待实例在 Nacos 中健康后，更新 CMDB 期望实例数，记录伸缩历史，并通知 Portal。

缩容：先通过 Nacos 摘除实例，等待排水后通知云 API 销毁实例，并更新 CMDB。

5. 各模块设计细节
5.1 Portal（React）
技术栈：React 18 + Vite + Ant Design + React Router。

主要页面：应用列表、环境详情、实例列表、发布操作、发布历史、伸缩策略配置。

关键交互：

环境切换：左侧根据环境选择器展示不同数据。

发布：选择历史版本（从deploy_tasks中COMPLETED的版本列表获取）或输入新版本，支持灰度策略配置。

回滚：在发布历史页面提供“一键回滚”按钮。

权限控制：基于角色（开发者、运维、管理员），生产环境操作需二次确认。

5.2 CMDB（MySQL）
存储所有元数据和期望状态，作为“事实来源”。

通过中心运维服务定期与 Nacos、云平台对账，清理幽灵实例并告警。

5.3 中心运维服务（SpringBoot + MyBatis）
核心服务，提供 REST API。

主要模块：

发布引擎：状态机驱动，按策略分批执行（摘流→调用Agent→等待健康→引流）。

回滚执行器：复用发布引擎，以旧版本为目标执行升级流程。

弹性伸缩控制器：接收云钩子，同步实例状态；与云 API 交互更新伸缩组策略。

对账服务：定时对比 Nacos 实际实例与 CMDB 期望实例，发现差异告警。

指令调度：向 Agent 发送 HTTP 指令。

集成 Nacos Client（Java），通过 NamingService 操作实例权重、查询实例列表。

5.4 Agent（Go）
运行于业务容器内，是容器主进程。

启动顺序：启动 Java 业务进程 → 等待业务就绪 → 向 Nacos 注册。

核心功能：

服务注册与心跳：以临时实例（Ephemeral）注册，Nacos 心跳由 SDK 自动维护。

指令接收：暴露 HTTP 端口 /execute/upgrade、/execute/rollback、/health。

进程管理：监控 Java 子进程，非升级场景下异常退出则自动重启（带次数限制），超过上限后 Agent 退出使容器终止，便于调度器重新调度。

升级流程：下载新 jar → 停旧进程 → 替换启动新进程 → 健康检查 → 更新 Nacos metadata。

资源回收：通过 cmd.Wait() 等待子进程，避免僵尸进程。

环境变量配置：SERVICE_NAME、NACOS_NAMESPACE、AGENT_IP、AGENT_PORT、NACOS_SERVER、APP_VERSION 等。

5.5 Nacos
集群部署，提供命名空间隔离环境。

通过 Open API 或 SDK 供运维服务和 Agent 调用，进行实例注册、权重修改、健康状态查询等。

5.6 制品仓库与 CI/CD 集成
Jenkins：监听代码合并，编译打包，上传制品到 Nexus，并调用中心运维服务 POST /api/deployments 将版本和制品 URL 写入 deploy_tasks。

制品地址：存储在 deploy_tasks.package_url，可供后续发布选择。

6. 技术选型
模块	技术栈
Portal	React 18, Vite, Ant Design, React Router
中心运维服务	Spring Boot 3.x, MyBatis, MySQL, Nacos Client
Agent	Go 1.21, Nacos Go SDK v2
注册中心	Nacos 2.x
数据库	MySQL 8.0
容器化	Docker, Docker Compose
CI/CD	Jenkins, Nexus (可替换)
7. 部署与启动
7.1 基础设施准备
启动 MySQL（导入 init.sql）。

启动 Nacos（使用 Docker 或独立部署），创建各环境命名空间，获取命名空间 ID。

7.2 构建各模块
Center-Ops：mvn package，配置 application.yml 中数据库和 Nacos 地址。

Portal：npm run build，部署静态文件或通过 Vite 开发服务器运行。

Agent：编译 GOOS=linux go build -o agent main.go。

Order-Service：包含 Dockerfile，将 agent 和 order-service.jar 打入镜像。

7.3 启动示例（本地开发环境）
bash
# 1. 启动 Nacos 和 MySQL（docker-compose）
docker-compose up -d

# 2. 启动 Center-Ops
cd center-ops && mvn spring-boot:run

# 3. 启动 Portal
cd portal && npm run dev

# 4. 构建并启动 order-service 容器（含 Agent）
cd order-service
docker build -t order-service:latest .
docker run -d --name order-service --network hadoop \
  -p 8080:8080 -p 9090:9090 \
  -e NACOS_NAMESPACE=xxx \
  -e SERVICE_NAME=order-service-dev \
  -e AGENT_IP=host.docker.internal \
  -e AGENT_PORT=9090 \
  -e NACOS_SERVER=172.19.0.13:8848 \
  -e APP_VERSION=1.0.0 \
  order-service:latest
8. 安全与高可用
权限：RBAC 模型，生产操作需二次确认和审批。

API 安全：JWT 鉴权；Agent 端口仅内网可达，可加 Token 校验。

高可用：中心运维服务多副本，MySQL 主从，Nacos 集群。

自身管理：平台自身也注册至 CMDB，通过自身进行发布管理（引导节点除外）。

9. 总结
本技术说明覆盖了统一运维平台的全部关键设计，从顶层架构到具体模块实现，明确了发布、回滚、伸缩等核心自动化的完整流程。各组件职责清晰，通过 Nacos + Agent + CMDB 的三角协作，实现了对数百个业务服务的精细化、自动化治理。