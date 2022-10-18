
# nacos基础镜像

- 此工程，是基于nacos官方镜像制作，可以直接使用官网的镜像

- application.properties
此文件为nacos配置文件，主要是为了解决nacos只能保存30天历史记录的问题

```shell
nacos.core.auth.enabled=true
nacos.config.retention.days=${CONFIG_RETENTION_DAYS:300}
```
