### Docker命令安装

docker run -e "ACCEPT_EULA=Y" -e "MSSQL_SA_PASSWORD=Your_Password123" -p 1433:1433 --name my_sqlserver --hostname my_sqlserver -d mcr.microsoft.com/mssql/server:2022-latest

上面运行容器命令没有设置数据持久化，需要持久化可以添加命令：-v /sqlserver_data:/var/opt/mssql

### docker-compose文件安装

在`docker-compose.yml`文件下执行命令：docker-compose up -d