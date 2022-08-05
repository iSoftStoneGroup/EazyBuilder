# 本目录为前端代码

# Pom文件说明

本工程为前端工程，但是目录结构，也是采用的maven模式，镜像生成，使用的maven docker plugin ,pom中的镜像仓库地址，已经做了处理，在编译之前，需要改成用户自己的镜像仓库地址


# 技术框架
- AngularJs

# 部署说明

1. **启动前端应用**

- 安装nginx,更改nginx.conf

```json
server {
    listen       80;
    server_name  localhost;
    location /ci {
    #后端接口地址
        proxy_pass   http://localhost:8080/eazybuilder-server/;
    }
    location / {
	    #前端代码目录
        root   E:/eazybuilder/eazybuilder-web/src/main/resources;
    }
}

```

- 启动nginx


2. **访问应用**

访问http://localhost/console/index.html 即可
