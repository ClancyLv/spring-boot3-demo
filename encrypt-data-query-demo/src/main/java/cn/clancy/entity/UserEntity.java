package cn.clancy.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author ClancyLv
 * @Date 2025/6/24 10:30
 * @Description 实体类--系统用户实体类
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserEntity {
    /**
     * 主键id
     */
    private Long id;

    /**
     * 加密后的用户名
     */
    private String username;

    /**
     * 存储所有N-gram分词的哈希值，并建立全文索引
     * 数据库字段使用TEXT类型，并用空格分隔哈希值
     */
    private String usernameHash;

    /**
     * 加密后的手机号
     */
    private String mobile;

    /**
     * 手机号后四位的哈希值
     */
    private String mobileHash;
}
