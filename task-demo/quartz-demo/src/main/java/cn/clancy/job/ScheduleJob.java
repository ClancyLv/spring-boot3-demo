package cn.clancy.job;

import cn.clancy.manager.ScheduleManager;
import cn.clancy.model.JobData;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * @Author ClancyLv
 * @Date 2025/7/9 15:11
 * @Description Quartz 定时任务类
 */
@RequiredArgsConstructor
@Slf4j
public class ScheduleJob extends QuartzJobBean {
    private final ApplicationContext applicationContext;

    @Override
    protected void executeInternal(JobExecutionContext context) {
        JobData jobData = (JobData) context.getMergedJobDataMap().
                get(ScheduleManager.JOB_PARAM_KEY);

        // 任务开始时间
        LocalDateTime startTime = LocalDateTime.now();

        try {
            //执行任务
            log.info("任务准备执行，开始时间：{}，任务ID：{}，bean名：{}，方法名：{}", DateUtil.format(startTime, DatePattern.NORM_DATETIME_MS_PATTERN), jobData.getJobId(), jobData.getBeanName(), jobData.getMethodName());
            Object target = applicationContext.getBean(jobData.getBeanName());
            Method method = target.getClass().getDeclaredMethod(jobData.getMethodName(), String.class);
            method.invoke(target, jobData.getParams());

            // 任务结束时间
            LocalDateTime endTime = LocalDateTime.now();
            // 任务执行总时长（毫秒）
            long times = ChronoUnit.MILLIS.between(startTime, endTime);

            log.info("任务执行完毕，结束时间：{}，任务ID：{}  总共耗时：{} 毫秒", DateUtil.format(endTime, DatePattern.NORM_DATETIME_MS_PATTERN), jobData.getJobId(), times);
        } catch (Exception e) {
            // 任务结束时间
            LocalDateTime endTime = LocalDateTime.now();
            log.error("任务执行失败，结束时间：{}，任务ID：{}，失败原因：{}", endTime, jobData.getJobId(), e.getMessage(), e);
        }
    }
}
