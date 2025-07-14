package cn.clancy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * @Author ClancyLv
 * @Date 2025/7/9 11:28
 * @Description 配置类--定时任务
 * 注解@EnableScheduling 用于开启 Spring 的定时任务支持。
 */
@Configuration
@EnableScheduling
public class ScheduleConfig {
    /**
     * （已经在yml文件配置了）配置并返回一个线程池任务调度器，用于执行定时任务。
     *
     * @return 一个配置好的 {@link ThreadPoolTaskScheduler} 对象，包含线程池大小设置以及线程名前缀。
     */
//    @Bean
//    public ThreadPoolTaskScheduler taskScheduler() {
//        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
//        // 线程池大小 = 核心数 × 2 （通过Runtime.getRuntime().availableProcessors()获取核心数）
//        scheduler.setPoolSize(10);
//        // 线程前缀名称
//        scheduler.setThreadNamePrefix("ScheduledTask-");
//        return scheduler;
//    }

}
