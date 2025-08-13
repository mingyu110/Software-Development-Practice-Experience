
package com.example.webfluxspringboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot 应用主类
 *
 * @SpringBootApplication 注解是一个复合注解，它包含了：
 * - @SpringBootConfiguration: 标记该类为 Spring 的配置类。
 * - @EnableAutoConfiguration: 启用 Spring Boot 的自动配置机制，根据类路径中的 jar 包和定义的 Bean 自动配置应用。
 * - @ComponentScan: 自动扫描该类所在包及其子包下的组件（如 @Component, @Service, @RestController 等）。
 */
@SpringBootApplication
public class WebfluxSpringbootApplication {

    /**
     * 应用主入口方法。
     * 调用 SpringApplication.run() 来启动整个 Spring 应用。
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(WebfluxSpringbootApplication.class, args);
    }

}
