# Third party plugins

EazyBuilder integrates plugins such as jenkins and sonarqube. This directory contains the Dockerfile for making plugin images. Recreating the image using the Dockerfile is not necessary. You can configure the existing plugin access address directly in the EazyBuilder configuration file.

# [中文说明](./README_CN.md)

# Directory description

##x86 support
- The platform uses the image of the x86 architecture by default.

## Notice
- The access address, password and other sensitive information contained in the file have been specially processed. Before re-creating the image, you should first read the README file in the directory in detail, modify the script, and set the access address and password to real and effective information.

## Jenkins Instructions
- The platform automatically generates the jenkins pipeline script, and then calls the jenkins interface to run the pipeline. This directory contains three jenkins related images, the specific functions are as follows:
### jenkins-base

- The basic image, based on the official jenkins/jenkins image, installed some necessary plugins, and installed some pipeline tools: maven, gradle, sonar-scanner, nodejs, kubernetes docker-plugin, subversion, ansible.

### jenkins-inbound-agent

- The base image, based on the official jenkins/inbound-agent, is dedicated to kubernetes, similar to jenkins-base, and has installed some necessary plugins and tools for the pipeline.

### jenkins-inbound-agent-java jenkins-inbound-agent-net

- Based on the jenkins-inbound-agent image, some customized feature transformations have been made. Users need to modify the dockerfile and make their own images according to their actual situation

## For details, please refer to the README file in each directory
