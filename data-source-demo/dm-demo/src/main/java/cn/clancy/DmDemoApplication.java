package cn.clancy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author ClancyLv
 * @Date 2025/7/23 17:21
 * @Description 达梦数据库演示程序
 */
@MapperScan("cn.clancy.mapper")
@SpringBootApplication
public class DmDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DmDemoApplication.class, args);
    }
}