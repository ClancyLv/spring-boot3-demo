package cn.clancy.service;

import cn.clancy.entity.UserEntity;
import cn.clancy.service.impl.NgramServiceImpl;
import cn.clancy.utils.EncryptUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NgramServiceImplTest {

    private NgramService ngramService;

    @BeforeEach
    void setUp() throws Exception {
        ngramService = new NgramServiceImpl();
        // Clear the mock database before each test
        Field userListField = NgramServiceImpl.class.getDeclaredField("USER_LIST");
        userListField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<UserEntity> userList = (List<UserEntity>) userListField.get(null);
        userList.clear();
    }

    @Test
    void testSaveAndQueryUsername() {
        String username = "ClancyLv";
        assertTrue(ngramService.saveUsername(username));

        // Query with a partial match
        List<UserEntity> result = ngramService.queryByUsername("ncyLv");
        assertEquals(1, result.size());
        assertEquals(EncryptUtil.aesEncrypt(username), result.get(0).getUsername());

        // Query with a non-matching string
        List<UserEntity> noResult = ngramService.queryByUsername("xyz");
        assertTrue(noResult.isEmpty());
    }

    @Test
    void testQueryWithBlankUsername() {
        ngramService.saveUsername("ClancyLv");
        List<UserEntity> result = ngramService.queryByUsername("");
        assertEquals(1, result.size()); // Should return all users
    }

    @Test
    void testQueryWithInvalidUsername() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ngramService.queryByUsername("a");
        });
        assertEquals("用户名长度不能小于两位", exception.getMessage());
    }

    @Test
    void testSaveWithInvalidUsername() {
        assertThrows(NullPointerException.class, () -> ngramService.saveUsername(null));
        assertThrows(NullPointerException.class, () -> ngramService.saveUsername(""));
    }

    @Test
    void testQueryWithMultipleUsers() {
        ngramService.saveUsername("ClancyLv");
        ngramService.saveUsername("SteveJobs");

        List<UserEntity> result = ngramService.queryByUsername("Lv");
        assertEquals(1, result.size());
        assertEquals("ClancyLv", EncryptUtil.aesDecrypt(result.get(0).getUsername()));

        List<UserEntity> result2 = ngramService.queryByUsername("Steve");
        assertEquals(1, result2.size());
        assertEquals("SteveJobs", EncryptUtil.aesDecrypt(result2.get(0).getUsername()));
    }
}
