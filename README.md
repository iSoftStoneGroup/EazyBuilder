
# A completely free enterprise level continuous integration platform

# Welcome to the EazyBuilder project!

EazyBuilder focuses on multi-team and multi-project unified compilation, build, scan, deployment and other continuous integration automation pipeline requirements, through plug-in automatic injection and decoration, pipeline template, improve traditional continuous integration tools (such as directly using Jenkins/Hudson) configuration is cumbersome and professional High requirements (for example, you must be familiar with Jenkinsfile/pipeline/groovy syntax, etc.), complex environment dependencies (shared library needs to rely on code bases when running), difficult configuration of large-scale projects, and difficult management.

# [中文概述](./README_CN.md)

# Features and Benefits


## Features
- **Flexible and easy to use**: You only need to provide the project SVN/GIT repository information, you can experience the standardization of the whole process from packaging construction, unit testing, coverage scanning, quality vulnerability scanning, component warehouse push, and automatic deployment;
- **Precise reminder**: pipeline process and scan reports, code submission and merging, real-time push via email, DingTalk, etc., and precise subscription configuration;
- **Abundant integration methods**: Support Webhook, timed automatic build, event or integrate and trigger continuous integration pipeline through OpenAPI;
- **Plug-in Enhancement**: Quality, vulnerability scanning, and other extended pipeline steps are based on automated plugin decoration and enhancement technology. There are no additional plugin installation, configuration files, or directory structure requirements for source code projects, and no additional requirements for build outputs. influences;
- **Visual orchestration**: switch-style pipeline visual orchestration method, built-in a large number of processing templates for common CI processes such as build, test, deployment, etc., support rapid customization and expansion of continuous integration pipelines;
- **Elastic Resource Scheduling**: Supports build machine load balancing, K8S elastic scheduling, supports large-scale parallel construction processing and resource scheduling requirements, and makes full use of hardware resources;
- **Mainstream development framework support**: Compatible with mainstream Java, C# and front-end development project code compilation, build and release modes; support Docker image repository, Maven repository and NPM repository push;
- **Project quality monitoring**: comprehensively grasp various engineering quality index data scanned during the operation of the assembly line, can customize quality access control, red light repair reminders, support custom statistical analysis dimensions, and provide online quality reports or emails report.

## Advantage
- **Simple configuration**: The operation steps are simple, no need to be familiar with the pipeline script syntax, visualize the arrangement, and quickly create an efficient pipeline.
- **Non-intrusive integration**: Does not depend on third-party tools, uses modular design, and solves the problem of pipeline sharing and reuse.
- **Enterprise-level security**: Provides fine-grained access control, single sign-on and auditing functions based on roles, records the operation content of each user, and changes the history of the pipeline to meet the security needs of users.
- **Pipeline monitoring and early warning**: Built-in rich early warning methods, comprehensively monitor the pipeline status, actively send abnormal logs, and continuously improve the pipeline through data-driven.
- **Event-driven**: Support wildcards, configure different event types for different code branches, and trigger different pipelines.


# **Getting Started**

- Eazybulider includes a front-end and a back-end application, the code directories are: eazybuilder-web, eazybuilder-server

#### Docker installation

- Docker version: 1.13.0+

**Eazybuilder supports deployment package installation and docker installation. You can refer to the following steps to make your own image
  
  
- Self-built mirrors

  Generate an image: docker-compose build

- start the app

  docker-compose version 1.27.4, build 40524192

  docker-compose up -d

  Use default compose file: [dockercompose/docker-compose.yml](./dockercompose/docker-compose.yml)

 #### Kubernetes Installation
Install according to the detailed tutorial : [k8s/README. md](./k8s/README.md)

Just visit http://localhost/console/index.html

- Build machine

   The pipeline execution in EazyBuilder depends on jenkins, and the build environment needs to be initialized in jenkins in advance. For details, please refer to [jenkins dockerfile](./eazybuilder/eazybuilder-server/thridparty/jenkins/src/main/docker/Dockerfile)

# **Contribute**

Contributors are welcome to join the EazyBuilder project. Please see [CONTRIBUTING](./CONTRIBUTING_EN.md) to learn how to contribute to this project.


## Scope


### Scope of application

EazyBuilder is designed to solve quick and easy creation of CI pipelines. Therefore, the project will implement or has implemented:

* [Build Resource Management]
* [Visual Pipeline Orchestration]
* [Pipeline shared multiplexing]
* [Pipeline monitoring and warning]
* [event-driven pipeline]
* [Message Alert]
* [OpenAPI integration support]

### Out of range

EazyBuilder will be used with other tools in a cloud-native environment. Therefore, the following specific functions will not be included:

* [Container Resource Management]
* [Quality Protocol Integration]
* [code hosting]
* [automated test]
* [Demand Management]
* [Product Management]

# **Contact us**

- Welcome to pay attention to eazybuilder, if you encounter problems in use, you can join the group discussion)

