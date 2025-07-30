# spring-retry-demo

本模块是基于 Spring Retry 实现的重试机制示例，演示如何通过注解方式优雅地为服务方法添加自动重试与兜底恢复能力，适用于远程调用、网络请求、数据库操作等易失败场景。

# 教程

## 1、添加依赖

```xml
<dependencies>
    <!--spring重试，这里使用的是由spring-boot管理的2.0.12版本-->
    <dependency>
        <groupId>org.springframework.retry</groupId>
        <artifactId>spring-retry</artifactId>
    </dependency>
    <!--aspectj切面-->
    <dependency>
        <groupId>org.aspectj</groupId>
        <artifactId>aspectjweaver</artifactId>
        <version>${aspectjweaver.version}</version>
    </dependency>
</dependencies>
```

## 2、启动类开启重试功能
```java
@SpringBootApplication
@EnableRetry
public class SpringRetryApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringRetryApplication.class, args);
    }
}
```

## 3、使用重试功能

### 3.1 编写重试Service接口
```java
public interface RetryService {
    boolean defaultRetryMethod(int num);
    boolean customRetryMethod(String str);
}
```

### 3.2 编写重试Service实现
```java
@Slf4j
@Service
public class RetryServiceImpl implements RetryService {
    private static int ATTEMPTS = 1;

    /**
     * 默认重试示例
     */
    @Retryable
    @Override
    public boolean defaultRetryMethod(int num) {
        log.info("第{}次执行！！！", ATTEMPTS++);
        if (num < 0) {
            throw new IllegalArgumentException("参数num小于0");
        }
        log.info("执行成功！！！");
        return true;
    }

    @Recover
    public boolean defaultRetryMethodIllegalArgumentExceptionRecover(IllegalArgumentException e, int num) {
        log.info("重试失败，IllegalArgumentException异常，调用默认方法！！！");
        return false;
    }

    /**
     * 自定义重试参数示例
     * retryFor：指定重试异常类型
     * maxAttempts：最大重试次数4次
     * backoff：初始间隔2秒，后续依次为3秒、4.5秒。即每次重试的间隔分别为2000ms, 3000ms, 4500ms
     */
    @Retryable(
        retryFor = IllegalStateException.class,
        maxAttempts = 4,
        backoff = @Backoff(delay = 2000, multiplier = 1.5)
    )
    @Override
    public boolean customRetryMethod(String str) {
        log.info("customRetryMethod第{}次执行，参数：{}", ATTEMPTS++, str);
        if (str == null || str.isEmpty()) {
            throw new IllegalStateException("参数str为空");
        }
        log.info("customRetryMethod执行成功！");
        return true;
    }

    @Recover
    public boolean customRetryMethodRecover(IllegalStateException e, String str) {
        log.info("customRetryMethod重试失败，调用recover方法，异常：{}，参数：{}", e.getMessage(), str);
        return false;
    }
}
```

### 3.3 单元测试
```java
@SpringBootTest
class RetryServiceImplTest {
    @Autowired
    private RetryService retryService;

    @Test
    void testDefaultRetryMethodSuccess() {
        boolean result = retryService.defaultRetryMethod(1);
        Assertions.assertTrue(result);
    }

    @Test
    void testDefaultRetryMethodFailAndRecover() {
        boolean result = retryService.defaultRetryMethod(-1);
        Assertions.assertFalse(result);
    }

    @Test
    void testCustomRetryMethodSuccess() {
        boolean result = retryService.customRetryMethod("hello");
        Assertions.assertTrue(result);
    }

    @Test
    void testCustomRetryMethodFailAndRecover() {
        boolean result = retryService.customRetryMethod("");
        Assertions.assertFalse(result);
    }

    @Test
    void testCustomRetryMethodNullAndRecover() {
        boolean result = retryService.customRetryMethod(null);
        Assertions.assertFalse(result);
    }
}
```

## 4、常见问题
- @Retryable注解的value属性已弃用，推荐使用retryFor。
- @Retryable注解的backoff属性可灵活配置重试间隔和递增倍数。
- @Recover注解的方法用于所有重试失败后的兜底处理。

## 5、参考
- [Spring Retry文档](https://springdoc.cn/spring-retry-guide/)
