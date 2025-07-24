# Spring WebFlux SSE 示例

该模块演示了如何使用 Spring WebFlux 实现服务器发送事件 (SSE)。

## 1. 概述

本项目是一个简单的 Spring Boot 应用程序，它公开了一个 SSE 端点。当客户端连接到此端点时，服务器将每秒推送一条消息。

- **`SpringWebfluxSseDemoApplication.java`**: Spring Boot 应用程序的主入口点。
- **`SseController.java`**: 包含处理 SSE 逻辑的控制器。它提供了一个位于 `/sse/stream` 的端点。

## 2. 如何运行

您可以直接使用 Maven 运行该应用程序：

```bash
mvn spring-boot:run
```

或者，您可以从 IDE 中运行 `SpringWebfluxSseDemoApplication` 类中的 `main` 方法。

该应用程序将在默认端口（通常是 8080）上启动。

## 3. 如何测试

应用程序运行后，您可以在终端中使用 `curl` 命令测试 SSE 端点：

```bash
curl http://localhost:8080/sse/stream
```

您将看到一个事件流，每秒都会出现一个新事件：

```
id:d8b8c6e3-c8e9-4b2f-b6d6-2b3a0c5b3e1a
event:default_event
data:SSE消息内容：16:30:01.123456789

id:a1b2c3d4-e5f6-7890-1234-567890abcdef
event:default_event
data:SSE消息内容：16:30:02.123456789

id:b2c3d4e5-f6a7-8901-2345-67890abcdef1
event:default_event
data:SSE消息内容：16:30:03.123456789

...
```

您也可以在支持 SSE 的 Web 浏览器（如 Chrome、Firefox 或 Safari）中打开 `http://localhost:8080/sse/stream` 来查看事件。