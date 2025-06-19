package cn.clancy.retry.service;

/**
 * @Author ClancyLv
 * @Date 2025/6/19 11:56
 * @Description 服务层--重试
 */
public interface RetryService {
    /**
     * 默认重试方法
     * @param num 数字
     * @return 响应结果
     */
    boolean defaultRetryMethod(int num);

    /**
     * 自定义重试方法
     * @param str 字符串参数
     * @return 响应结果
     */
    boolean customRetryMethod(String str);
}
