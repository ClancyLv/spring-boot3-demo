package cn.clancy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author ClancyLv
 * @Date 2025/7/8 09:22
 * @Description RabbitMQ消费者示例应用启动类
 */
@SpringBootApplication
public class ConsumerDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerDemoApplication.class, args);
    }
}