[English](./README.md) | [中文](./README_CN.md)

# Spring Boot 多租户实现指南

本项目是一个基于 Spring Boot 的多租户模式的实践项目，具体采用了 **“每个租户一个数据库”** 的策略。它能够根据 HTTP 请求头中的 `X-Tenant-ID` 动态地路由数据库连接。


## 功能特性

- **动态路由数据源**: 使用 Spring 的 `AbstractRoutingDataSource` 在运行时切换数据源连接。
- **YAML 外部化配置**: 所有租户的数据库连接信息都在外部的 `databases.yml` 文件中进行配置，清晰且易于管理。
- **基于请求头的租户解析**: 通过 HTTP 请求中的 `X-Tenant-ID` 请求头来识别当前租户。
- **自动数据库迁移**: 在应用启动时，使用 Flyway 为每一个租户的数据库自动执行 SQL 迁移脚本。
- **清晰标准的项目结构**: 遵循最佳实践，项目结构清晰明了。

## 工作原理

1.  一个 `HandlerInterceptor` (`TenantIdentifierInterceptor`) 从请求中读取 `X-Tenant-ID` 请求头，并将租户ID存入一个 `ThreadLocal` 变量 (`TenantContext`) 中。
2.  `RoutingDataSource` 的实现类使用这个 `ThreadLocal` 中的值作为查找键（lookup key），来决定应该使用哪个数据库连接。
3.  应用在启动时，会从 `databases.yml` 文件中加载所有租户的数据库配置。
4.  对于每一个配置好的数据源，项目会自动运行 Flyway 迁移，以确保数据库表结构是最新的。

## 环境要求

- Java 17 或更高版本
- Maven 3.6 或更高版本
- Docker 和 Docker Compose (用于快速搭建 PostgreSQL 环境)
- 一个正在运行的 PostgreSQL 实例。您需要预先创建 `databases.yml` 中定义的数据库和用户。

## 安装与运行

1.  **配置数据库**:
    -   确保您的 PostgreSQL 服务正在运行。
    -   根据 `databases.yml` 文件中的定义，创建相应的数据库和用户。例如：
        ```sql
        CREATE DATABASE tenant_1_db;
        CREATE USER user1 WITH PASSWORD 'password1';
        GRANT ALL PRIVILEGES ON DATABASE tenant_1_db TO user1;

        CREATE DATABASE tenant_2_db;
        -- 以此类推
        ```
    -   更新 `databases.yml` 文件，填入您真实的数据库URL、用户名和密码。

2.  **构建项目**:
    ```bash
    mvn clean install
    ```

3.  **运行应用**:
    ```bash
    java -jar target/spring-boot-multi-tenancy-*.jar
    ```
    应用将在 8080 端口启动。

## 测试 API

您可以使用 `curl` 或 Postman 等工具来测试接口。关键在于提供 `X-Tenant-ID` 请求头。

**查询租户1的数据:**
```bash
curl -H "X-Tenant-ID: tenant_1" http://localhost:8080/api/cities
```
这个请求应该会返回 `tenant_1_db` 数据库中的城市列表。

**查询租户2的数据:**
```bash
curl -H "X-Tenant-ID: tenant_2" http://localhost:8080/api/cities
```
这个请求应该会返回 `tenant_2_db` 数据库中的城市列表。

**查询共享数据库的租户:**
```bash
curl -H "X-Tenant-ID: tenant_3" http://localhost:8080/api/cities
```
这个请求应该会返回 `shared_db` 数据库中的城市列表。