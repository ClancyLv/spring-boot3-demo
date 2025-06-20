# spring-redis-demo

本模块基于 Spring Boot 3 和 Spring Data Redis，演示如何在 Spring 应用中集成和使用 Redis，重点介绍 RedisTemplate 的配置使用以及 Spring Cache 注解，适用于缓存等场景。

# 教程

## 1、添加依赖

```xml
<dependencies>
    <!--redis缓存-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>
    <!--Jackson 用于Redis对象序列化-->
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
        <version>2.19.0</version>
    </dependency>
</dependencies>
```

## 2、核心配置

### 2.1 Redis 连接配置

在 `application.yml` 中配置 Redis 连接信息：

```yaml
spring:
  redis:
    host: localhost   # Redis服务器地址
    port: 6379        # Redis服务器连接端口
    password:         # Redis服务器连接密码（默认为空）
    database: 0       # Redis数据库索引（默认为0）
```

### 2.2 RedisTemplate 配置

在 `RedisConfig.java` 中配置 RedisTemplate 和 RedisCacheManager：

```java
// 启用Spring的缓存注解支持，允许使用@Cacheable等注解进行缓存操作
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
        // 创建默认的缓存配置，设置value序列化方式为JSON
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer()));
        return RedisCacheManager.builder(factory)
                .cacheDefaults(config)
                .build();
    }
}
```

## 3、Redis 操作

### 3.1 使用 RedisTemplate

RedisTemplate 是 Spring Data Redis 提供的核心操作类，支持多种数据类型操作：

```java
@Autowired
private RedisTemplate<String, Object> redisTemplate;

// String类型操作
redisTemplate.opsForValue().set("key", "value", 1, TimeUnit.HOURS);
Object value = redisTemplate.opsForValue().get("key");

// Hash类型操作
redisTemplate.opsForHash().put("hashKey", "field", "value");
Object field = redisTemplate.opsForHash().get("hashKey", "field");

// List类型操作
redisTemplate.opsForList().leftPush("listKey", "value");
String item = (String) redisTemplate.opsForList().leftPop("listKey");

// Set类型操作
redisTemplate.opsForSet().add("setKey", "value");
Set<Object> members = redisTemplate.opsForSet().members("setKey");
```

### 3.2 Spring Cache 注解

Spring Cache 提供了声明式的缓存注解，通过 @EnableCaching 开启支持：

#### 3.2.1 @Cacheable
用于查询方法，在执行方法前先查询缓存，如果缓存中存在，则直接返回缓存数据；否则执行方法并将结果存入缓存。

```java
// 最简单的使用方式
@Cacheable("users")
public User getUser(Long id) { ... }

// 使用SpEL指定key
@Cacheable(value = "users", key = "#id")
public User getUser(Long id) { ... }

// 指定多个缓存名称
@Cacheable(value = {"users", "userCache"})
public User getUser(Long id) { ... }

// 设置条件，满足条件才缓存
@Cacheable(value = "users", condition = "#id > 0")
public User getUser(Long id) { ... }

// 除非结果为null，否则都缓存
@Cacheable(value = "users", unless = "#result == null")
public User getUser(Long id) { ... }

// 自定义key生成
@Cacheable(value = "users", key = "#p0 + '_' + #p1")
public User getUser(Long id, String name) { ... }
```

#### 3.2.2 @CachePut
用于更新方法，每次都会执行方法，并将结果更新到缓存。

```java
// 更新缓存，key为用户ID
@CachePut(value = "users", key = "#user.id")
public User updateUser(User user) { ... }

// 更新多个缓存
@CachePut(value = {"users", "userCache"}, key = "#user.id")
public User updateUser(User user) { ... }

// 条件更新
@CachePut(value = "users", condition = "#user.age > 18")
public User updateUser(User user) { ... }
```

#### 3.2.3 @CacheEvict
用于删除方法，可以删除一个或多个缓存数据。

```java
// 删除单个缓存
@CacheEvict(value = "users", key = "#id")
public void deleteUser(Long id) { ... }

// 删除所有缓存
@CacheEvict(value = "users", allEntries = true)
public void clearUserCache() { ... }

// 方法执行前删除缓存
@CacheEvict(value = "users", beforeInvocation = true)
public void deleteUser(Long id) { ... }

// 删除多个缓存空间的数据
@CacheEvict(value = {"users", "userCache"}, key = "#id")
public void deleteUser(Long id) { ... }
```

#### 3.2.4 @Caching
组合多个缓存操作，适用于复杂的缓存规则。

```java
@Caching(
    cacheable = {
        @Cacheable(value = "users", key = "#username")
    },
    put = {
        @CachePut(value = "users", key = "#result.id"),
        @CachePut(value = "users", key = "#result.email")
    },
    evict = {
        @CacheEvict(value = "userCache", allEntries = true)
    }
)
public User updateUser(String username) { ... }
```

#### 3.2.5 常用的 SpEL 表达式

缓存注解支持 Spring Expression Language (SpEL)，常用的表达式：

- `#参数名`：访问方法参数
- `#p0`、`#p1`：访问第一个、第二个参数
- `#root.method`：当前方法名
- `#root.target`：当前对象
- `#result`：方法结果（不能用在 @Cacheable）
- `#root.caches[0].name`：当前缓存名称

#### 3.2.6 使用建议

1. 缓存粒度：
   - 避免缓存太多数据
   - 设置合适的缓存过期时间
   - 根据业务场景选择缓存策略

2. 缓存key设计：
   - 建议使用 key 前缀区分业务
   - 使用多个参数时注意 key 的唯一性
   - 考虑 key 的可读性和长度

3. 注意事项：
   - @Cacheable 方法应该是幂等的
   - 避免缓存敏感数据
   - 合理使用 condition 和 unless
   - 注意缓存一致性问题

示例代码：
```java
@Service
public class UserServiceImpl implements UserService {
    
    // 使用缓存空间users，key为参数id
    @Cacheable(value = "users", key = "#id", 
              condition = "#id != null", 
              unless = "#result == null")
    public User getUser(Long id) {
        return userMapper.selectById(id);
    }
    
    // 更新数据后更新缓存
    @CachePut(value = "users", key = "#user.id")
    public User updateUser(User user) {
        userMapper.updateById(user);
        return user;
    }
    
    // 删除数据同时删除缓存
    @CacheEvict(value = {"users", "userCache"}, 
                key = "#id",
                beforeInvocation = true)
    public void deleteUser(Long id) {
        userMapper.deleteById(id);
    }
    
    // 组合多个缓存操作
    @Caching(
        evict = {
            @CacheEvict(value = "users", key = "#user.id"),
            @CacheEvict(value = "userCache", key = "#user.email")
        },
        put = {
            @CachePut(value = "userLatest", key = "#user.id")
        }
    )
    public User complexOperation(User user) {
        // 复杂业务处理
        return user;
    }
}
```

## 4、示例代码

本项目提供了两个服务类演示以上功能：
- `RedisService`: 演示 RedisTemplate 的基础使用方法
- `CacheService`: 演示 Spring Cache 注解的使用方式

## 5、测试用例

- `RedisServiceImplTest.java`: RedisTemplate 操作测试
- `CacheServiceImplTest.java`: 缓存注解功能测试

## 6、参考文档

- [Spring Data Redis](https://docs.spring.io/spring-data/redis/docs/current/reference/html/#redis:template)
- [Spring Cache 注解说明](https://docs.spring.io/spring-framework/reference/integration/cache.html)
