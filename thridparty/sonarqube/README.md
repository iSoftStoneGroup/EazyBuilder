# sonarqube基础镜像

- 此工程，是基于sonarqube官方镜像制作，可以直接使用官网的镜像

# plugins 目录

此目录中，可以放sonarqube的一些插件包，例如分支扫描，sql扫描等插件，由于文件过大，并没有将插件上传上来，需要用户自行下载
推荐的Sonarqube的插件包:

- p3c java代码规范扫描

在github中搜索sonar-pmd-p3c，任意找一个项目，将代码下载下来，编译成jar:sonar-pmd-plugin-*.jar


- 分支扫描
在github中搜索sonar-pmd-p3c，任意找一个项目，将代码下载下来，编译成jar:sonarqube-community-branch-plugin-*.jar

- sql 规范扫描
在github中，打开：https://github.com/gretard/sonar-sql-plugin，将代码下载下来，编译成jar:sonar-sql-plugin-*.jar

## 注意：选择的jar，要与自己sonarqube的版本保持一致。

