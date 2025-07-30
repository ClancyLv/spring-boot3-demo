package cn.clancy.mybatisplus.service;

import cn.clancy.mybatisplus.entity.UserEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Author ClancyLv
 * @Date 2025/6/24 10:32
 * @Description 接口层--用户
 */
public interface UserService extends IService<UserEntity> {
    /**
     * 根据状态和真实姓名查询用户列表
     *
     * @param status 用户状态（可选）
     * @param realName 真实姓名（可选，模糊查询）
     * @return 用户列表
     */
    List<UserEntity> listByStatusAndName(Integer status, String realName);
}
