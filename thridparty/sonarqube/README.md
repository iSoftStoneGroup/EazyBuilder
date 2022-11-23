# [中文说明](./README_CN.md)

# Sonarqube basic image

- This project is based on the official image of sonarqube and can directly use the image on the official website
- 
# Plugins directory

In this directory, you can put some plug-in packages of sonarqube, such as branch scanning and sql scanning. Because the file is too large, the plug-in has not been uploaded, and you need to download it yourself
Recommended plug-in package for Sonarqube:

- P3c java code specification scanning

Search: sonar pmd p3c in github, find a project at random, download the code, and compile it into jar: sonar pmd plugin - *. jar

- Branch scan

Search in github: sonarqube community branch plugin. Find any project, download the code, and compile it into jar: sonarqube community branch plugin - *. jar

-SQL specification scanning

In github, open: https://github.com/gretard/sonar-sql-plugin , download the code and compile it into jar: sonar sql plugin *. jar

## Note: The selected jar should be consistent with the version of your sonarqube.
