package cn.clancy.manager;

import cn.clancy.QuartzDemoApplicationTests;
import cn.clancy.job.TestJob;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

@Import(TestJob.class)
class ScheduleManagerTest extends QuartzDemoApplicationTests {

    @Autowired
    private ScheduleManager scheduleManager;

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private TestJob testJob;

    private final String jobId = "testJobId";
    private final String groupName = "testGroup";
    private final String beanName = "testJob";
    private final String methodName = "execute";
    private final String params = "testParams";
    private final String cron = "0/2 * * * * ?";

    @BeforeEach
    void setUp() throws SchedulerException {
        testJob.reset();
        // 在每次测试之前清理
        if (scheduleManager.checkExists(jobId, groupName)) {
            scheduleManager.deleteJob(jobId, groupName);
        }
    }

    @AfterEach
    void tearDown() throws SchedulerException {
        // 测试之后清理
        if (scheduleManager.checkExists(jobId, groupName)) {
            scheduleManager.deleteJob(jobId, groupName);
        }
    }

    @Test
    void testCreateAndCheckExistsScheduleJob() throws SchedulerException {
        assertFalse(scheduleManager.checkExists(jobId, groupName));
        scheduleManager.createScheduleJob(jobId, groupName, cron, beanName, methodName, params);
        assertTrue(scheduleManager.checkExists(jobId, groupName));

        // 等待定时任务执行
        await().atMost(5, SECONDS).until(testJob::hasExecuted);
        assertEquals(params, testJob.getLastParams());
    }

    @Test
    void testCreateSecondScheduleJob() throws SchedulerException {
        int intervalInSeconds = 1;
        assertFalse(scheduleManager.checkExists(jobId, groupName));
        scheduleManager.createSecondScheduleJob(jobId, groupName, intervalInSeconds, beanName, methodName, params);
        assertTrue(scheduleManager.checkExists(jobId, groupName));

        // 等待定时任务执行
        await().atMost(3, SECONDS).until(testJob::hasExecuted);
        assertEquals(params, testJob.getLastParams());
    }

    @Test
    void testUpdateScheduleJob() throws SchedulerException {
        scheduleManager.createScheduleJob(jobId, groupName, "0/10 * * * * ?", beanName, methodName, params);

        String newCron = "0/2 * * * * ?";
        String newParams = "newParams";
        scheduleManager.updateScheduleJob(jobId, groupName, newCron, beanName, methodName, newParams);

        assertEquals(newCron, scheduleManager.getCronTrigger(jobId, groupName).getCronExpression());

        // 等待定时任务执行
        await().atMost(5, SECONDS).until(testJob::hasExecuted);
        assertEquals(newParams, testJob.getLastParams());
    }

    @Test
    void testPauseAndResumeJob() throws SchedulerException, InterruptedException {
        scheduleManager.createScheduleJob(jobId, groupName, cron, beanName, methodName, params);
        scheduleManager.pauseJob(jobId, groupName);

        Trigger.TriggerState triggerState = scheduler.getTriggerState(scheduleManager.getTriggerKey(jobId, groupName));
        assertEquals(Trigger.TriggerState.PAUSED, triggerState);

        // 等待任务暂停
        Thread.sleep(3000);
        assertFalse(testJob.hasExecuted());

        scheduleManager.resumeJob(jobId, groupName);
        triggerState = scheduler.getTriggerState(scheduleManager.getTriggerKey(jobId, groupName));
        assertEquals(Trigger.TriggerState.NORMAL, triggerState);

        // 等待任务恢复执行
        await().atMost(5, SECONDS).until(testJob::hasExecuted);
        assertEquals(params, testJob.getLastParams());
    }

    @Test
    void testRunJob() throws SchedulerException {
         // 定义了一个不会执行的定时任务
         scheduleManager.createScheduleJob(jobId, groupName, "0 0 0 1 1 ? 2099", beanName, methodName, params);
         assertFalse(testJob.hasExecuted());

         String runNowParams = "runNowParams";
         scheduleManager.run(jobId, groupName, beanName, methodName, runNowParams);

         // 等待任务执行
         await().atMost(3, SECONDS).until(testJob::hasExecuted);
         assertEquals(runNowParams, testJob.getLastParams());
    }

