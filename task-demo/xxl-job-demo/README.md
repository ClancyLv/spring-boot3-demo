# xxl-job 示例

本模块 (`xxl-job-demo`) 是一个 XXL-Job 执行器示例，用于演示如何在 Spring Boot 项目中集成和使用 XXL-Job。

项目中的 `xxl-job-admin` 模块是官方提供的调度中心。要完整地运行和测试定时任务，需要同时启动 `xxl-job-admin`（调度中心）和 `xxl-job-demo`（执行器）。

## 模块结构

- **xxl-job-admin**: 调度中心，负责任务的统一管理、调度和监控。
- **xxl-job-demo**: 执行器，负责接收调度并执行具体的业务任务。

## 如何运行

### 1. 启动调度中心

- **初始化数据库**: 在 `xxl-job-admin` 的 `resources/db` 文件下找到 `tables_xxl_job.sql` 文件并导入到您的 MySQL 数据库中。
- **修改配置**: 在 `xxl-job-admin` 的 `application.yml` 中，配置正确的数据库连接信息（地址、用户名、密码）。
- **启动服务**: 运行 `XxlJobAdminApplication` 的 `main` 方法启动调度中心。
- **访问**: 启动后，可以访问 `http://localhost:8080/xxl-job-admin` 来管理任务。

### 2. 启动执行器

- **修改配置**: 在 `xxl-job-demo` 的 `application.yml` 文件中，确保 `xxl.job.admin.addresses` 指向了正确的调度中心地址（默认为 `http://127.0.0.1:8080/xxl-job-admin`）。
- **启动服务**: 运行 `XxlJobDemoApplication` 的 `main` 方法启动执行器。
- **查看状态**: 启动后，执行器会自动注册到调度中心。您可以在调度中心的 "执行器管理" 页面看到新注册的 `xxl-job-demo-executor`。

### 3. 新建并执行任务

1.  登录调度中心。
2.  在 "任务管理" 页面点击 "新增"。
3.  **执行器**: 选择 `xxl-job-demo-executor`。
4.  **JobHandler**: 填写 `SampleJobHandler` 中定义的 JobHandler 名称，例如 `demoJobHandler`。
5.  配置 Cron 表达式或其他调度选项。
6.  保存任务，并手动触发一次，观察执行器控制台和调度中心日志，验证任务是否成功执行。

## 示例任务 (SampleJobHandler)

- **demoJobHandler**: 简单的 "Hello World" 任务。
- **shardingJobHandler**: 演示分片广播任务。
- **paramJobHandler**: 演示如何使用任务参数。
- **dynamicParamJobHandler**: 演示如何使用动态参数。