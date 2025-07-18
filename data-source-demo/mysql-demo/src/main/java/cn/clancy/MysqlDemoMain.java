package cn.clancy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author ClancyLv
 * @Date 2025/7/17 14:49
 * @Description MySQL 数据源演示主类
 */
@MapperScan("cn.clancy.mapper")
@SpringBootApplication
public class MysqlDemoMain {
    public static void main(String[] args) {
        SpringApplication.run(MysqlDemoMain.class, args);
    }
}