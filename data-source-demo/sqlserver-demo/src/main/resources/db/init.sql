-- 首先检查表是否存在，如果存在则删除，这是 SQL Server 的标准做法
IF OBJECT_ID('dbo.sys_user', 'U') IS NOT NULL
BEGIN
DROP TABLE dbo.sys_user;
END
GO

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
CREATE TABLE dbo.sys_user (
    id BIGINT IDENTITY(1,1) NOT NULL,
    username NVARCHAR(32) NOT NULL,
    -- "password" 是 SQL Server 的保留关键字，用方括号括起来。
    [password] NVARCHAR(128) NOT NULL,
    salt NVARCHAR(32) NOT NULL,
    status BIT NOT NULL DEFAULT 0,
    real_name NVARCHAR(20) NULL,
    head_url NVARCHAR(128) NULL,
    email NVARCHAR(255) NULL,
    mobile NVARCHAR(255) NULL,
    position NVARCHAR(16) NULL,
    del_flg BIT NOT NULL DEFAULT 0,
    create_id BIGINT NOT NULL,
    update_id BIGINT NULL,
    create_time DATETIME2(0) NOT NULL DEFAULT SYSDATETIME(),
    update_time DATETIME2(0) NOT NULL DEFAULT SYSDATETIME(),
    remark NVARCHAR(255) NULL,
    -- 定义主键约束
    CONSTRAINT PK_sys_user PRIMARY KEY CLUSTERED (id ASC)
    );
GO

-- ----------------------------
-- 添加表和列的注释 (SQL Server 使用扩展属性来实现)
-- ----------------------------
EXEC sp_addextendedproperty
     @name = N'MS_Description', @value = N'系统--用户表',
     @level0type = N'SCHEMA', @level0name = N'dbo',
     @level1type = N'TABLE', @level1name = N'sys_user';
GO
EXEC sp_addextendedproperty
     @name = N'MS_Description', @value = N'登录名',
     @level0type = N'SCHEMA', @level0name = N'dbo',
     @level1type = N'TABLE', @level1name = N'sys_user',
     @level2type = N'COLUMN', @level2name = N'username';
GO
EXEC sp_addextendedproperty
     @name = N'MS_Description', @value = N'登录密码(用户输入明文 前端计算MD5 后端加盐)',
     @level0type = N'SCHEMA', @level0name = N'dbo',
     @level1type = N'TABLE', @level1name = N'sys_user',
     @level2type = N'COLUMN', @level2name = N'password';
GO
EXEC sp_addextendedproperty
     @name = N'MS_Description', @value = N'盐值',
     @level0type = N'SCHEMA', @level0name = N'dbo',
     @level1type = N'TABLE', @level1name = N'sys_user',
     @level2type = N'COLUMN', @level2name = N'salt';
GO
EXEC sp_addextendedproperty
     @name = N'MS_Description', @value = N'账号状态(0:默认正常,1:禁用)',
     @level0type = N'SCHEMA', @level0name = N'dbo',
     @level1type = N'TABLE', @level1name = N'sys_user',
     @level2type = N'COLUMN', @level2name = N'status';
GO
EXEC sp_addextendedproperty
     @name = N'MS_Description', @value = N'真实姓名',
     @level0type = N'SCHEMA', @level0name = N'dbo',
     @level1type = N'TABLE', @level1name = N'sys_user',
     @level2type = N'COLUMN', @level2name = N'real_name';
GO
EXEC sp_addextendedproperty
     @name = N'MS_Description', @value = N'头像URL minio相对路径',
     @level0type = N'SCHEMA', @level0name = N'dbo',
     @level1type = N'TABLE', @level1name = N'sys_user',
     @level2type = N'COLUMN', @level2name = N'head_url';
GO
EXEC sp_addextendedproperty
     @name = N'MS_Description', @value = N'邮箱',
     @level0type = N'SCHEMA', @level0name = N'dbo',
     @level1type = N'TABLE', @level1name = N'sys_user',
     @level2type = N'COLUMN', @level2name = N'email';
GO
EXEC sp_addextendedproperty
     @name = N'MS_Description', @value = N'手机号',
     @level0type = N'SCHEMA', @level0name = N'dbo',
     @level1type = N'TABLE', @level1name = N'sys_user',
     @level2type = N'COLUMN', @level2name = N'mobile';
GO
EXEC sp_addextendedproperty
     @name = N'MS_Description', @value = N'职位',
     @level0type = N'SCHEMA', @level0name = N'dbo',
     @level1type = N'TABLE', @level1name = N'sys_user',
     @level2type = N'COLUMN', @level2name = N'position';
GO
EXEC sp_addextendedproperty
     @name = N'MS_Description', @value = N'是否删除 0正常 1删除',
     @level0type = N'SCHEMA', @level0name = N'dbo',
     @level1type = N'TABLE', @level1name = N'sys_user',
     @level2type = N'COLUMN', @level2name = N'del_flg';
GO
EXEC sp_addextendedproperty
     @name = N'MS_Description', @value = N'创建人id',
     @level0type = N'SCHEMA', @level0name = N'dbo',
     @level1type = N'TABLE', @level1name = N'sys_user',
     @level2type = N'COLUMN', @level2name = N'create_id';
GO
EXEC sp_addextendedproperty
     @name = N'MS_Description', @value = N'更新人id',
     @level0type = N'SCHEMA', @level0name = N'dbo',
     @level1type = N'TABLE', @level1name = N'sys_user',
     @level2type = N'COLUMN', @level2name = N'update_id';
GO
EXEC sp_addextendedproperty
     @name = N'MS_Description', @value = N'创建时间',
     @level0type = N'SCHEMA', @level0name = N'dbo',
     @level1type = N'TABLE', @level1name = N'sys_user',
     @level2type = N'COLUMN', @level2name = N'create_time';
GO
EXEC sp_addextendedproperty
     @name = N'MS_Description', @value = N'更新时间',
     @level0type = N'SCHEMA', @level0name = N'dbo',
     @level1type = N'TABLE', @level1name = N'sys_user',
     @level2type = N'COLUMN', @level2name = N'update_time';
GO
EXEC sp_addextendedproperty
     @name = N'MS_Description', @value = N'备注',
     @level0type = N'SCHEMA', @level0name = N'dbo',
     @level1type = N'TABLE', @level1name = N'sys_user',
     @level2type = N'COLUMN', @level2name = N'remark';
GO