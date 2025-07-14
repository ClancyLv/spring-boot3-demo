package cn.clancy.job;

import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @Author ClancyLv
 * @Date 2025/7/9 11:47
 * @Description 注解方式的定时任务
 * Cron 表达式格式:
 * 秒 分 小时 日 月 星期 [年]
 * 字段名  允许值 特殊字符
 * 秒    0-59    , - * /
 * 分    0-59    , - * /
 * 小时   0-23    , - * /
 * 日    1-31    , - * / ? L W C
 * 月    1-12    , - * /` 或 JAN-DEC
 * 星期   1-7     , - * / ? L #` 或 SUN-SAT
 * 年    可省略     1970-2099
 */
@Component
public class ScheduledTask {

    /**
     * fixedRate	上一次开始执行时间点之后多长时间再次执行	@Scheduled(fixedRate = 5000)
     * fixedDelay	上一次执行结束后多长时间再次执行	@Scheduled(fixedDelay = 5000)
     * cron	使用 cron 表达式	@Scheduled(cron = "0/5 * * * * ?")
     */
    @Scheduled(cron = "0 */1 * * * *")
    public void cronTask() {
        System.out.print("时间：" + LocalDateTime.now() + "，当前线程名:" + Thread.currentThread().getName() + "，cron表达式: 每分钟执行一次");
    }

    /**
     * fixedRateTask方法是一个定时任务方法，被注解@Scheduled(fixedRate = 5000)标记。
     * 功能描述：
     * 该方法每隔5秒执行一次，是通过fixedRate属性配置的定时任务。任务执行时不考虑上一次任务的结束时间，
     * 而是基于上一次任务的开始时间点来计算下一次任务的执行时间。
     * 注意事项：
     * - 适合任务执行时间较短且任务调度频率固定的场景。
     * - 如果任务执行时间超过了fixedRate指定的间隔时间，可能会出现调度堆积。
     */
    @Scheduled(fixedRate = 5000)
    public void fixedRateTask() {
        // 获取当前线程名
        System.out.print("时间：" + LocalDateTime.now() + "，当前线程名:" + Thread.currentThread().getName() + "，fixedRate: 每5秒执行一次");
    }

    /**
     * fixedDelayTask方法是一个定时任务方法，被注解@Scheduled(fixedDelay = 10000)标记。
     * 功能描述：
     * 该方法的定时任务将在上一次任务执行结束后间隔10秒再次执行。
     * 注意事项：
     * - 此调度方式适用于需要确保任务执行完毕后再间隔固定时间执行下一次任务的场景。
     * - 如果任务执行时间超过指定的fixedDelay间隔时间，无需担心发生任务堆积。
     */
    @SneakyThrows
    @Scheduled(fixedDelay = 10000)
    public void fixedDelayTask() {
        // 获取当前线程名
        System.out.print("时间：" + LocalDateTime.now() + "，当前线程名:" + Thread.currentThread().getName() + "，fixedDelay: 上一次任务执行结束后10秒再次执行");
    }
}
