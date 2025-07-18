package cn.clancy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author ClancyLv
 * @Date 2025/7/18 16:23
 * @Description MariaDB 数据源演示应用程序入口
 */
@MapperScan("cn.clancy.mapper")
@SpringBootApplication
public class MariadbDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(MariadbDemoApplication.class, args);
    }
}