package cn.clancy.redis.service.impl;

import cn.clancy.redis.service.CacheService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author ClancyLv
 * @Date 2025/6/20 11:45
 * @Description 服务实现层--缓存
 */
@Service
public class CacheServiceImpl implements CacheService {
    /**
     * 模拟数据库缓存数据，使用ConcurrentHashMap以支持并发操作
     */
    private final Map<Long, String> DATA_MAP = new ConcurrentHashMap<>();

    /**
     * 注解@Cacheable：用于缓存方法的返回值<br>
     * value：缓存的名称，可指定多个，查找时按顺序进行缓存命中<br>
     * cacheNames：缓存的名称，同value属性，与value二选一<br>
     * key：缓存的键，默认使用方法参数作为键<br>
     *      可以使用SpEL表达式自定义键，例如 #id表示使用方法参数id作为键，#root.methodName表示方法名称<br>
     * keyGenerator：key的生成器，同key属性，与value二选一，默认使用SimpleKeyGenerator，可自定义实现org.springframework.cache.interceptor.KeyGenerator<br>
     * condition：缓存条件，只有满足条件时才会缓存结果<br>
     * unless：缓存结果的条件，只有不满足条件时才会缓存结果<br>
     * sync：是否同步缓存，默认false，表示异步缓存<br>
     */
    @Cacheable(value = "cacheData", key = "#id")
    @Override
    public String get(Long id) {
        return DATA_MAP.get(id);
    }

    /**
     * 注解@CachePut：将方法结果用于更新缓存<br>
     * 属性与@Cacheable类似，但@CachePut会在方法执行后更新缓存，而不是在方法调用前检查缓存<br>
     */
    @CachePut(value = "cacheData", key = "#id")
    @Override
    public String save(Long id, String data) {
        DATA_MAP.put(id, data);
        return data;
    }

    @CachePut(value = "cacheData", key = "#id")
    @Override
    public String update(Long id, String data) {
        DATA_MAP.put(id, data);
        return data;
    }

    /**
     * 注解@CacheEvict：用于删除缓存<br>
     * 属性与@Cacheable类似，但@CacheEvict会在方法调用后删除缓存<br>
     * allEntries：是否删除所有缓存，默认false，表示只删除指定键的缓存<br>
     * beforeInvocation：是否在方法调用前删除缓存，默认false，表示在方法调用后删除缓存<br>
     */
    @CacheEvict(value = "cacheData", key = "#id")
    @Override
    public void delete(Long id) {
        DATA_MAP.remove(id);
    }
}
