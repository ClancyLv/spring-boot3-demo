-- 删除表，如果它已经存在
DROP TABLE IF EXISTS sys_user;

-- 创建 sys_user 表
CREATE TABLE sys_user (
    id int8 PRIMARY KEY,
    username VARCHAR(32) NOT NULL,
    password VARCHAR(128) NOT NULL,
    salt VARCHAR(32) NOT NULL,
    status int2 NOT NULL DEFAULT 0,
    real_name VARCHAR(20),
    head_url VARCHAR(128),
    email VARCHAR(255),
    mobile VARCHAR(255),
    position VARCHAR(16),
    del_flg int2 NOT NULL DEFAULT 0,
    create_id int8 NOT NULL,
    update_id int8,
    create_time TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    remark VARCHAR(255)
);

-- 添加表注释
COMMENT ON TABLE sys_user IS '系统--用户表';
COMMENT ON COLUMN sys_user.id IS '主键id';
COMMENT ON COLUMN sys_user.username IS '登录名';
COMMENT ON COLUMN sys_user.password IS '登录密码(用户输入明文 前端计算MD5 后端加盐)';
COMMENT ON COLUMN sys_user.salt IS '盐值';
COMMENT ON COLUMN sys_user.status IS '账号状态(0:默认正常,1:禁用)';
COMMENT ON COLUMN sys_user.real_name IS '真实姓名';
COMMENT ON COLUMN sys_user.head_url IS '头像URL minio相对路径';
COMMENT ON COLUMN sys_user.email IS '邮箱';
COMMENT ON COLUMN sys_user.mobile IS '手机号';
COMMENT ON COLUMN sys_user.position IS '职位';
COMMENT ON COLUMN sys_user.del_flg IS '是否删除 0正常 1删除';
COMMENT ON COLUMN sys_user.create_id IS '创建人id';
COMMENT ON COLUMN sys_user.update_id IS '更新人id';
COMMENT ON COLUMN sys_user.create_time IS '创建时间';
COMMENT ON COLUMN sys_user.update_time IS '更新时间';
COMMENT ON COLUMN sys_user.remark IS '备注';