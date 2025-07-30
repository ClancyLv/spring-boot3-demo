# SSE示例

## 简介
该模块演示了如何使用 Spring MVC 实现 Server-Sent Events (SSE)。SSE 是一种服务器向客户端推送实时更新的技术，适用于需要实时数据更新的场景。

## 功能
- SSE 事件订阅与推送
- 管理 SSE 连接

## 主要组件

### 1. SpringMvcSseDemoApplication
- 应用程序的入口类，负责启动 Spring Boot 应用。

### 2. SseController
- 控制器类，提供 SSE 相关的 API。
- 主要功能：
  - 订阅 SSE 事件
  - 关闭 SSE 连接

### 3. SseManager
- 管理器类，负责管理 SSE 连接和事件推送。
- 提供核心逻辑以支持多客户端连接。

## 如何运行
1. 确保已安装 JDK 17 或更高版本。
2. 使用以下命令启动应用程序：
   ```bash
   mvn spring-boot:run
   ```
3. 访问 `http://localhost:8080/sse` 测试 SSE 功能。

## 依赖
- Spring Boot 3.x
- Lombok
