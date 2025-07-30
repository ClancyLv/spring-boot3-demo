package cn.clancy.redis.service.impl;

import cn.clancy.redis.SpringRedisDemoApplicationTests;
import cn.clancy.redis.service.RedisService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

import java.util.concurrent.TimeUnit;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class RedisServiceImplTest extends SpringRedisDemoApplicationTests {
    @Autowired
    private RedisService redisService;

    @Test
    void testSetStrAndGetStr() {
        redisService.setStr("key", "value", 10, TimeUnit.SECONDS);
        String result = redisService.getStr("key");
        Assertions.assertEquals("value", result);
    }

    @Test
    void testSetStrIfAbsent() {
        redisService.setStrIfAbsent("key", "v1", 10, TimeUnit.SECONDS);
        String result = redisService.getStr("key");
        Assertions.assertEquals("v1", result);
        redisService.setStrIfAbsent("key", "v2", 10, TimeUnit.SECONDS);
        result = redisService.getStr("key");
        Assertions.assertEquals("v1", result);
    }

    @Test
    void testSetExpire() throws InterruptedException {
        redisService.setStr("expireKey", "expireValue", 1, TimeUnit.SECONDS);
        redisService.setExpire("expireKey", 1, TimeUnit.SECONDS);
        Thread.sleep(1200);
        String result = redisService.getStr("expireKey");
        Assertions.assertEquals("", result);
    }

    @Test
    void testGetStrNull() {
        String result = redisService.getStr("not_exist_key");
        Assertions.assertEquals("", result);
    }

    @Test
    void testDelete() {
        redisService.setStr("delKey", "delValue", 10, TimeUnit.SECONDS);
        boolean deleted = redisService.delete("delKey");
        Assertions.assertTrue(deleted);
        String result = redisService.getStr("delKey");
        Assertions.assertEquals("", result);
    }
}
