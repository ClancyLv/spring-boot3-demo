package cn.clancy.handler;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @Author ClancyLv
 * @Date 2025/7/14
 * @Description Xxl-Job 示例任务
 */
@Slf4j
@Component
public class SampleJobHandler {

    /**
     * 1、简单任务示例（Bean模式）
     */
    @XxlJob("demoJobHandler")
    public void demoJobHandler() throws Exception {
        XxlJobHelper.log("XXL-JOB, Hello World.");
        log.info("XXL-JOB, Hello World.");
        for (int i = 0; i < 5; i++) {
            XxlJobHelper.log("beat at:" + i);
            log.info("beat at: {}", i);
            TimeUnit.SECONDS.sleep(2);
        }
    }


    /**
     * 2、分片广播任务
     */
    @XxlJob("shardingJobHandler")
    public void shardingJobHandler() throws Exception {
        // 分片参数
        int shardIndex = XxlJobHelper.getShardIndex();
        int shardTotal = XxlJobHelper.getShardTotal();

        XxlJobHelper.log("分片参数：当前分片序号 = {}, 总分片数 = {}", shardIndex, shardTotal);
        log.info("分片参数：当前分片序号 = {}, 总分片数 = {}", shardIndex, shardTotal);

        // 业务逻辑
        for (int i = 0; i < shardTotal; i++) {
            if (i == shardIndex) {
                XxlJobHelper.log("第 {} 片, 命中分片开始处理", i);
                log.info("第 {} 片, 命中分片开始处理", i);
            } else {
                XxlJobHelper.log("第 {} 片, 忽略", i);
                log.info("第 {} 片, 忽略", i);
            }
        }
    }


    /**
     * 3、任务参数示例
     * 任务参数：String
     */
    @XxlJob("paramJobHandler")
    public void paramJobHandler() throws Exception {
        String param = XxlJobHelper.getJobParam();
        XxlJobHelper.log("任务参数:{}", param);
        log.info("任务参数:{}", param);

        // 模拟业务处理
        TimeUnit.SECONDS.sleep(2);
        if (RandomUtil.randomInt(10) > 5) {
            XxlJobHelper.handleFail("业务处理失败");
            return;
        }
        XxlJobHelper.handleSuccess("业务处理成功");
    }

    /**
     * 4、动态参数示例
     * 任务参数：Date
     */
    @XxlJob("dynamicParamJobHandler")
    public void dynamicParamJobHandler() throws Exception {
        String jobParam = XxlJobHelper.getJobParam();
        Date date = DateUtil.parse(jobParam);
        XxlJobHelper.log("动态任务参数:{}", date);
        log.info("动态任务参数:{}", date);
    }
}
