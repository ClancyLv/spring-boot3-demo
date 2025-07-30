package cn.clancy.service;

import cn.clancy.entity.UserEntity;
import cn.clancy.service.impl.PartMatchServiceImpl;
import cn.clancy.utils.EncryptUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PartMatchServiceImplTest {

    private PartMatchService partMatchService;

    @BeforeEach
    void setUp() throws Exception {
        partMatchService = new PartMatchServiceImpl();
        // Clear the mock database before each test
        Field userListField = PartMatchServiceImpl.class.getDeclaredField("USER_LIST");
        userListField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<UserEntity> userList = (List<UserEntity>) userListField.get(null);
        userList.clear();
    }

    @Test
    void testSaveAndQueryMobile() {
        String mobile = "13812345678";
        assertTrue(partMatchService.saveMobile(mobile));

        // Query with correct suffix
        List<UserEntity> result = partMatchService.queryByMobileSuffix("5678");
        assertEquals(1, result.size());
        assertEquals(EncryptUtil.aesEncrypt(mobile), result.get(0).getMobile());

        // Query with incorrect suffix
        List<UserEntity> noResult = partMatchService.queryByMobileSuffix("1111");
        assertTrue(noResult.isEmpty());
    }

    @Test
    void testQueryWithBlankSuffix() {
        partMatchService.saveMobile("13812345678");
        List<UserEntity> result = partMatchService.queryByMobileSuffix("");
        assertEquals(1, result.size()); // Should return all users
    }

    @Test
    void testQueryWithInvalidSuffix() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            partMatchService.queryByMobileSuffix("123");
        });
        assertEquals("手机尾号不是四位", exception.getMessage());
    }

    @Test
    void testSaveWithInvalidMobile() {
        assertThrows(NullPointerException.class, () -> partMatchService.saveMobile(null));
        assertThrows(NullPointerException.class, () -> partMatchService.saveMobile(""));
        assertThrows(NullPointerException.class, () -> partMatchService.saveMobile("123"));
    }

    @Test
    void testQueryWithNoData() {
        List<UserEntity> result = partMatchService.queryByMobileSuffix("1234");
        assertTrue(result.isEmpty());
    }
}
