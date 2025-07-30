### 安装教程：https://eco.dameng.com/document/dm/zh-cn/start/dm-install-windows.html

### 问题记录

问题：数据库正常启动后，使用disql命令登录数据库报错：[-2501]:用户名或密码错误.
```shell
[dmdba@uos bin]$ ./disql SYSDBA/xxxx@localhost:5237
[-2501]:用户名或密码错误.
```

原因：这里提示很明显了，就是用户名或者是密码不对。我这里是因为密码中含有特殊符号`@`，导致登录失败，报错时候根据提示就已经知道是密码不对，尝试用单引号、双引号包裹还是不对，后面去社区找类似问题才找到解决方案。

吐槽一个其他问题，这里登录失败后会让你重新输入用户名和密码，按道理这里应该就能正常登录了，因为这里是不需要对特殊符号做处理的，但是没有用，出现报错：[-70028]:创建SOCKET连接失败.

```shell
[dmdba@uos bin]$ ./disql SYSDBA/xxxx@localhost:5237
[-2501]:用户名或密码错误.
disql V8
用户名:SYSDBA
密码:(隐藏式输入密码)
[-70028]:创建SOCKET连接失败.
```

解决：使用disql命令登录数据库时，如果有特殊符号@要用'""'包裹，其他特殊符号处理可参考官方文档：https://eco.dameng.com/document/dm/zh-cn/pm/getting-started-disql
```shell
[dmdba@uos bin]$ ./disql SYSDBA/'"xxxx"'@localhost:5237
```