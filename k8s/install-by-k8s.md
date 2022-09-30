1.安装mysql

kubectl apply -f mysql.yaml -n es

 mysql容器启动后，进入容器，执行如下命令，做初始化设置
 mysql -u root -p
 
 alter user 'root'@'%' identified by 'mysql_123' password expire never;
 alter user 'root'@'%' identified with mysql_native_password BY 'mysql_123';
flush privileges;
 
2.创建数据库
create database ci;

3.安装nacos
kubectl apply -f nacos-quick-start.yaml -n es

4.导入nacos日志文件
在nacos中创建命名空间 devops

5.安装redis
kubectl apply -f redis.yaml -n es

6.安装rabbitmq
kubectl apply -f rbac.yaml -n es
kubectl apply -f rabbitmq.yaml -n es

rabbitmq启动成功后，将rabbitmq绑定服务器端口，登录rabbitmq控制台，创建用户
kubectl apply -f rabbitmq-network -n es


创建管理员用户/密码：devops/devops

7.安装eazybuilder前后端
kubectl apply -f eazybuilder-server.yaml
kubectl apply -f eazybuilder-web.yaml