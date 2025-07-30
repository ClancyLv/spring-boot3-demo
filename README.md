# spring-boot3-demo

本项目为 Spring Boot 3.x 综合示例项目，旨在演示和集成常用的 Spring Boot 相关技术与最佳实践。

## 项目结构

- **mybatis-plus-demo**：Mybatis Plus 持久层框架示例
  - 详见 [mybatis-plus-demo/README.md](mybatis-plus-demo/README.md)
- **spring-rabbitmq-demo**：Spring 消息队列示例
  - 详见 [spring-rabbitmq-demo/README.md](spring-rabbitmq-demo/README.md)
- **spring-redis-demo**：Spring 缓存示例
  - 详见 [spring-redis-demo/README.md](spring-redis-demo/README.md)
- **spring-retry-demo**：Spring Retry 重试机制示例
  - 详见 [spring-retry-demo/README.md](spring-retry-demo/README.md)

> 后续将逐步添加更多子模块，涵盖缓存、消息队列、分布式事务、监控等常见场景。

## 环境要求
- JDK 17 及以上
- Maven 3.8+

## 使用说明

1. 克隆本项目：
   ```bash
   git clone -b master https://github.com/ClancyLv/spring-boot3-demo.git
   ```
2. 进入项目根目录，使用 Maven 构建：
   ```bash
   mvn clean install
   ```
3. 进入各子模块目录，参考对应 README 文档运行和体验示例。

## 交流反馈
如有建议或问题，欢迎 issue 或 PR。

---

本项目持续更新中，欢迎关注与 Star！
