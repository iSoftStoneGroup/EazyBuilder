It is preferred to create a namespace (this step is not mandatory, and you can also deploy eazybuilder in other namespaces)
```shell
kubectl apply -f namespace.yaml
```
# 1. Install MySQL
```shell
kubectl apply -f mysql. yaml -n es
```
After the MySQL container is started, enter the container, execute the following command, and make initialization settings
```shell
mysql -u root -p
alter user 'root'@'%' identified by 'mysql_ 123' password expire never;
alter user 'root'@'%' identified with mysql_ native_ password BY 'mysql_ 123';
flush privileges;
```
# 2. Create database
```shell
create database ci;
```
## Note that this step can be completed by initializing SQL, directly executing SQL under the [initsql] (../initsql) directory, and importing all the data of eazybuilder and nacos at one time
# 3. Install nacos
```shell
kubectl apply -f nacos-quick-start. yaml -n es
```
# 4. Import the nacos log file
Create the namespace devops in nacos, and import the configuration file under the eazybuilder config directory
# 5. Install Redis
```shell
kubectl apply -f redis. yaml -n es
```
# 6. Install rabbit mq
```shell
kubectl apply -f rbac. yaml -n es
kubectl apply -f rabbitmq. yaml -n es
```
After Rabbitmq starts successfully, bind Rabbitmq to the server port, log in to the Rabbitmq console, and create a user
```shell
kubectl apply -f rabbitmq-network -n es
```
Create administrator user/password: devops/devops
# 7. Install the front and rear ends of eazybuilder
```shell
kubectl apply -f eazybuilder-server.yaml
kubectl apply -f eazybuilder-web.yaml
```
# 8. Create eazybuilder ingress
```shell
kubectl apply -f eazybuilder-ingress. yaml -n es
```
- Access address: http://es-web.eazybuilder.com/eazybuilder-web/login.html
- Note: es-web.eazybuilder.com is the K8S cluster pseudo domain name. For local access, you need to configure the host to map to the server in the cluster
