# Quartz 示例

该项目是一个 Spring Boot 示例，演示了如何使用 Quartz Scheduler 进行动态和持久化的任务调度。它提供了一个强大的框架，用于创建、管理和执行存储在数据库中的调度作业。

## 功能

- **动态任务管理**: 在运行时创建、更新和删除调度任务。
- **持久化作业存储**: 利用 JDBC JobStore 将作业和触发器信息持久化到数据库中，确保在应用程序重启时任务不会丢失。
- **多种调度策略**: 支持用于复杂调度的 cron 表达式和用于重复任务的简单秒级间隔。
- **作业控制**: 暂停、恢复或立即触发任何调度作业。
- **批量操作**: 对整组作业执行暂停、恢复或删除等操作。
- **集中管理**: 专用的 `ScheduleManager` 为所有调度操作提供了清晰的 API。

## 设置与配置

### 1. 数据库设置

该项目需要一个 MySQL 数据库来存储 Quartz 作业数据。

1.  **配置连接**: 在 `src/main/resources/application.yml` 中更新数据库 URL、用户名和密码：
    ```yaml
    spring:
      datasource:
        url: jdbc:mysql://localhost:3306/spring-boot3-demo?serverTimezone=Asia/Shanghai
        username: root
        password: 123456
    ```
2.  **创建表**: 在目标数据库中执行位于 `src/main/resources/db/tables_mysql_innodb.sql` 的 SQL 脚本。这将创建 Quartz 用于 JDBC JobStore 的所有必需的 `QRTZ_` 表。

### 2. 应用程序配置

核心 Quartz 配置位于 `src/main/resources/application.yml` 中。

- **作业存储类型**: `job-store-type` 设置为 `jdbc` 以启用数据库持久化。
  ```yaml
  spring:
    quartz:
      job-store-type: jdbc
  ```
- **调度器自动启动**: 调度器配置为在应用程序启动时不自动启动 (`auto-startup: false`)。您必须在应用程序准备就绪时手动启动它。
  ```yaml
  spring:
    quartz:
      auto-startup: false
  ```
  您可以通过注入 `Scheduler` bean 并调用其 `start()` 方法来启动调度器，例如，在应用程序监听器中：
  ```java
  @Autowired
  private Scheduler scheduler;

  @EventListener(ApplicationReadyEvent.class)
  public void startScheduler() throws SchedulerException {
      scheduler.start();
  }
  ```

## 如何使用

### 1. 创建一个作业 Bean

首先，创建一个标准的 Spring bean，其中包含您要调度的方法。该方法应接受一个 `String` 类型的参数。

```java
@Component("myTask")
@Slf4j
public class MyTask {
    public void run(String params) {
        log.info("MyTask 正在运行，参数为: {}", params);
    }
}
```

### 2. 使用 ScheduleManager

将 `ScheduleManager` 注入到您的服务中以管理作业。

```java
@Autowired
private ScheduleManager scheduleManager;
```

#### 调度一个 Cron 作业

```java
// 调度每5秒运行一次
String jobId = "myCronJob";
String cron = "0/5 * * * * ?";
String beanName = "myTask";
String methodName = "run";
String params = "{\"source\":\"cron\"}";

scheduleManager.createScheduleJob(jobId, null, cron, beanName, methodName, params);
```

#### 调度一个秒级间隔作业

```java
// 调度每10秒运行一次
String jobId = "myIntervalJob";
int intervalInSeconds = 10;
String beanName = "myTask";
String methodName = "run";
String params = "{\"source\":\"interval\"}";

scheduleManager.createSecondScheduleJob(jobId, null, intervalInSeconds, beanName, methodName, params);
```

#### 更新一个作业

您可以更改现有作业的 cron 表达式或其他参数。

```java
String newCron = "0/15 * * * * ?"; // 更改为每15秒运行一次
scheduleManager.updateScheduleJob(jobId, null, newCron, beanName, methodName, params);
```

#### 暂停和恢复一个作业

```java
// 暂停作业
scheduleManager.pauseJob(jobId, null);

// 恢复作业
scheduleManager.resumeJob(jobId, null);
```

#### 立即运行一个作业

您可以触发一个作业立即运行，而不受其调度计划的限制。

```java
scheduleManager.run(jobId, null, beanName, methodName, "{\"source\":\"manual\"}");
```

#### 删除一个作业

```java
scheduleManager.deleteJob(jobId, null);
```

## 核心组件

- **`ScheduleManager`**: 与 Quartz 调度器交互的主要服务。它抽象了 Quartz API 的底层细节。
- **`ScheduleJob`**: 由调度器实际执行的通用 `QuartzJobBean`。它使用反射来调用指定 Spring bean 上的目标方法。
- **`JobData`**: 一个简单的数据类，持有 `ScheduleJob` 执行所需的信息（目标 bean、方法、参数）。
- **`application.yml`**: 包含数据库和 Quartz 属性的所有配置。
- **`db/tables_mysql_innodb.sql`**: Quartz JDBC JobStore 所需的数据库模式脚本。
- **`entity/ScheduleEntity.java`**: 一个示例实体，可用于在您自己的应用程序表中持久化作业元数据，从而实现更复杂的管理界面。`ScheduleManager` 不直接使用它，但它可作为一种设计模式。