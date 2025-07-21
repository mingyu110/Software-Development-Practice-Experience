# 高性能支付服务 - 启动与使用说明

本文档提供了启动和使用高性能支付服务的详细步骤。

## 1. 环境要求

在启动本服务前，请确保您的系统中已安装并运行以下软件：

- **Java 11 或更高版本**
- **Apache Maven 3.6 或更高版本**
- **Docker (推荐)**，用于快速启动 Kafka 和 Redis
- **Git**

## 2. 依赖服务启动

本服务依赖于 Kafka 和 Redis。我们推荐使用 Docker Compose 来快速启动这两个服务。

### 使用 Docker Compose (推荐)

1.  在项目根目录下创建一个 `docker-compose.yml` 文件，内容如下：

    ```yaml
    version: '3.8'
    services:
      zookeeper:
        image: confluentinc/cp-zookeeper:7.0.1
        container_name: zookeeper
        environment:
          ZOOKEEPER_CLIENT_PORT: 2181
          ZOOKEEPER_TICK_TIME: 2000

      kafka:
        image: confluentinc/cp-kafka:7.0.1
        container_name: kafka
        ports:
          - "9092:9092"
        depends_on:
          - zookeeper
        environment:
          KAFKA_BROKER_ID: 1
          KAFKA_ZOOKEEPER_CONNECT: 'zookeeper:2181'
          KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka:29092,PLAINTEXT_HOST://localhost:9092
          KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT
          KAFKA_INTER_BROKER_LISTENER_NAME: PLAINTEXT
          KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1

      redis:
        image: redis:6.2-alpine
        container_name: redis
        ports:
          - "6379:6379"
    ```

2.  在项目根目录下，打开终端并运行以下命令来启动 Kafka 和 Redis：

    ```bash
    docker-compose up -d
    ```

### 手动安装

如果您不使用 Docker，请参照官方文档手动下载、安装和启动 Kafka 及 Redis，并确保它们分别在 `localhost:9092` 和 `localhost:6379` 上运行。

## 3. 服务编译与启动

1.  **克隆项目** (如果尚未克隆)

    ```bash
    git clone <your-repository-url>
    cd high-performance-payment-system
    ```

2.  **使用 Maven 编译和打包项目**

    在项目根目录下，运行以下命令：

    ```bash
    mvn clean install
    ```

    该命令会下载所有依赖，并生成一个可执行的 JAR 文件在 `target` 目录下。

3.  **启动服务**

    使用以下命令启动 Spring Boot 应用：

    ```bash
    java -jar target/high-performance-payment-system-1.0.0.jar
    ```

    服务启动后，它将监听 `8080` 端口。

## 4. 如何使用

服务启动后，您可以通过 HTTP 请求与其交互。

### 发起支付请求

使用 `curl` 或任何 API 测试工具向 `/api/payments` 端点发送一个 `POST` 请求。

**请求示例:**

```bash
curl -X POST http://localhost:8080/api/payments \
-H "Content-Type: application/json" \
-d '{
  "orderId": "ORDER-12345",
  "amount": 99.99,
  "currency": "USD",
  "userId": "USER-001"
}'
```

**成功响应:**

您将收到一个确认消息，表明请求已被接收并正在处理中。

```
支付请求已收到，正在处理中。
```

同时，您可以在服务的控制台日志中看到支付事件被发送到 Kafka 以及被消费者接收的记录。

## 5. 监控与健康检查

本服务集成了 Spring Boot Actuator，提供了多个用于监控和管理的端点。

-   **查看所有可用端点**: `http://localhost:8080/actuator`
-   **健康检查**: `http://localhost:8080/actuator/health`
-   **应用指标**: `http://localhost:8080/actuator/metrics`
-   **Prometheus 指标**: `http://localhost:8080/actuator/prometheus`

这些端点对于监控服务的运行状态和性能至关重要。
