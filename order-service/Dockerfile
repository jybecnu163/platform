# 第一阶段：构建应用
FROM maven:3.8.4-openjdk-11-slim AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# 第二阶段：运行镜像
FROM eclipse-temurin:11-jre-alpine
WORKDIR /app

# 复制构建好的 jar
COPY --from=build /app/target/*.jar app-1.0.0.jar

# 复制预先编译好的 agent（需提前在 agent 目录编译）
# 注意：构建镜像前请先执行 cd ../agent && go build -o agent main.go
COPY ./bin/agent /app/agent
RUN chmod +x /app/agent
#RUN apk add --no-cache curl
#ARG AGENT_DOWNLOAD_URL=http://localhost:8081/download/agent
#RUN curl -fsSL -o /app/agent ${AGENT_DOWNLOAD_URL} && chmod +x /app/agent


# 暴露业务端口和 Agent 指令端口
EXPOSE 8080 9090

# 启动脚本（后台运行 Agent，前台运行 Spring Boot）
COPY entrypoint.sh /app/entrypoint.sh
RUN chmod +x /app/entrypoint.sh

ENTRYPOINT ["/app/entrypoint.sh"]


