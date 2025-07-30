# mybatis-plus-demo

本模块基于 Spring Boot 3 和 MyBatis-Plus，演示如何在 Spring 应用中集成和使用 MyBatis-Plus，实现优雅的数据库操作，包括基础的 CRUD、自定义查询、逻辑删除等功能。

# 教程

## 1、添加依赖

```xml
<dependencies>
    <!--mybatis-plus核心模块-->
    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>mybatis-plus-spring-boot3-starter</artifactId>
    </dependency>
    <!--sql解析器-->
    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>mybatis-plus-jsqlparser</artifactId>
    </dependency>
    <!--mysql驱动-->
    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
        <version>8.3.0</version>
        <scope>runtime</scope>
    </dependency>
    <!--lombok-->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

## 2、核心配置

### 2.1 启动类进行Mapper扫描

```java
@MapperScan("cn.clancy.mybatisplus.mapper")
@SpringBootApplication
public class MybatisPlusDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(MybatisPlusDemoApplication.class, args);
    }
}
```

### 2.2 数据源配置

在 `application.yml` 中配置数据库连接信息：

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/your_database?useUnicode=true&characterEncoding=UTF-8&useSSL=true&serverTimezone=Asia/Shanghai
    username: root
    password: your_password

mybatis-plus:
  mapper-locations: classpath*:mapper/**.xml
  type-aliases-package: cn.clancy.mybatisplus.entity
  configuration:
    map-underscore-to-camel-case: true
  global-config:
    db-config:
      id-type: auto
      logic-delete-field: del_flg
```

## 3、代码实现

### 3.1 实体类
```java
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
```

### 3.2 Mapper接口
```java
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
```

### 3.3 XML映射文件
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" 
    "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="cn.clancy.mybatisplus.mapper.UserMapper">
    <select id="selectByStatusAndName" resultType="cn.clancy.mybatisplus.entity.UserEntity">
        SELECT
            id, username, real_name, status,
            head_url, email, mobile, position,
            del_flg, create_id, update_id,
            create_time, update_time, remark
        FROM sys_user
        WHERE del_flg = 0
        <if test="status != null">
            AND status = #{status}
        </if>
        <if test="realName != null and realName != ''">
            AND real_name LIKE CONCAT('%', #{realName}, '%')
        </if>
        ORDER BY id ASC
    </select>
</mapper>
```

### 3.4 Service接口及实现
```java
public interface UserService extends IService<UserEntity> {
    List<UserEntity> listByStatusAndName(Integer status, String realName);
}

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {
    @Override
    public List<UserEntity> listByStatusAndName(Integer status, String realName) {
        return baseMapper.selectByStatusAndName(status, realName);
    }
}
```

## 4、功能特性

1. 内置CRUD：继承BaseMapper即可获得常用的数据库操作方法
2. 自动填充：通过注解实现创建时间、更新时间自动填充
3. 逻辑删除：通过@TableLogic注解实现逻辑删除
4. 自定义查询：支持XML方式编写复杂SQL查询
5. 分页查询：内置分页插件，无需手动实现分页逻辑

## 5、单元测试

本模块包含完整的单元测试用例，覆盖以下功能：
- 基础CRUD操作测试
- 自定义查询测试
- 分页查询测试
- 逻辑删除测试

详见 `UserServiceTest.java`。
