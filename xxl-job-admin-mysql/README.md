# XXL-Job-Admin 镜像

> XXL-Job-Admin 镜像, 可直接部署使用

## 1. 运行命令

``` xml
docker run -d --name xxl-job-admin -p 8080:8080 --restart=always \
-e MYSQL_URL=jdbc:mysql://127.0.0.1:3306/db_test?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&serverTimezone=Asia/Shanghai \
-e MYSQL_USERNAME=xxxx \
-e MYSQL_PWD=xxxx \
javafamily/xxl-job-admin-mysql:2.3.2-SNAPSHOT
```

## 2. 变量信息

| 环境变量         | 描述            |  是否必填      | 默认值           |
| --------------  | -------------- | ------------- | --------------- |
| MYSQL_URL       |   jdbc url     |       YES      |     /          |
| MYSQL_USERNAME  | mysql username |       YES      |     /          |
| MYSQL_PWD       | mysql password |       YES      |     /          |
| CONTEXT_PATH    |  context path  |        NO      | /xxl-job-admin  |
| PORT            |     port       |        NO      |     8080        |
| DS_INIT_MODE    | 是否初始化库表   |        NO      |     never       |

## 3. 访问

```shell
http://ip:port/${CONTEXT_PATH}
```
