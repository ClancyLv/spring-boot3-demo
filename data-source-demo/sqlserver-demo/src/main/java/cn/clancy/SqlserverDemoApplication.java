package cn.clancy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author ClancyLv
 * @Date 2025/7/21 16:17
 * @Description Sqlserver 应用程序入口
 */
@MapperScan("cn.clancy.mapper")
@SpringBootApplication
public class SqlserverDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(SqlserverDemoApplication.class, args);
    }
}