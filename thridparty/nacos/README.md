# [中文说明](./README_CN.md)
# Nacos basic image

- This project is based on the official image of nacos, and can directly use the image on the official website

- application.properties
This file is a nacos configuration file, mainly to solve the problem that nacos can only save 30 days of history
```shell
nacos.core.auth.enabled=true
nacos.config.retention.days=${CONFIG_RETENTION_DAYS:300}
```
