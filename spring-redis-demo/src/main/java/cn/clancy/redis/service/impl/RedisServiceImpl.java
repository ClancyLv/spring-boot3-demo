package cn.clancy.redis.service.impl;

import cn.clancy.redis.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @Author ClancyLv
 * @Date 2025/6/19 17:47
 * @Description 服务实现层--Redis缓存
 */
@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void setStr(String key, String value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    @Override
    public void setStrIfAbsent(String key, String value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().setIfAbsent(key, value, timeout, unit);
    }

    @Override
    public void setExpire(String key, long timeout, TimeUnit unit) {
        redisTemplate.expire(key, timeout, unit);
    }

    @Override
    public String getStr(String key) {
        String value = redisTemplate.opsForValue().get(key);
        return value == null ? "" : value;
    }

    @Override
    public boolean delete(String key) {
        return redisTemplate.delete(key);
    }
}
