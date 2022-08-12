# XXL-Job-Admin 镜像

> XXL-Job-Admin 镜像, 可直接部署使用

## 1. 运行命令

``` xml
docker run -d --name xxl-job-admin --restart=always --net="host" \
-e MYSQL_USERNAME=root \
-e MYSQL_PWD=rootAdmin \
javafamily/xxl-job-admin-mysql:2.3.2-SNAPSHOT
```

## 2. 变量信息

| 环境变量         | 描述            |  是否必填      | 默认值           |
| --------------  | -------------- | ------------- | --------------- |
| MYSQL_IP        |   mysql 地址     |       NO      | 127.0.0.1(容器内) |
| MYSQL_PORT      |   mysql 端口   |       NO      |     3306        |
| MYSQL_DBNAME    | mysql 数据库名称 |       NO      |     db_xxl-job  |
| MYSQL_URL_PARAMS |  mysql 连接参数 |       NO      | ?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&serverTimezone=Asia/Shanghai |
| MYSQL_USERNAME  | mysql 用户名 |       YES      |     /          |
| MYSQL_PWD       | mysql password |       YES      |     /          |
| CONTEXT_PATH    |  context path  |        NO      | /xxl-job-admin  |
| PORT            | admin ui 页面访问端口 |        NO      |     8080        |
| DS_INIT_MODE    | 是否自动初始化建表   |        NO      |     never       |

## 3. 访问

```shell
http://ip:port/${CONTEXT_PATH}
```
