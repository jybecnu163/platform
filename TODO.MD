# 统一运维平台开发 TODO
## 1. 基础设施与工具链
部署 Nacos 集群，创建 dev、test、pre、prod 命名空间并记录 ID    √
部署 MySQL 实例，执行 init.sql 创建 CMDB 表 √
搭建 Jenkins，配置 GitLab/GitHub Webhook，编写流水线脚本（编译、打包、上传 Nexus、回调中心运维服务）
搭建制品仓库（Nexus / MinIO），建立版本目录规范

## 2. Agent（Go）
完善 main.go：合并最新版本（先启动 Java 再注册 Nacos、进程监控与自愈）  √
实现 /execute/upgrade 完整逻辑：下载 jar、优雅关闭旧进程、启动新进程、健康检查、更新 Nacos 元数据   √
实现 /execute/rollback 逻辑：支持回滚到上一版本（预留制品下载接口） √
增加 Agent 自身优雅退出（SIGTERM 时主动摘除 Nacos 并退出）  √
添加指令端口安全校验（可选 Token）
编写 Dockerfile（将 Agent 与业务 jar 打包，使用 entrypoint 启动 Agent） √
本地联调：启动 order-service 容器，验证注册、心跳、升级、回滚   √

## 3. 中心运维服务（SpringBoot + MyBatis）
修正所有 Mapper 的 @Insert、@Update 为完整 SQL  √
实现 DeployStrategyMapper 查询方法（按 appId + envId 查找）√
重构 DeployService 发布引擎：
支持分批灰度（解析 batch_config）
调用 Agent 升级接口 √
轮询等待实例健康
实现回滚逻辑（创建回滚任务，调用 Agent 回滚接口）   √
实现 GET /api/applications/{id}/versions 接口（返回 deploy_tasks 中已完成版本）
实现实例同步对账服务（对比 Nacos 与 CMDB，清理幽灵实例）
集成云 API（弹性伸缩部分，若设备未到位可暂缓）
全局异常处理与日志记录，方便排查 500 错误

## 4. Portal（React）
完善环境详情页 EnvironmentList（从后端获取数据展示）
完善发布表单 DeployForm：√
版本下拉列表（自动联想 + 手动输入）
根据环境自动加载部署策略
提交调用 POST /api/deployments  √
实现发布历史页面 DeployHistory，支持按应用/环境筛选，展示状态机流转
实现一键回滚按钮（仅在允许回滚的任务行显示）
集成权限与审批流（至少前端控制，生产操作二次确认）
优化 UI/UX（加载状态、错误提示、空数据占位）

## 5. 网关服务
自动发现

## 6. 文件服务
文件按服务划分进行版本管理

## 7. 业务服务


## 8. 联调与测试
端到端流程测试：Git push → Jenkins 构建 → 后端记录版本 → Portal 发布 → Agent 升级 → Nacos 状态一致

异常场景测试：
Agent 升级过程中下载失败
新版本启动失败，验证自动回滚
Java 进程异常退出，验证重启与最终容器退出
中心运维服务与 Agent 网络中断
性能测试：模拟上百个服务实例同时注册、分批发布
安全测试：Agent 端口未授权访问、越权操作

## 9. 文档与交付
编写平台使用手册（Portal 操作指南、发布流程、回滚步骤）
编写运维手册（如何部署 Nacos、MySQL、各微服务组件）
整理配置模板（application.yml、环境变量列表、Docker 命令示例）
准备演示环境，录制核心功能操作视频