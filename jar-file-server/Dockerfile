# 第一阶段：构建（使用 Maven 镜像）
FROM maven:3.8.4-openjdk-11-slim AS build

# 设置工作目录
WORKDIR /app

# 先复制 pom.xml 以利用 Docker 缓存，避免每次重新下载依赖
COPY pom.xml .
# RUN mvn dependency:go-offline -B

# 复制全部源码
COPY src ./src

# 打包（跳过测试，加快构建）
RUN mvn clean package -DskipTests

# 第二阶段：运行（使用精简 JRE 镜像）
FROM eclipse-temurin:11-jre-alpine

# 创建非 root 用户运行应用（安全）
# RUN groupadd -r spring && useradd -r -g spring spring

# 设置工作目录
WORKDIR /app

# 从构建阶段复制 jar 包
COPY --from=builder /app/target/*.jar app.jar

# 创建文件上传目录（volume 挂载点）
RUN mkdir -p /app/uploads
# && chown spring:spring /app/uploads

# 切换到非 root 用户
# USER spring

# 暴露端口
EXPOSE 8081 8081

# 启动应用
ENTRYPOINT ["java", "-jar", "app.jar"]

