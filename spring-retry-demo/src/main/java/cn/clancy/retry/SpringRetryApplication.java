package cn.clancy.retry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

/**
 * @Author ClancyLv
 * @Date 2025/6/19 10:57
 * @Description 启动类
 * 添加注解@EnableRetry开始重试功能
 */
@SpringBootApplication
@EnableRetry
public class SpringRetryApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringRetryApplication.class, args);
    }
}
