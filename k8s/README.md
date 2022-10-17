
首选创建命名空间(这一步不是必选的，也可以将eazybuilder部署在其他命名空间)
```shell
kubectl apply -f namespace.yaml
```


# 1.安装mysql

```shell
kubectl apply -f mysql.yaml -n es
```

 mysql容器启动后，进入容器，执行如下命令，做初始化设置
 
 ```shell
 mysql -u root -p
 
 alter user 'root'@'%' identified by 'mysql_123' password expire never;
 alter user 'root'@'%' identified with mysql_native_password BY 'mysql_123';
 flush privileges;
 ```
 
# 2.创建数据库
```shell
create database ci;
```

## 注意，这一步，可以采用初始化sql完成，直接执行[initsql](../initsql)目录下的sql，可以将eazybuilder与nacos的数据，一次性全部导入

# 3.安装nacos
```shell
kubectl apply -f nacos-quick-start.yaml -n es
```

# 4.导入nacos日志文件

在nacos中创建命名空间 devops,导入eazybuilder-config目录下面的配置文件

# 5.安装redis
```shell
kubectl apply -f redis.yaml -n es
```
# 6.安装rabbitmq
```shell
kubectl apply -f rbac.yaml -n es
kubectl apply -f rabbitmq.yaml -n es
```
rabbitmq启动成功后，将rabbitmq绑定服务器端口，登录rabbitmq控制台，创建用户
```shell
kubectl apply -f rabbitmq-network -n es
```

创建管理员用户/密码：devops/devops

# 7.安装eazybuilder前后端
```shell
kubectl apply -f eazybuilder-server.yaml
kubectl apply -f eazybuilder-web.yaml
```
# 8.创建eazybuilder ingress
```shell
kubectl apply -f eazybuilder-ingress.yaml -n es
```
访问地址：http://es-web.eazybuilder.com/eazybuilder-web/login.html

注意：es-web.eazybuilder.com是k8s的集群假域名，本地访问，需要配置host映射到集群中的服务器中

