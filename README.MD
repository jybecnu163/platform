# 统一运维管理平台

## 项目结构
```
platform/
├── portal/          # React 前端 (Vite)
├── center-ops/      # SpringBoot 中心运维服务
├── agent/           # Go Agent (带Nacos注册)
├── gateway-service/ # 网关
├── jar-file-server/ # 文件服务器
├── sql/             # MySQL 初始化脚本
├── docker-compose.yml  # 本地基础设施
└── README.md
```

## 快速启动
1. 安装依赖：Java 17+、Node 18+、Go 1.21+、Docker
2. 启动基础设施：
   ```bash
   docker-compose up -d

   docker exec -i mysql57 mysql -uroot -ppass 数据库名 < ./sql/Dump20260428.sql
   ```

3. 启动Portal：
   ```bash
   cd portal
   npm install
   npm run dev
   ```

4. 启动中心运维服务：
   ```bash
   cd center-ops
   mvn spring-boot:build

   docker run -d --name center-ops --network hadoop --ip 172.19.0.14 -p 8080:8080 -v D:\IdeaProject\deepseek-dev\platform\center-ops\target\center-ops-1.0.0.jar:/app/app.jar -w /app eclipse-temurin:11-jre-alpine java -jar app.jar

   ```

5. 启动业务服务：
   ```bash
   cd order-service
   mvn spring-boot:build

   docker run -d --network hadoop --ip 172.19.0.7  -e NACOS_NAMESPACE=051c1cce-2566-4887-a9a4-d9455b64d6d8 -e SERVICE_NAME=order-service-dev -e AGENT_PORT=9090 -e NACOS_SERVER=172.19.0.13 -e APP_VERSION=1.0.0 -e NACOS_USERNAME=MyServerIdentityKey -e NACOS_PASSWORD=MyUniqueServerIdentityValue order-service:latest

docker run -d --network hadoop --ip 172.19.0.9  -e NACOS_NAMESPACE=051c1cce-2566-4887-a9a4-d9455b64d6d8 -e SERVICE_NAME=order-service-dev -e AGENT_PORT=9090 -e NACOS_SERVER=172.19.0.13 -e APP_VERSION=1.0.0 -e NACOS_USERNAME=MyServerIdentityKey -e NACOS_PASSWORD=MyUniqueServerIdentityValue order-service:latest


docker cp D:\IdeaProject\deepseek-dev\platform\agent\agent jovial_nobel:/app/
docker cp D:\IdeaProject\deepseek-dev\platform\agent\agent crazy_ardinghelli:/app/

   ```

6. (可选)启动网关服务：
   ```bash
   cd gateway-service
   mvn spring-boot:build

   docker run -d --name gateway --network hadoop --ip 172.19.0.11 -p 8088:8088 -v D:\IdeaProject\deepseek-dev\platform\gateway-service\out\gateway-service-0.0.1-SNAPSHOT.jar:/app/app.jar -w /app eclipse-temurin:11-jre-alpine java -jar app.jar

   ```

7. (可选)文件管理服务：
   ```bash
   cd jar-file-service
   mvn spring-boot:build

   docker run -d --name jar-file-server --network hadoop --ip 172.19.0.10 -p 8081:8081 -v D:\IdeaProject\deepseek-dev\platform\jar-file-server\out\jar-file-server.jar:/app/app.jar -v D:\IdeaProject\deepseek-dev\platform\jar-file-server\uploads:/app/uploads -w /app eclipse-temurin:11-jre-alpine java -jar app.jar

   ```

8. (可选) 编译并运行Agent：
   ```bash
   cd agent
   go build -o agent main.go
   export NACOS_NAMESPACE=prod
   export SERVICE_NAME=order-service-prod
   export AGENT_IP=127.0.0.1
   export AGENT_PORT=9090
   ./agent
   ```

## 说明
- Portal 访问 http://localhost:3000
- 中心运维服务 API 基础路径 http://localhost:8080
- Nacos 控制台 http://localhost:8848/nacos (默认账号nacos/nacos)