    @Test
    void testDeleteJob() throws SchedulerException {
        scheduleManager.createScheduleJob(jobId, groupName, cron, beanName, methodName, params);
        assertTrue(scheduleManager.checkExists(jobId, groupName));
        scheduleManager.deleteJob(jobId, groupName);
        assertFalse(scheduleManager.checkExists(jobId, groupName));
    }

    @Test
    void testUpdateSecondScheduleJob() throws SchedulerException {
        scheduleManager.createSecondScheduleJob(jobId, groupName, 10, beanName, methodName, params);

        int newInterval = 2;
        String newParams = "newParamsForSecondJob";
        scheduleManager.updateSecondScheduleJob(jobId, groupName, newInterval, beanName, methodName, newParams);

        // 等待任务执行
        await().atMost(5, SECONDS).until(testJob::hasExecuted);
        assertEquals(newParams, testJob.getLastParams());
    }

    @Test
    void testBatchJobs() throws SchedulerException {
        String jobId1 = "batchJob1";
        String jobId2 = "batchJob2";
        String group = "batchGroup";

        scheduleManager.createScheduleJob(jobId1, group, cron, beanName, methodName, "p1");
        scheduleManager.createScheduleJob(jobId2, group, cron, beanName, methodName, "p2");

        assertTrue(scheduleManager.checkExists(jobId1, group));
        assertTrue(scheduleManager.checkExists(jobId2, group));

        // 暂停所有任务
        scheduleManager.pauseJobs(group);
        assertEquals(Trigger.TriggerState.PAUSED, scheduler.getTriggerState(scheduleManager.getTriggerKey(jobId1, group)));
        assertEquals(Trigger.TriggerState.PAUSED, scheduler.getTriggerState(scheduleManager.getTriggerKey(jobId2, group)));

        // 批量恢复任务
        scheduleManager.resumeJobs(group);
        assertEquals(Trigger.TriggerState.NORMAL, scheduler.getTriggerState(scheduleManager.getTriggerKey(jobId1, group)));
        assertEquals(Trigger.TriggerState.NORMAL, scheduler.getTriggerState(scheduleManager.getTriggerKey(jobId2, group)));

        // 根据组名删除定时任务
        scheduleManager.deleteJobsByGroupName(group);
        assertFalse(scheduleManager.checkExists(jobId1, group));
        assertFalse(scheduleManager.checkExists(jobId2, group));
    }

    @Test
    void testDeleteMultipleJobs() throws SchedulerException {
        String jobId1 = "deleteJob1";
        String jobId2 = "deleteJob2";
        String group1 = "deleteGroup1";
        String group2 = "deleteGroup2";

        scheduleManager.createScheduleJob(jobId1, group1, cron, beanName, methodName, params);
        scheduleManager.createScheduleJob(jobId2, group2, cron, beanName, methodName, params);

        assertTrue(scheduleManager.checkExists(jobId1, group1));
        assertTrue(scheduleManager.checkExists(jobId2, group2));

        scheduleManager.deleteJobs(java.util.Arrays.asList(jobId1, jobId2), java.util.Arrays.asList(group1, group2));

        assertFalse(scheduleManager.checkExists(jobId1, group1));
        assertFalse(scheduleManager.checkExists(jobId2, group2));
    }

    @Test
    void testGetters() {
        assertEquals("TASK_testJobId", scheduleManager.getJobId(jobId));
        assertEquals("GROUP_testGroup", scheduleManager.getGroupName(groupName));
        assertEquals(scheduleManager.getTriggerKey(jobId, groupName).getName(), scheduleManager.getJobId(jobId));
        assertEquals(scheduleManager.getTriggerKey(jobId, groupName).getGroup(), scheduleManager.getGroupName(groupName));
        assertEquals(scheduleManager.getJobKey(jobId, groupName).getName(), scheduleManager.getJobId(jobId));
        assertEquals(scheduleManager.getJobKey(jobId, groupName).getGroup(), scheduleManager.getGroupName(groupName));
    }
}
