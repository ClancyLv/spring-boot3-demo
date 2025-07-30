package cn.clancy.redis.service.impl;

import cn.clancy.redis.SpringRedisDemoApplicationTests;
import cn.clancy.redis.service.CacheService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CacheServiceImplTest extends SpringRedisDemoApplicationTests {
    @Autowired
    private CacheService cacheService;

    @Test
    void testSaveAndGet() {
        String saved = cacheService.save(1L, "data1");
        Assertions.assertEquals("data1", saved);
        String data = cacheService.get(1L);
        Assertions.assertEquals("data1", data);
    }

    @Test
    void testUpdate() {
        cacheService.save(2L, "data2");
        String updated = cacheService.update(2L, "data2-updated");
        Assertions.assertEquals("data2-updated", updated);
        String data = cacheService.get(2L);
        Assertions.assertEquals("data2-updated", data);
    }

    @Test
    void testDelete() {
        cacheService.save(3L, "data3");
        cacheService.delete(3L);
        String data = cacheService.get(3L);
        Assertions.assertNull(data);
    }

    @Test
    void testGetNotExist() {
        String data = cacheService.get(999L);
        Assertions.assertNull(data);
    }
}
