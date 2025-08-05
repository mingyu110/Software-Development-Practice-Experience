package com.example.shedlockdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot 应用主入口类。
 * @SpringBootApplication 注解是多个注解的组合，主要包括：
 * - @Configuration: 标记该类为 Spring 的配置类。
 * - @EnableAutoConfiguration: 启用 Spring Boot 的自动配置机制。
 * - @ComponentScan: 扫描当前包及其子包下的组件。
 */
@SpringBootApplication
public class ShedlockDemoApplication {

    /**
     * 应用主方法，程序的起点。
     * @param args 命令行参数。
     */
    public static void main(String[] args) {
        SpringApplication.run(ShedlockDemoApplication.class, args);
    }

}
