package cn.clancy.mybatisplus;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author ClancyLv
 * @Date 2025/6/24 10:35
 * @Description SpringBoot启动类
 */
@MapperScan("cn.clancy.mybatisplus.mapper")
@SpringBootApplication
public class MybatisPlusDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(MybatisPlusDemoApplication.class, args);
    }
}
