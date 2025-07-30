package cn.clancy.redis.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @Author ClancyLv
 * @Date 2025/6/19 17:46
 * @Description 配置类--Redis缓存配置
 * 注解@EnableCaching: 启用Spring的缓存注解支持，允许使用@Cacheable等注解进行缓存操作
 */
@EnableCaching
@Configuration
public class RedisConfig {
    /**
     * 配置RedisTemplate，key使用String序列化，value使用JSON序列化，支持对象存储
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        // key采用字符串序列化
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // value采用JSON序列化，方便对象存储与读取
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        // hash的key也采用字符串序列化
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        // hash的value采用JSON序列化
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     * 配置RedisCacheManager，使用默认的缓存配置
     * 设置value序列化方式为JSON，便于对象的存储与读取
     */
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory factory) {
        // 创建默认的缓存配置，设置value序列化方式为JSON，便于对象的存储与读取
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
        // 构建RedisCacheManager，应用上述配置
        return RedisCacheManager.builder(factory)
                .cacheDefaults(config)
                .build();
    }

}
