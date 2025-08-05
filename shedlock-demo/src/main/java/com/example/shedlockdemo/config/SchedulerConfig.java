package com.example.shedlockdemo.config;

import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.sql.DataSource;

/**
 * ShedLock 和调度任务的配置类。
 */
@Configuration
// 启用 Spring 的计划任务功能
@EnableScheduling
// 启用 ShedLock，并设置默认的锁最长持有时间为 10 分钟
@EnableSchedulerLock(defaultLockAtMostFor = "10m")
public class SchedulerConfig {

    /**
     * 定义 LockProvider bean，用于提供分布式锁的实现。
     * @param dataSource Spring Boot 自动配置的数据源。
     * @return 基于 JDBC Template 的 LockProvider 实例。
     */
    @Bean
    public LockProvider lockProvider(DataSource dataSource) {
        return new JdbcTemplateLockProvider(
            JdbcTemplateLockProvider.Configuration.builder()
                // 使用 Spring 的 JdbcTemplate 与数据库交互
                .withJdbcTemplate(new JdbcTemplate(dataSource))
                // 使用数据库时间来确保不同服务器之间的一致性
                .usingDbTime()
                .build()
        );
    }
}
