package cn.clancy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author ClancyLv
 * @Date 2025/7/18 09:46
 * @Description tidb数据库示例应用程序入口类
 */
@MapperScan("cn.clancy.mapper")
@SpringBootApplication
public class TidbDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(TidbDemoApplication.class, args);
    }
}