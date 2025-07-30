package cn.clancy.mapper;

import cn.clancy.entity.UserEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * @Author ClancyLv
 * @Date 2025/7/17 15:27
 * @Description 数据访问层--用户
 */
@Repository
public interface UserMapper extends BaseMapper<UserEntity> {
}
