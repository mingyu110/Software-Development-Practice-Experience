# Spring Cloud 微服务展示项目

本项目是一个使用 Spring Cloud、Java 21 和 Docker 构建的微服务架构展示项目。

## 服务模块

本项目包含以下核心微服务模块：

*   `discovery-server`: Eureka 服务发现中心，用于服务的注册与发现。
*   `config-server`: 集中式配置服务器，为所有微服务提供外部化配置。
*   `api-gateway`: API 网关，作为所有外部请求的统一入口，负责请求路由、负载均衡等。
*   `auth-service`: 认证与授权服务，处理用户身份验证和权限管理。
*   `order-service`: 订单服务，处理订单相关的业务逻辑。
*   `payment-service`: 支付服务，处理支付相关的业务逻辑。

### Docker Compose 部署遇到的问题

在实际部署过程中，遇到了一些挑战，主要包括：

1.  **服务启动时序和依赖管理复杂性**：微服务之间存在复杂的启动依赖关系（这需要多次调试和设置参数，因此不建议在生产环境使用Docker Compose部署分布式微服务架构应用），即使使用 `depends_on` 和 `healthcheck`，也难以完全避免服务在依赖项未完全就绪时尝试连接的问题。
2.  **配置加载问题**：Config Server 在加载配置仓库时遇到路径和模式不匹配的问题。
3.  **Docker 环境配置问题**：例如 Docker 镜像加速器导致镜像拉取失败等。
4.  **第三方服务健康检查的挑战**：部分第三方服务（如 RabbitMQ）的健康检查配置可能与容器环境不完全兼容，导致误报。

关于这些问题的详细分析和解决过程，请参考 `docs/Deployment_Summary.md` 文档中的 **"3. 本次部署遇到的主要问题"** 部分。

## 为什么建议使用 Kubernetes

鉴于 Docker Compose 在处理复杂微服务架构的启动时序、依赖管理、高可用性、弹性伸缩和自动化运维方面的局限性，我们强烈建议在生产环境中使用 Kubernetes 进行部署。

Kubernetes 提供了更健壮、自动化和可扩展的解决方案，能够有效解决我们在 Docker Compose 部署中遇到的诸多问题。

关于 Kubernetes 如何解决这些问题以及其优势的详细说明，请参考 `docs/Deployment_Summary.md` 文档中的 **"4. 使用 Kubernetes 部署的优势与实践"** 部分。

## 项目架构设计与核心组件

本项目的整体架构设计和服务模块划分，以及所使用的核心组件（如 Eureka、Spring Cloud Config、API Gateway 等）的详细介绍，请参考 `docs/architecture-manual.md` 文档。

