package cn.clancy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author ClancyLv
 * @Date 2025/7/18 13:38
 * @Description Postgresql 数据源演示应用程序
 */
@MapperScan("cn.clancy.mapper")
@SpringBootApplication
public class PostgresqlDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(PostgresqlDemoApplication.class, args);
    }
}