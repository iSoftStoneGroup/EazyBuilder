
# [中文说明](./README_CN.md)
# Jenkins basic image
-This project is based on the official jenkins image, and some necessary plug-ins are installed
# ci-tool
-This directory is used to store all enhancement plug-ins. When making an image, *. jar in this directory will be copied to the image
-The platform provides four plug-ins. You need to put the compiled jars of these plug-ins into the ci tool directory
```shell
eazybuilder-server/pipeline/buildfile-decorator
eazybuilder-server/pipeline/legacy-ant-adapter
eazybuilder-server/pipeline/report-collector
eazybuilder-server/pipeline/sql-scanner
```
# .git-credentials
-Git configuration file to configure git robot account
# ansible.cfg
-Ansible configuration
# executors.groovy
-jenkins configuration file
# plugins.txt
-Jenkins plug-in list, which can be added or deleted
# push-war.yml
-Script for pushing war
# setting.xml
-For the configuration file of maven nexus, you need to change the registry to your own warehouse address
