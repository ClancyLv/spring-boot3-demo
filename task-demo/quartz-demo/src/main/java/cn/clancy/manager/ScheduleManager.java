package cn.clancy.manager;

import cn.clancy.job.ScheduleJob;
import cn.clancy.model.JobData;
import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author ClancyLv
 * @Date 2025/7/9 17:52
 * @Description 管理器--定时任务
 */
@RequiredArgsConstructor
@Component
public class ScheduleManager {
    private final Scheduler scheduler;

    /**
     * 任务id前缀
     */
    private final static String JOB_NAME = "TASK_";
    /**
     * 任务组前缀
     */
    private final static String GROUP_NAME = "GROUP_";
    /**
     * 任务调度参数key
     */
    public static final String JOB_PARAM_KEY = "JOB_PARAM_KEY";

    /**
     * 获取任务id
     */
    public String getJobId(String jobId) {
        return JOB_NAME + jobId;
    }

    /**
     * 获取组名
     */
    public String getGroupName(String groupName) {
        if (groupName == null) {
            return null;
        }
        return GROUP_NAME + groupName;
    }

    /**
     * 获取触发器key
     */
    public TriggerKey getTriggerKey(String jobId) {
        return getTriggerKey(jobId, null);
    }

    /**
     * 获取触发器key
     */
    public TriggerKey getTriggerKey(String jobId, String groupName) {
        return TriggerKey.triggerKey(getJobId(jobId), getGroupName(groupName));
    }

    /**
     * 获取jobKey
     */
    public JobKey getJobKey(String jobId) {
        return JobKey.jobKey(jobId, null);
    }

    /**
     * 获取jobKey
     */
    public JobKey getJobKey(String jobId, String groupName) {
        return JobKey.jobKey(getJobId(jobId), getGroupName(groupName));
    }

    /**
     * 获取jobKey列表
     */
    public List<JobKey> getJobKeys(List<String> jobIds) {
        List<JobKey> jobKeys = new ArrayList<>();
        jobIds.forEach(jobId -> jobKeys.add(getJobKey(jobId)));
        return jobKeys;
    }

    /**
     * 获取jobKey列表
     */
    public List<JobKey> getJobKeys(List<String> jobIds, List<String> groupNames) {
        if (jobIds.size() != groupNames.size()) {
            throw new IllegalArgumentException("任务id和组名列表必须具有相同的大小");
        }
        List<JobKey> jobKeys = new ArrayList<>(jobIds.size());
        for (int i = 0; i < jobIds.size(); i++) {
            String jobId = jobIds.get(i);
            String groupName = groupNames.get(i);
            jobKeys.add(getJobKey(jobId, groupName));
        }
        return jobKeys;
    }

    /**
     * 获取表达式触发器
     */
    public CronTrigger getCronTrigger(String jobId) throws SchedulerException {
        return getCronTrigger(jobId, null);
    }

    /**
     * 获取表达式触发器
     */
    public CronTrigger getCronTrigger(String jobId, String groupName) throws SchedulerException {
        return (CronTrigger) scheduler.getTrigger(getTriggerKey(jobId, groupName));
    }

    /**
     * 校验任务是否存在
     */
    public Boolean checkExists(String jobId) throws SchedulerException {
        return checkExists(jobId, null);
    }

    /**
     * 校验任务是否存在
     */
    public Boolean checkExists(String jobId, String groupName) throws SchedulerException {
        return scheduler.checkExists(getJobKey(jobId, groupName));
    }

    /**
     * 创建定时任务
     * @param jobId 任务id
     * @param cron 表达式
     * @param beanName bean对象名称
     * @param params 多个参数用json格式
     * @throws SchedulerException 异常
     */
    public void createScheduleJob(String jobId, String cron, String beanName, String methodName, String params) throws SchedulerException {
        // 创建定时任务
        createScheduleJob(jobId, null, cron, beanName, methodName, params);
    }

    /**
     * 创建定时任务
     * @param jobId 任务id
     * @param groupName 组名
     * @param cron 表达式
     * @param beanName bean对象名称
     * @param params 多个参数用json格式
     * @throws SchedulerException 异常
     */
    public void createScheduleJob(String jobId, String groupName, String cron, String beanName, String methodName, String params) throws SchedulerException {
        // 构建job信息
        JobDetail jobDetail = JobBuilder.newJob(ScheduleJob.class)
                .withIdentity(getJobKey(jobId, groupName))
                // 持久化存储
                .storeDurably()
                .build();
        // 放入参数，运行时的方法可以获取
        JobData jobData = JobData.builder()
                .jobId(getJobId(jobId))
                .beanName(beanName)
                .methodName(methodName)
                .params(params)
                .build();
        jobDetail.getJobDataMap().put(JOB_PARAM_KEY, jobData);

        //表达式调度构建器
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron)
                .withMisfireHandlingInstructionDoNothing();

