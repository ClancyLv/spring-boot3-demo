package cn.clancy.mybatisplus.service.impl;

import cn.clancy.mybatisplus.MybatisPlusDemoApplicationTests;
import cn.clancy.mybatisplus.entity.UserEntity;
import cn.clancy.mybatisplus.service.UserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author ClancyLv
 * @Date 2025/6/24 10:36
 * @Description 测试--用户服务
 */
@Transactional
public class UserServiceImplTest extends MybatisPlusDemoApplicationTests {

    @Autowired
    private UserService userService;

    /**
     * 测试--保存用户
     */
    @Test
    @DisplayName("测试保存用户")
    void testSave() {
        UserEntity user = createTestUser();
        assertTrue(userService.save(user));
        assertNotNull(user.getId());
    }

    /**
     * 测试--查询用户
     */
    @Test
    @DisplayName("测试查询用户")
    void testQuery() {
        // 创建测试数据
        UserEntity user = createTestUser();
        userService.save(user);

        // 测试单个查询
        UserEntity foundUser = userService.getById(user.getId());
        assertNotNull(foundUser);
        assertEquals("testUser", foundUser.getUsername());
        assertEquals("测试用户", foundUser.getRealName());
    }

    /**
     * 测试--更新用户
     */
    @Test
    @DisplayName("测试更新用户")
    void testUpdate() {
        // 创建测试数据
        UserEntity user = createTestUser();
        userService.save(user);

        // 更新用户信息
        user.setRealName("更新名称");
        user.setMobile("13900139000");
        user.setPosition("高级测试工程师");
        assertTrue(userService.updateById(user));

        // 验证更新结果
        UserEntity updatedUser = userService.getById(user.getId());
        assertEquals("更新名称", updatedUser.getRealName());
        assertEquals("13900139000", updatedUser.getMobile());
        assertEquals("高级测试工程师", updatedUser.getPosition());
    }

    /**
     * 测试--分页查询
     */
    @Test
    @DisplayName("测试分页查询")
    void testPage() {
        // 创建测试数据
        UserEntity user = createTestUser();
        userService.save(user);

        // 测试分页查询
        IPage<UserEntity> page = userService.page(new Page<>(1, 10));
        assertTrue(page.getTotal() > 0);
        assertFalse(page.getRecords().isEmpty());
    }

    /**
     * 测试--删除用户
     */
    @Test
    @DisplayName("测试删除用户")
    void testDelete() {
        // 创建测试数据
        UserEntity user = createTestUser();
        userService.save(user);

        // 测试删除
        assertTrue(userService.removeById(user.getId()));
        assertNull(userService.getById(user.getId()));
    }

    /**
     * 测试--根据状态和姓名查询
     */
    @Test
    @DisplayName("测试根据状态和姓名查询")
    void testListByStatusAndName() {
        // 创建测试数据
        UserEntity user1 = createTestUser();
        user1.setStatus(0);
        user1.setRealName("张三");
        userService.save(user1);

        UserEntity user2 = createTestUser();
        user2.setStatus(0);
        user2.setRealName("张四");
        user2.setUsername("testUser2");
        userService.save(user2);

        UserEntity user3 = createTestUser();
        user3.setStatus(1);
        user3.setRealName("李四");
        user3.setUsername("testUser3");
        userService.save(user3);

        // 测试仅按状态查询
        List<UserEntity> normalUsers = userService.listByStatusAndName(0, null);
        assertEquals(2, normalUsers.size());
        assertTrue(normalUsers.stream().allMatch(user -> user.getStatus().equals(0)));

        // 测试仅按姓名查询
        List<UserEntity> zhangUsers = userService.listByStatusAndName(null, "张");
        assertEquals(2, zhangUsers.size());
        assertTrue(zhangUsers.stream().allMatch(user -> user.getRealName().contains("张")));

        // 测试状态和姓名组合查询
        List<UserEntity> normalZhangUsers = userService.listByStatusAndName(0, "张");
        assertEquals(2, normalZhangUsers.size());
        assertTrue(normalZhangUsers.stream().allMatch(user ->
            user.getStatus().equals(0) && user.getRealName().contains("张")));

        // 测试查询无结果
        List<UserEntity> emptyList = userService.listByStatusAndName(0, "王");
        assertTrue(emptyList.isEmpty());
    }

    /**
     * 创建测试用户
     */
    private UserEntity createTestUser() {
        UserEntity user = new UserEntity();
        user.setUsername("testUser");
        user.setPassword("password123");
        user.setSalt("randomSalt");
        user.setStatus(0);
        user.setRealName("测试用户");
        user.setHeadUrl("http://example.com/avatar.jpg");
        user.setEmail("test@example.com");
        user.setMobile("13800138000");
        user.setPosition("测试工程师");
        user.setCreateId(1L);
        user.setRemark("测试账号");
        return user;
    }
}
