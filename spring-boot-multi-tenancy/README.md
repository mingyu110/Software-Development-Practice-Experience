[English](./README.md) | [中文](./README_CN.md)

# Spring Boot Multi-Tenancy Example

This project is a practical implementation of the multi-tenancy pattern in Spring Boot, specifically using the **"Database per Tenant"** strategy. It dynamically routes database connections based on an `X-Tenant-ID` HTTP header.

## Features

- **Dynamic Routing DataSource**: Uses Spring's `AbstractRoutingDataSource` to switch connections at runtime.
- **Configuration via YAML**: Tenant databases are configured in an external `databases.yml` file for clarity and ease of management.
- **Header-based Tenant Resolution**: Identifies the current tenant via the `X-Tenant-ID` header in incoming HTTP requests.
- **Automatic Database Migration**: Uses Flyway to automatically apply SQL migrations to each tenant's database upon application startup.
- **Clean & Standard Project Structure**: Organized for clarity and best practices.

## How It Works

1.  An `HandlerInterceptor` (`TenantIdentifierInterceptor`) reads the `X-Tenant-ID` header from the request and stores the tenant ID in a `ThreadLocal` variable (`TenantContext`).
2.  The `RoutingDataSource` implementation uses this `ThreadLocal` value as a lookup key to determine which database connection to use.
3.  The application loads all tenant database configurations from `databases.yml` at startup.
4.  For each configured datasource, Flyway migrations are automatically executed to ensure the schema is up-to-date.

## Prerequisites

- Java 17 or later
- Maven 3.6 or later
- Docker and Docker Compose (for running PostgreSQL databases easily)
- A running PostgreSQL instance. You need to create the databases and users defined in `databases.yml`.

## Setup & Running

1.  **Configure Databases**:
    -   Make sure you have PostgreSQL running.
    -   Create the databases and users as defined in `databases.yml`. For example:
        ```sql
        CREATE DATABASE tenant_1_db;
        CREATE USER user1 WITH PASSWORD 'password1';
        GRANT ALL PRIVILEGES ON DATABASE tenant_1_db TO user1;

        CREATE DATABASE tenant_2_db;
        -- etc.
        ```
    -   Update the `databases.yml` file with your actual database URLs, usernames, and passwords.

2.  **Build the Project**:
    ```bash
    mvn clean install
    ```

3.  **Run the Application**:
    ```bash
    java -jar target/spring-boot-multi-tenancy-*.jar
    ```
    The application will start on port 8080.

## Testing the API

You can use a tool like `curl` or Postman to test the endpoint. The key is to provide the `X-Tenant-ID` header.

**Querying Tenant 1:**
```bash
curl -H "X-Tenant-ID: tenant_1" http://localhost:8080/api/cities
```
This should return the cities from the `tenant_1_db` database.

**Querying Tenant 2:**
```bash
curl -H "X-Tenant-ID: tenant_2" http://localhost:8080/api/cities
```
This should return the cities from the `tenant_2_db` database.

**Querying a Shared DB Tenant:**
```bash
curl -H "X-Tenant-ID: tenant_3" http://localhost:8080/api/cities
```
This should return the cities from the `shared_db` database.