        //按新的cronExpression表达式构建一个新的trigger
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(getTriggerKey(jobId, groupName)).withSchedule(scheduleBuilder).build();

        // 执行任务
        scheduler.scheduleJob(jobDetail, trigger);
    }

    /**
     * 创建秒级定时任务
     */
    public void createSecondScheduleJob(String jobId, int seconds, String beanName, String methodName, String params) throws SchedulerException {
        createSecondScheduleJob(jobId, null, seconds, beanName, methodName, params);
    }

    /**
     * 创建秒级定时任务
     * @param jobId 任务id
     * @param groupName 组名
     * @param seconds 每隔多少秒
     * @param beanName bean对象名称
     * @param params 多个参数用json格式
     * @throws SchedulerException 异常
     */
    public void createSecondScheduleJob(String jobId, String groupName, int seconds, String beanName, String methodName, String params) throws SchedulerException {
        // 构建job信息
        JobDetail jobDetail = JobBuilder.newJob(ScheduleJob.class)
                .withIdentity(getJobKey(jobId, groupName))
                // 持久化存储
                .storeDurably()
                .build();
        // 放入参数，运行时的方法可以获取
        JobData jobData = JobData.builder()
                .jobId(getJobId(jobId))
                .beanName(beanName)
                .methodName(methodName)
                .params(params)
                .build();
        jobDetail.getJobDataMap().put(JOB_PARAM_KEY, jobData);

        //按新的cronExpression表达式构建一个新的trigger
        SimpleTrigger trigger = TriggerBuilder.newTrigger().withIdentity(getTriggerKey(jobId, groupName))
                .withSchedule(
                        SimpleScheduleBuilder
                                .simpleSchedule()
                                .withIntervalInSeconds(seconds)
                                .repeatForever())
                .build();

        // 执行任务
        scheduler.scheduleJob(jobDetail, trigger);
    }

    /**
     * 更新定时任务
     */
    public void updateScheduleJob(String jobId, String cron, String beanName, String methodName, String params) throws SchedulerException {
        updateScheduleJob(jobId, null, cron, beanName, methodName, params);
    }

    /**
     * 更新定时任务
     */
    public void updateScheduleJob(String jobId, String groupName, String cron, String beanName, String methodName, String params) throws SchedulerException {
        TriggerKey triggerKey = getTriggerKey(jobId, groupName);

        //表达式调度构建器
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron)
                .withMisfireHandlingInstructionDoNothing();

        CronTrigger cronTrigger = getCronTrigger(jobId, groupName);

        //按新的cronExpression表达式重新构建trigger
        cronTrigger = cronTrigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

        // 参数
        JobData jobData = JobData.builder()
                .jobId(getJobId(jobId))
                .beanName(beanName)
                .methodName(methodName)
                .params(params)
                .build();
        cronTrigger.getJobDataMap().put(JOB_PARAM_KEY, jobData);
        // 重新调度作业
        scheduler.rescheduleJob(triggerKey, cronTrigger);
    }

    /**
     * 更新秒级定时任务
     */
    public void updateSecondScheduleJob(String jobId, int seconds, String beanName, String methodName, String params) throws SchedulerException {
        updateSecondScheduleJob(jobId, null, seconds, beanName, methodName, params);
    }

    /**
     * 更新秒级定时任务
     */
    public void updateSecondScheduleJob(String jobId, String groupName, int seconds, String beanName, String methodName, String params) throws SchedulerException {
        TriggerKey triggerKey = getTriggerKey(jobId, groupName);

        SimpleTrigger simpleTrigger = (SimpleTrigger) scheduler.getTrigger(getTriggerKey(jobId, groupName));


        //按新的cronExpression表达式重新构建trigger
        simpleTrigger = simpleTrigger.getTriggerBuilder().withIdentity(triggerKey)
                .withSchedule(
                        SimpleScheduleBuilder
                                .simpleSchedule()
                                .withIntervalInSeconds(seconds)
                                .repeatForever())
                .build();

        // 参数
        JobData jobData = JobData.builder()
                .jobId(getJobId(jobId))
                .beanName(beanName)
                .methodName(methodName)
                .params(params)
                .build();
        simpleTrigger.getJobDataMap().put(JOB_PARAM_KEY, jobData);
        // 重新调度作业
        scheduler.rescheduleJob(triggerKey, simpleTrigger);
    }

    /**
     * 立即执行任务
     */
    public void run(String jobId, String beanName, String methodName, String params) throws SchedulerException {
        run(jobId, null, beanName, methodName, params);
    }

    /**
     * 立即执行任务
     */
    public void run(String jobId, String groupName, String beanName, String methodName, String params) throws SchedulerException {
        //参数
        JobData jobData = JobData.builder()
                .jobId(getJobId(jobId))
                .beanName(beanName)
                .methodName(methodName)
                .params(params)
                .build();
        JobDataMap dataMap = new JobDataMap();
        dataMap.put(JOB_PARAM_KEY, jobData);

        scheduler.triggerJob(getJobKey(jobId, groupName), dataMap);
    }

    /**
     * 暂停任务
     * @param jobId 任务id
     * @throws SchedulerException 异常
     */
    public void pauseJob(String jobId) throws SchedulerException {
        pauseJob(jobId, null);
    }

    /**
     * 暂停任务
     * @param jobId 任务id
     * @param groupName 组名
     * @throws SchedulerException 异常
     */
    public void pauseJob(String jobId, String groupName) throws SchedulerException {
        scheduler.pauseJob(getJobKey(jobId, groupName));
    }

    /**
     * 批量暂停任务
     * @param groupName job组名
     * @throws SchedulerException 异常
     */
    public void pauseJobs(String groupName) throws SchedulerException {
        GroupMatcher<JobKey> matcher = GroupMatcher.jobGroupEquals(getGroupName(groupName));
        // 批量暂停任务
        scheduler.pauseJobs(matcher);
    }

    /**
     * 恢复任务
     * @param jobId 任务id
     * @throws SchedulerException 异常
     */
    public void resumeJob(String jobId) throws SchedulerException {
        resumeJob(jobId, null);
    }

    /**
     * 恢复任务
     * @param jobId 任务id
     * @param groupName 组名
     * @throws SchedulerException 异常
     */
    public void resumeJob(String jobId, String groupName) throws SchedulerException {
        scheduler.resumeJob(getJobKey(jobId, groupName));
    }

    /**
     * 批量恢复任务
     * @param groupName job组名
     * @throws SchedulerException 异常
     */
    public void resumeJobs(String groupName) throws SchedulerException {
        GroupMatcher<JobKey> matcher = GroupMatcher.jobGroupEquals(getGroupName(groupName));
        // 批量恢复任务
        scheduler.resumeJobs(matcher);
    }

    /**
     * 删除定时任务
     * @param jobId 任务id
     * @throws SchedulerException 异常
     */
    public void deleteJob(String jobId) throws SchedulerException {
        deleteJob(jobId, null);
    }

    /**
     * 删除定时任务
     * @param jobId 任务id
     * @param groupName 组名
     * @throws SchedulerException 异常
     */
    public void deleteJob(String jobId, String groupName) throws SchedulerException {
        scheduler.deleteJob(getJobKey(jobId, groupName));
    }

    /**
     * 批量删除定时任务
     * @param jobIds 任务id列表
     * @throws SchedulerException 异常
     */
    public void deleteJobs(List<String> jobIds) throws SchedulerException {
        scheduler.deleteJobs(getJobKeys(jobIds));
    }

    /**
     * 批量删除定时任务
     * @param jobIds 任务id列表
     * @param groupNames 组名列表
     * @throws SchedulerException 异常
     */
    public void deleteJobs(List<String> jobIds, List<String> groupNames) throws SchedulerException {
        scheduler.deleteJobs(getJobKeys(jobIds, groupNames));
    }

    /**
     * 根据组名删除定时任务
     * @param groupName 组名
     * @throws SchedulerException 异常
     */
    public void deleteJobsByGroupName(String groupName) throws SchedulerException {
        GroupMatcher<JobKey> matcher = GroupMatcher.jobGroupEquals(getGroupName(groupName));
        List<JobKey> jobKeys = new ArrayList<>();
        CollUtil.addAll(jobKeys, scheduler.getJobKeys(matcher));
        scheduler.deleteJobs(jobKeys);
    }
}