- **Welcome to join the communication group. You can scan WeChat QR code below and invite you to join the group. Please specify to join easybuilder**

<img width="400" alt="image" src="https://user-images.githubusercontent.com/1069092/189043893-fe653257-3180-464b-85e1-c91876b29df4.jpg">


# **Resource**

The latest documentation for each release can be found at:

- [EazyBuilder 1.0.0](./doc/referencebook/v1.0.0/referencebook_en.md)


# **Download**

- [Github Release](https://github.com/iSoftStoneGroup/EazyBuilder/releases)


# **License**

See [license](./LICENSE) for more information.

## Guidelines

We follow the [CNCF Code of Conduct](./CODE_OF_CONDUCT_EN.md).

## Road Map
<img width="1000" alt="image" src="docs/eazybuilder.png"   height="800" >


## Road Map Design Details

### Pipeline management

####  Visual pipeline layout

The back-end uses freemark templates to solidify each type of construction into templates. The front-end uses switch items. The user selects the construction content and enters key information. After the back-end fills the data into the template, it generates the completed jenkins pipeline script, calls the jenkins api, and executes the pipeline

#### Pipeline one click creation (Java, C #, JavaScript)

All fixed configurations are abstracted, so that users can automatically create a pipeline by entering the code warehouse address

#### Pipeline audit, rollback

After the pipeline is modified, the background records it and stores it in the history table. You can compare the pipeline differences between any two versions, and specify the pipeline to roll back to any version

#### Integrated Kubernetes automatic deployment

Using the kubectl+kubeconfig method, the platform integrates kubectl. Users can specify different kubeconfig files in each pipeline, and use kubectl -- kubeconfig in the pipeline to manage the k8s cluster, so as to achieve cross cluster and cross environment management of the k8s

#### Integrate Sonarqube to directly view the technical debt in the platform

-Method 1: Call the sonarqube api to query the quality details of the project code
-Method 2: Directly query the table of sonarqube to query the quality details of the project code

#### Integrate jmeter to trigger automated test scripts in the pipeline

The platform integrates the jmeter plug-in. In the build switch, the user turns on the "automatic test" switch and specifies the directory location of the automatic test script. The pipeline directly executes the jmeter command and runs the automatic test

### Build resource management

#### Build automatic utilization and recycling of resources

Using the Jenkins Kubernetes Agent plug-in, when the pipeline is running, a pod is automatically created in the k8s cluster. After the pipeline runs, the pod is automatically deleted

#### Self service interactive pipeline layout, users can customize the construction steps included in the pipeline as required

User defined events, such as code submission and code merge. For each type of event, users can select different pipelines, so that different pipelines can be formulated for different projects and environments

### Profile Management

Connect with the NACOS API. The user also places the configuration file in the code warehouse. After the configuration file changes, the corresponding pipeline will be triggered, and NACOS will be called in the pipeline to update the configuration file in NACOS

### Database Script Management

Connect with liqiubase and use liqiubase as the sql management platform

### Quality safety management

#### LDAP login authentication

Integrated ldap function, you can log in using ldap users

#### Assembly line access card control

The user configures the threshold of access control in the system, for example, the BUG blocking is not greater than 5; After the code is submitted, perform code quality scanning in the pipeline, collect the scan results of sonarqube, and judge whether they are consistent with the quality threshold set by the system. If they are not consistent, the pipeline will fail and the subsequent process will be aborted

#### Integrated Gitlab, MR automatic audit

Connect the gilab api. When the project code meets the quality access control, the merge request will be automatically created and automatically merged into the integration branch

#### Submit code comments to standardize strong card control

Set a hook in gitlab to verify the comments of the submitted code. The comments must contain a valid task ID. If they do not conform to the specification, code submission is prohibited

#### Message alert configuration

Design an alert configuration page to allow users to subscribe to message types, such as code submission/consolidation, pipeline success/failure; Message sending can be in the form of stapling, email or WeChat

### R&D process management

#### Pipeline Alert

A red light repair switch is designed. The user can turn it on in the pipeline, and configure the time interval and times of alerts. If the pipeline fails, a red light repair alert will be sent. This allows developers to focus on some high-risk pipelines, and timely repair development integration problems

#### Integrate Redmine/Zen to establish the traceability relationship between pipeline and demand task

-Integrated demand management platform. At present, it is considered to interface with Redmine and Zen. Developers submit code, and the comment must contain the task ID (in the demand management platform)
-After the pipeline runs, it sends the running result to the demand management platform (by calling the api), automatically appends the content to the progress under the task, and automatically updates the task status of the demand management platform

#### Pipeline approval process, realizing the approval of testing, launching and deployment

For assembly lines in different environments, do permission control, design the approval process, automatically promote products after approval, and deploy test/production environments

#### Nail approval process docking, realizing the work flow of assembly line in nailing

Nail approval process, which involves the approval function, is approved by nails

#### Pipeline Integration Grafana Measurement Report

Integrate Grafana, and make Grafana reports from the platform's process data. Different roles can see different measurement reports



