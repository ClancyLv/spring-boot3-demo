package cn.clancy.service;

import cn.clancy.entity.UserEntity;
import java.util.List;

/**
 * @Author ClancyLv
 * @Date 2025/7/17 15:22
 * @Description 接口层--用户
 */
public interface UserService {
    /**
     * 查询所有用户列表
     * @return 用户列表
     */
    List<UserEntity> queryAllList();

    /**
     * 根据用户名查找用户列表
     * @param username 用户名
     * @return 用户列表
     */
    List<UserEntity> queryByUsername(String username);

    /**
     * 保存用户信息
     * @param userEntity 用户实体
     * @return 是否保存成功
     */
    boolean saveUser(UserEntity userEntity);

    /**
     * 更新用户信息
     * @param userEntity 用户实体
     * @return 是否更新成功
     */
    boolean updateUser(UserEntity userEntity);

    /**
     * 删除用户
     * @param userId 用户ID
     * @return 是否删除成功
     */
    boolean deleteUser(Long userId);
}
