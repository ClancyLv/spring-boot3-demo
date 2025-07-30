package cn.clancy.mybatisplus.mapper;

import cn.clancy.mybatisplus.entity.UserEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * @Author ClancyLv
 * @Date 2025/6/24 10:31
 * @Description Mapper层--用户
 */
@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {
    /**
     * 根据状态和真实姓名查询用户列表
     *
     * @param status 用户状态
     * @param realName 真实姓名（模糊查询）
     * @return 用户列表
     */
    List<UserEntity> selectByStatusAndName(@Param("status") Integer status, @Param("realName") String realName);
}
