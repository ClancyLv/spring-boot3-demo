package cn.clancy.retry.service.impl;

import cn.clancy.retry.service.RetryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

/**
 * @Author ClancyLv
 * @Date 2025/6/19 11:57
 * @Description 服务实现层--测试
 */
@Slf4j
@Service
public class RetryServiceImpl implements RetryService {
    /**
     * 初始重试次数
     */
    private static int ATTEMPTS = 1;

    /**
     * 注解@Retryable标注需要重试的方法<br>
     * retryFor：默认重试所有异常<br>
     * maxAttempts：默认最大重试次数3次<br>
     * backoff：重试回退策略：默认重试间隔1000毫秒
     */
    @Retryable
    @Override
    public boolean defaultRetryMethod(int num) {
        log.info("defaultRetryMethod第{}次执行，参数：{}", ATTEMPTS++, num);
        if (num < 0) {
            throw new IllegalArgumentException("参数num小于0" );
        }
        log.info("defaultRetryMethod执行成功！！！");
        return true;
    }

    /**
     * <b>默认方法匹配原则：异常类型匹配->参数匹配</b><br>
     * <b>处理多个异常情况：方法重载实现</b>
     */
    @Recover
    public boolean defaultRetryMethodExceptionRecover(Exception e, int num) {
        log.info("defaultRetryMethod重试失败，调用recover方法，Exception异常：{}，参数：{}！！！", e.getMessage(), num);
        return false;
    }

    /**
     * 这里使用到了更具体的异常类型IllegalArgumentException，优先调用
     */
    @Recover
    public boolean defaultRetryMethodIllegalArgumentExceptionRecover(IllegalArgumentException e, int num) {
        log.info("defaultRetryMethod重试失败，调用recover方法，IllegalArgumentException异常：{}，参数：{}！！！", e.getMessage(), num);
        return false;
    }

    /**
     * retryFor：指定重试的异常类型，这里是IllegalStateException和NullPointerException<br>
     * maxAttempts：指定最大重试次数为4次<br>
     * backoff：指定重试的初始间隔为2秒，增长倍数为2。即每次重试的间隔分别为2000ms, 4000ms, 8000ms<br>
     */
    @Retryable(
        retryFor = {IllegalStateException.class, NullPointerException.class},
        maxAttempts = 4,
        backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    @Override
    public boolean customRetryMethod(String str) {
        log.info("customRetryMethod第{}次执行，参数：{}", ATTEMPTS++, str);
        if (str == null || str.isEmpty()) {
            throw new NullPointerException("参数str为空");
        }
        log.info("customRetryMethod执行成功！！！");
        return true;
    }

    /**
     * <b>默认方法匹配原则：异常类型匹配->参数匹配</b><br>
     * <b>处理多个异常情况：方法重载实现</b>
     */
    @Recover
    public boolean customRetryMethodRecover(NullPointerException e, String str) {
        log.info("customRetryMethod重试失败，调用recover方法，NullPointerException异常：{}，参数：{}", e.getMessage(), str);
        return false;
    }
}
