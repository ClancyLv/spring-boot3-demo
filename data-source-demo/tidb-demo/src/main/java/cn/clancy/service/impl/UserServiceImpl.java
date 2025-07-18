package cn.clancy.service.impl;

import cn.clancy.entity.UserEntity;
import cn.clancy.mapper.UserMapper;
import cn.clancy.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author ClancyLv
 * @Date 2025/7/17 15:26
 * @Description 接口实现层--用户
 */
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;

    @Override
    public List<UserEntity> queryAllList() {
        return userMapper.selectList(new LambdaQueryWrapper<>());
    }

    @Override
    public List<UserEntity> queryByUsername(String username) {
        return userMapper.selectList(new LambdaQueryWrapper<UserEntity>().like(username != null, UserEntity::getUsername, username));
    }

    @Override
    public boolean saveUser(UserEntity userEntity) {
        // 验证用户实体类的合法性
        validateUser(userEntity);
        // 设置默认值
        userEntity.setCreateId(1L);
        return userMapper.insert(userEntity) > 0;
    }

    @Override
    public boolean updateUser(UserEntity userEntity) {
        // 验证用户实体类的合法性
        validateUser(userEntity);
        if (userEntity.getId() == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        // 设置默认值
        userEntity.setUpdateId(1L);
        return userMapper.updateById(userEntity) > 0;
    }

    @Override
    public boolean deleteUser(Long userId) {
        return userMapper.deleteById(userId) > 0;
    }

    /**
     * 验证用户实体类的合法性
     * @param userEntity 用户实体类
     */
    private void validateUser(UserEntity userEntity) {
        if (userEntity == null) {
            throw new IllegalArgumentException("用户不能为空");
        }
        if (userEntity.getUsername() == null || userEntity.getUsername().isEmpty()) {
            throw new IllegalArgumentException("用户名不能为空");
        }
        if (userEntity.getPassword() == null || userEntity.getPassword().isEmpty()) {
            throw new IllegalArgumentException("密码不能为空");
        }
        if (userEntity.getSalt() == null || userEntity.getSalt().isEmpty()) {
            throw new IllegalArgumentException("密码盐值不能为空");
        }
    }
}
