package cn.clancy.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author ClancyLv
 * @Date 2025/6/24 10:30
 * @Description 实体类--系统用户实体类
 */
@Data
@TableName("sys_user")
public class UserEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 登录名
     */
    private String username;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 盐值
     */
    private String salt;

    /**
     * 账号状态(0:默认正常,1:禁用)
     */
    private Integer status;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 头像URL minio相对路径
     */
    private String headUrl;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 职位
     */
    private String position;

    /**
     * 是否删除 0正常 1删除
     */
    @TableLogic
    private Integer delFlg;

    /**
     * 创建人id
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createId;

    /**
     * 更新人id
     */
    @TableField(fill = FieldFill.UPDATE)
    private Long updateId;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 备注
     */
    private String remark;
}
