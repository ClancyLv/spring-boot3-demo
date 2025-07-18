package cn.clancy.service.impl;

import cn.clancy.entity.UserEntity;
import cn.clancy.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @Author ClancyLv
 * @Date 2025/7/17 16:20
 * @Description UserServiceImpl的单元测试
 */
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserMapper userMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testQueryAllList() {
        when(userMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(Collections.singletonList(new UserEntity()));
        List<UserEntity> userEntities = userService.queryAllList();
        assertFalse(userEntities.isEmpty());
    }

    @Test
    public void testQueryByUsername() {
        when(userMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(Collections.singletonList(new UserEntity()));
        List<UserEntity> userEntities = userService.queryByUsername("test");
        assertFalse(userEntities.isEmpty());
    }

    @Test
    public void testSaveUser() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("test");
        userEntity.setPassword("password");
        userEntity.setSalt("salt");
        when(userMapper.insert(any(UserEntity.class))).thenReturn(1);
        boolean result = userService.saveUser(userEntity);
        assertTrue(result);
    }

    @Test
    public void testSaveUserWithNullUser() {
        assertThrows(IllegalArgumentException.class, () -> userService.saveUser(null));
    }

    @Test
    public void testSaveUserWithEmptyUsername() {
        UserEntity userEntity = new UserEntity();
        userEntity.setPassword("password");
        userEntity.setSalt("salt");
        assertThrows(IllegalArgumentException.class, () -> userService.saveUser(userEntity));
    }

    @Test
    public void testUpdateUser() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUsername("test");
        userEntity.setPassword("password1");
        userEntity.setSalt("salt");
        when(userMapper.updateById(any(UserEntity.class))).thenReturn(1);
        boolean result = userService.updateUser(userEntity);
        assertTrue(result);
    }

    @Test
    public void testUpdateUserWithNullId() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("test");
        userEntity.setPassword("password");
        userEntity.setSalt("salt");
        assertThrows(IllegalArgumentException.class, () -> userService.updateUser(userEntity));
    }

    @Test
    public void testDeleteUser() {
        when(userMapper.deleteById(any(Long.class))).thenReturn(1);
        boolean result = userService.deleteUser(1L);
        assertTrue(result);
    }
}
