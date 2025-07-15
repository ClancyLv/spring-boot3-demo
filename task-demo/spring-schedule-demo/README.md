# Spring Schedule 示例

该项目是一个 Spring Boot 示例，用于演示如何使用 Spring Framework 内置的 `@Scheduled` 注解来创建和管理定时任务。

## 功能

- **基于注解**: 完全使用 `@Scheduled` 注解来定义任务，无需复杂的 XML 配置。
- **多种调度方式**:
    - **`cron`**: 使用 Cron 表达式定义复杂的调度规则（例如，"每分钟执行一次"）。
    - **`fixedRate`**: 以固定的频率执行任务，从上一次任务**开始**执行的时间点计算。
    - **`fixedDelay`**: 在上一次任务**执行完毕**后，等待一个固定的延迟时间，然后再次执行。
- **线程池管理**: 任务在 Spring 管理的线程池中异步执行。

## 如何使用

### 1. 启用调度

在您的 Spring Boot 主应用程序或任何配置类上，添加 `@EnableScheduling` 注解来开启定时任务的支持。

```java
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringScheduleDemoApplication {
    // ...
}
```

### 2. 创建一个调度任务

创建一个 Spring 组件（`@Component`），并在您希望调度的方法上添加 `@Scheduled` 注解。

```java
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class ScheduledTask {

    /**
     * 使用 cron 表达式，每分钟执行一次。
     */
    @Scheduled(cron = "0 */1 * * * *")
    public void cronTask() {
        System.out.println("Cron 任务执行于: " + LocalDateTime.now());
    }

    /**
     * fixedRate: 从上一次任务开始执行后，每隔 5 秒执行一次。
     */
    @Scheduled(fixedRate = 5000)
    public void fixedRateTask() {
        System.out.println("Fixed Rate 任务执行于: " + LocalDateTime.now());
    }

    /**
     * fixedDelay: 在上一次任务执行完毕后，延迟 10 秒再次执行。
     */
    @Scheduled(fixedDelay = 10000)
    public void fixedDelayTask() {
        System.out.println("Fixed Delay 任务执行于: " + LocalDateTime.now());
    }
}
```

### 3. 运行应用程序

直接运行 Spring Boot 应用程序。您将在控制台看到定时任务的输出。

## 配置

默认情况下，Spring 会创建一个单线程的线程池来执行所有任务。如果需要自定义线程池，您可以在配置类中添加一个 `TaskScheduler` bean，或者在 `application.yml` 文件中进行配置。

**通过 `application.yml` 配置:**

```yaml
spring:
  task:
    scheduling:
      pool:
        size: 10 # 设置线程池大小
      thread-name-prefix: my-scheduled-task- # 设置线程名前缀
```

## 核心组件

- **`SpringScheduleDemoApplication`**: Spring Boot 主应用程序类，使用 `@EnableScheduling` 开启调度。
- **`config/ScheduleConfig.java`**: （可选）用于配置任务调度器，例如线程池。
- **`job/ScheduledTask.java`**: 包含所有使用 `@Scheduled` 注解定义的定时任务。
