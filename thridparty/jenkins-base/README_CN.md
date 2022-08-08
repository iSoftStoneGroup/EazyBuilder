
# jenkins基础镜像

- 此工程，是基于jenkins官方镜像，安装了一些必须的插件

# ci-tool
- 这个目录，是用来放一切增强插件，在制作镜像时候，会将这个目录中的*.jar，都复制到镜像中
- 平台提供了四个插件,需要将这些插件编译之后的jar,放入ci-tool目录

```shell
eazybuilder-server/pipeline/buildfile-decorator
eazybuilder-server/pipeline/legacy-ant-adapter
eazybuilder-server/pipeline/report-collector
eazybuilder-server/pipeline/sql-scanner
```
# .git-credentials
- git配置文件，配置git的机器人账户

# ansible.cfg
- ansible配置

# executors.groovy
- jenkins配置文件

# plugins.txt
- jenkins插件清单，可以对插件做增减

# push-war.yml
- 推送war的脚本

# setting.xml
- maven nexus的配置文件，需要更改registry为用户自己的仓库地址
