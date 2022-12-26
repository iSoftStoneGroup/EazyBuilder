#dockercompose目录说明
##挂载目录说明
- **jenkins**:jenkins的挂载目录。（可删除，启动容器时会自动创建）
- **mysql**:mysql的挂载目录。（可删除，启动容器时会自动创建）
- **nginx**:nginx的挂载目录。（需要开始的时候创建好）
- - **config**:nginx的配置挂载目录。（不可删除）
 需要修改配置，conf.d/default.conf中的proxy_pass指向后端接口实际的访问地址
  proxy_pass   http://0.0.0.0:8080/ci;
   
- **jenkins**:postgresql的挂载目录。（可删除，启动容器时会自动创建）
- **rabbitmq**:rabbitmq的挂载目录。（可删除，启动容器时会自动创建）
- **redis**:redis的挂载目录。（需要开始的时候创建好）
- - **redis.conf**:redis的配置文件。（不可删除）
- **sonarqube**:sonarqube的挂载目录。（可删除，启动容器时会自动创建）
##yml文件说明
- **eazybuilder-client**:eazybuilder项目的前后端一键部署。（需要依赖eazybuilder-dependent.yml）
- **eazybuilder-dependent**:eazybuilder项目的依赖。（需要先部署,部署前先执行startMvn.sh脚本）
##.sh脚本文件说明
- **startMvn.sh**:生成eazybuilder项目jar，前后端对应的Dockerfile文件需要用到。
- **startUp.sh**:一键部署eazybuilder项目脚本（现阶段不可以）。

# eazybuilder-server是平台的后端接口，在docker-compose编排文件中，可以指定连接nacos的url，namespace等参数，可以根据实际情况，修改配置

