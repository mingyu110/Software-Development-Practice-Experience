
package com.example.webfluxspringboot;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 领域模型：Product (产品)
 * 定义了系统的核心业务对象“产品”的数据结构。
 * 这个类作为数据传输对象（DTO），在不同层（如 Controller, Service, Repository）之间以及跨网络（如 Kafka, Redis, WebSocket）传输数据。
 *
 * --- 健壮性设计说明 ---
 * 当前使用 @Data 注解，它会自动生成 getter, setter, toString, equals, hashCode 等方法，简化了样板代码。
 * 但 @Data 生成的类是“可变的”（Mutable），即可以通过 setter 方法在创建后修改其内部状态。
 *
 * 在高并发和响应式编程（如本项目）中，推荐使用“不可变对象”（Immutable Objects）来增强健壮性。
 * 不可变对象一旦创建，其状态就不能被修改，这天然地保证了线程安全，避免了复杂的状态同步问题。
 * 在 Java 中，可以通过 final 关键字和只提供 getter 方法，或使用 Record 类型（Java 14+）来实现不变性。
 * 例如，使用 Record 的定义会更健壮：
 * public record Product(String id, String name, double price) {}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private String id;
    private String name;
    private double price;
}
