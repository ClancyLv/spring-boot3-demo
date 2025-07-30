package cn.clancy.mybatisplus.service.impl;

import cn.clancy.mybatisplus.entity.UserEntity;
import cn.clancy.mybatisplus.mapper.UserMapper;
import cn.clancy.mybatisplus.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author ClancyLv
 * @Date 2025/6/24 10:33
 * @Description 实现层--用户服务
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {

    @Override
    public List<UserEntity> listByStatusAndName(Integer status, String realName) {
        return baseMapper.selectByStatusAndName(status, realName);
    }
}
