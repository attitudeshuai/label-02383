# 布布一二消消乐 - Dockerfile
# 多阶段构建，支持 ARM 和 X86 架构

# 构建阶段
FROM --platform=$BUILDPLATFORM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

# 复制 pom.xml 并下载依赖
COPY pom.xml .
RUN mvn dependency:go-offline -B

# 复制源代码
COPY src ./src

# 先生成图片资源
RUN mvn compile exec:java -Dexec.mainClass="com.bubu.game.ImageGenerator" -q || true

# 构建项目
RUN mvn clean package -DskipTests -q

# 运行阶段 - 使用支持 GUI 的基础镜像
FROM eclipse-temurin:17-jre

WORKDIR /app

# 安装 X11 相关依赖（用于 GUI 显示）
RUN apt-get update && apt-get install -y \
    libxext6 \
    libxrender1 \
    libxtst6 \
    libxi6 \
    fontconfig \
    fonts-wqy-zenhei \
    && rm -rf /var/lib/apt/lists/*

# 从构建阶段复制 JAR 文件
COPY --from=builder /app/target/bubu-match3-game-1.0.0.jar ./game.jar

# 设置环境变量
ENV DISPLAY=:0

# 运行游戏
CMD ["java", "-jar", "game.jar"]
