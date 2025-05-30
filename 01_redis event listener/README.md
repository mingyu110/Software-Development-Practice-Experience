# Redis Event Listener

这是一个Spring Boot应用程序，用于演示如何监听Redis的键空间通知(Key Space Notifications)，特别是键过期事件，并根据这些事件触发业务逻辑。

## 技术栈

- Java 17
- Spring Boot 3.2
- Spring Data Redis
- Docker (用于运行Redis)

## 项目结构

```
redis-event-listener/
├── src/
│   ├── main/
│   │   ├── java/com/example/redislistener/
│   │   │   ├── RedisListenerApplication.java (主应用类)
│   │   │   ├── config/
│   │   │   │   ├── RedisConfig.java (Redis配置)
│   │   │   ├── listener/
│   │   │   │   ├── RedisKeyExpirationListener.java (键过期监听器)
│   │   │   ├── service/
│   │   │   │   ├── BusinessService.java (业务逻辑服务)
│   │   │   │   ├── CacheService.java (缓存操作服务)
│   │   │   ├── controller/
│   │   │   │   ├── DemoController.java (REST API控制器)
│   │   ├── resources/
│   │   │   ├── application.properties (应用配置)
├── docker-compose.yml (Docker配置)
├── build.gradle (Gradle构建配置)
```

## 运行步骤

1. 启动Redis:

```bash
docker-compose up -d
```

2. 构建并运行应用:

```bash
./gradlew bootRun
```

## 测试API

1. 设置带过期时间的缓存:

```bash
curl -X POST "http://localhost:8080/api/cache?key=test-key&value=test-value&expirySeconds=10"
```

2. 获取缓存值:

```bash
curl -X GET "http://localhost:8080/api/cache/test-key"
```

3. 删除缓存:

```bash
curl -X DELETE "http://localhost:8080/api/cache/test-key"
```

## 观察过期事件

设置一个短时间过期的键，然后观察控制台输出。键过期后，应用将打印类似以下内容:

```
Key expired: test-key
Business logic triggered for expired key: test-key
```

## 注意事项

- Redis必须配置`notify-keyspace-events`参数以启用键空间通知
- 当前配置仅监听键过期事件，可以根据需要修改为监听其他事件 