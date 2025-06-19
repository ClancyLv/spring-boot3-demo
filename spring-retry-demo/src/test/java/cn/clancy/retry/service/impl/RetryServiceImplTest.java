package cn.clancy.retry.service.impl;

import cn.clancy.retry.SpringRetryApplicationTests;
import cn.clancy.retry.service.RetryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class RetryServiceImplTest extends SpringRetryApplicationTests {
    @Autowired
    private RetryService retryService;

    @BeforeEach
    void setUp() {
        // 可选：重置静态变量等
    }

    @Test
    void testDefaultRetryMethodSuccess() {
        boolean result = retryService.defaultRetryMethod(1);
        Assertions.assertTrue(result);
    }

    @Test
    void testDefaultRetryMethodFailAndRecover() {
        boolean result = retryService.defaultRetryMethod(-1);
        Assertions.assertFalse(result);
    }

    @Test
    void testCustomRetryMethodSuccess() {
        boolean result = retryService.customRetryMethod("hello");
        Assertions.assertTrue(result);
    }

    @Test
    void testCustomRetryMethodFailAndRecover() {
        boolean result = retryService.customRetryMethod("");
        Assertions.assertFalse(result);
    }

    @Test
    void testCustomRetryMethodNullAndRecover() {
        boolean result = retryService.customRetryMethod(null);
        Assertions.assertFalse(result);
    }
}
