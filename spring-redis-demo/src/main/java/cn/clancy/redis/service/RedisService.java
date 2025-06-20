package cn.clancy.redis.service;

import java.util.concurrent.TimeUnit;

/**
 * @Author ClancyLv
 * @Date 2025/6/19 17:47
 * @Description 服务层--Redis缓存
 */
public interface RedisService {
    /**
     * 设置字符串类型的键值对，可指定过期时间
     * @param key 键名
     * @param value 值
     * @param timeout 过期时间
     * @param unit 时间单位
     */
    void setStr(String key, String value, long timeout, TimeUnit unit);

    /**
     * 设置字符串类型的键值对，如果键不存在则设置成功，并指定过期时间
     * @param key 键名
     * @param value 值
     * @param timeout 过期时间
     * @param unit 时间单位
     */
    void setStrIfAbsent(String key, String value, long timeout, TimeUnit unit);

    /**
     * 设置键的过期时间
     * @param key 键名
     * @param timeout 过期时间
     * @param unit 时间单位
     */
    void setExpire(String key, long timeout, TimeUnit unit);

    /**
     * 获取字符串类型的值
     * @param key 键名
     * @return 值
     */
    String getStr(String key);

    /**
     * 删除指定键
     * @param key 键名
     * @return 是否删除成功
     */
    boolean delete(String key);
}
