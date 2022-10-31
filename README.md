Welcome to the EazyBuilder project!

EazyBuilder is an event-driven orchestration engine, which is based on the open-source distributed DevOps platform products independently designed and developed by Kubernetes. It reduces the complexity of continuous delivery pipeline, has flexible and easy to use, safe and reliable pipeline, sound change management system, two-way traceability of the whole link. Small smart approval flow, cloud native IDE plugin, it will be linked to various operations in the cloud native applications, provide butt plug type, degree of quantitative, objective and process data accurately and effectiveness of insight, provide enterprises with a unified enterprise collaboration plane, make it more easy and convenient management cloud native application on infrastructure, enterprise culture, landing To develop and distribute high quality, secure software more efficiently. EazyBuilder built-in jenkins/Buddy,K8s YAML, Harbor, redmine/zentao/tapd and other complex scenario best practices, suitable for sensitive and stable two-state, high-frequency stable delivery scenarios.

Our goal is through the use of native technology and integration of cloud, makes the acme contracted, efficient and easy to use the cloud native work platform, standardize the development process, simplify the development difficulty, improve the efficiency of development, enhance the code quality, support the entire software development life cycle (development, unit testing, integration testing, documentation, production release, To provide a complete cloud native development ecological environment and ecological system, so that enterprises can quickly embrace cloud native, improve research and development efficiency, and then feed back the cloud native market.

# [中文概述](./README_CN.md)

# Features and Advantages

# # features

### ### ** Portable and safe high efficiency assembly line **

Event-driven, support wildcard, for different code branches, configure different event types, trigger different pipeline. The operation procedure is simple. You do not need to be familiar with pipeline script syntax, visual layout, and quickly create efficient pipelines. It does not rely on three-party tools, and uses modular design to solve the problem of pipeline sharing and reuse. Provides fine-grained access control, single sign-on (SSO), and audit functions based on roles, records each user's operations, and pipeline change history to meet user security requirements. Built-in rich warning means, comprehensive monitoring of pipeline status, active sending of abnormal logs, continuous improvement of the pipeline through data-driven.

- ** flexible and easy to use **: only provide project SVN/GIT resource library information, you can experience the whole process of standardized processing from packaging construction, unit testing, coverage scanning, quality vulnerability scanning, component warehouse push, automatic deployment;
- ** Precise reminder **: pipelined process and scan report, code submission and merger, real-time push by email and pinning, accurate subscription configuration is supported;
- ** Rich integration methods **: Support Webhook, timed automatic build, event or through OpenAPI integration and trigger continuous integration pipeline;
- ** Plug-in enhancements **: Quality, vulnerability scanning, and other extension pipeline steps are based on automated plug-in decoration and enhancement techniques, with no additional plug-in installation, configuration file, or directory structure requirements for the source code project, and no impact on the build output;
** Visual Orchestration: The on-off visual orchestration of pipeline, with a large number of processing templates for common CI processes such as build, test and deployment, supports rapid customization and extension of continuous integration pipeline;
- ** Elastic Resource scheduling: supports load balancing and K8S elastic scheduling for large-scale parallel construction processing and resource scheduling, making full use of hardware resources.
- ** Mainstream development framework support **: compatible with mainstream Java, C# and front-end development project code compilation build and release mode; Support Docker image repository, Maven repository and NPM repository push;
- ** Project quality control **: Fully master all kinds of project quality index data scanned in the process of assembly line operation, can customize quality access control, red light repair reminder, support custom statistical analysis dimensions, and provide online quality reports or email reports.



### ** Friendly cloud native environment **

Copy minutes level environment, provide out-of-the-box cloud native plug-ins, such as: redis, directing, rabbitmq, mysql. Using standard k8s yaml management infrastructure middleware, easy to extend and easy to use.


### **A full range of automated testing guarantee **

With built-in sonarqube jmeter, liqiubase quality control tools, such as with safety as the criterion, the safety testing (code review, analysis, testing, etc.) move to the early stages of software development life cycle, so as to more effectively develop and publish high quality, the security software.


### ** Two-way traceability throughout the life cycle **

EazyBuilder from requirements creation, code development, automated testing, to the final online deployment, set a rich buried point, can be in the development of the whole life cycle of any link, do two-way traceability, integration of the cloud native development of massive messages, to avoid the information island.


### ** Multi-dimensional measurement monitoring platform **

EazyBuilder adopts four horizontal and two vertical dimensions, respectively from the product, research and development, testing, deployment dimension, as well as R & D personnel, project management personnel dimension, to provide accurate objective and comprehensive performance measurement data, to meet the different roles of different management requirements, accurate analysis of R & D performance shortcomings, promote steady improvement.


### ** Cloud native IDE plugin **
Developers can get the core capabilities of EazyBuilder in the IDE without switching platforms. After coding, it can realize code quality scanning, automatic compilation and packaging, one-click hot deployment to the self-test environment, quickly complete self-test, joint investigation and integration verification, and double the development efficiency.


# ** Getting Started **

-Eazybulider contains a front end and a back end application, code directories are: eazybuilder-web,eazybuilder-server

# # # # Docker installation

- Docker version: 1.13.0+

- EazyBuilder image has been published to [Docker Hub](https://hub.docker.com/ "Docker Hub");
You can use the official EazyBuilder image directly

- Build your own image

Generate a mirror: docker-compose build

- Start the application.

docker-compose  up -d

Use the default compose file :[dockercompose/docker-compose.yml](./dockercompose/docker-compose.



Visit http://localhost/console/index.html to

# # # # Kubernetes installation
See the details tutorial for installation :[k8s/README.md](./k8s/README.md)

- Build machine

The pipeline execution in EazyBuilder, which is compatible with jenkins, needs to initialize the build environment in jenkins in advance. Details you can refer to [Jenkins dockerfile] (. / eazybuilder - server/thridparty/Jenkins/SRC/main/docker/dockerfile)



# ** Contribute to **

Welcome contributors to the EazyBuilder project. Check [CONTRIBUTING](./CONTRIBUTING.md) to see how to contribute to the project.


Scope of # #


### Scope of application

EazyBuilder is designed to solve the problem of creating a CI pipeline quickly and easily. Therefore, the project will implement or has implemented:

* [Build Resource Management]
* [Visual Pipeline Choreography]
* [Pipeline sharing reuse]
* [Pipeline monitoring and warning]
* [event-driven pipeline]
* [Message warning]
* [OpenAPI integration support]

### out of scope

EazyBuilder will be used with other tools in a cloud native environment. Therefore, the following specific features will not be included:

* [Container Resource Management]
* [Quality Specification Integration]
* [Code Hosting]
* [Automated testing]
* [Demand Management]
* [Product Management]

# ** Contact us **


- "Welcome everyone to pay attention to eazybuilder, in the use of problems, you can add group discussion"

- ** Welcome to the exchange group. You can scan the QR code below to invite you to join the group. Please indicate to join easybuilder**

<img width="400" alt="image" <="" span=""> src="https://user-images.githubusercontent.com/1069092/189043893-fe653257-3180-464b-85e1-c91876b29df4.jpg">


# ** Resources **

The latest documentation for each release can be found at:

- [EazyBuilder 1.0.0] (. / doc/referencebook/v1.0.0 referencebook_cn. Md)


# ** Download **

- [Github Release](https://github.com/iSoftStoneGroup/EazyBuilder/releases)


# ** Permission **

See [LICENSE](./LICENSE) for more information.

Rule of # #

We follow the CNCF Code of Conduct (./CODE_OF_CONDUCT.md).

# # roadmap
<img width="1000" alt="image" src="docs/eazybuilder.png" height="800">

## Roadmap design details

### Line management

#### Visualizes pipelined orchestration
The back end uses freemark template, solidifies each build into a template, the front end uses switch items, the user selects the build content, input the key information, the back end fills the data into the template, generates the completed jenkins pipeline script, calls jenkins api, and executes the pipeline

#### Pipeline creation with one click (Java,C#,JavaScript)
The fixed configuration, all abstracted, so that the user only need to input the code warehouse address, can automatically create a pipeline

#### Pipeline audit, rollback
After a pipeline is modified, the background records it and stores it in the history table. The difference between any two pipeline versions can be compared, and the pipeline can be rolled back to any version

#### Integrates automatic deployment of Kubernetes
Using kubectl+kubeconfig, the platform integrates kubectl. Users can specify different kubeconfig files in each pipeline, and use kubectl --kubeconfig to manage k8s cluster in the pipeline, so as to realize the cross-cluster of k8s. Cross environmental management


#### Integrates with Sonarqube to view technical debt directly within the platform
- Method 1: Invoke the sonarqube api to query the quality details of the project code
- Method 2: Directly query the sonarqube table to query the quality details of the project code


#### Integrates with jmeter to trigger automated test scripts in a pipeline
The platform integrates the jmeter plug-in. In the build switch, the user turns on the switch of "automated test" and specifies the directory location of automated test scripts. The pipeline directly executes the jmeter command to run automated tests

### Build resource management

#### Builds automatic resource utilization and recycling
Jenkins Kubernetes Agent plug-in is used to automatically create a pod in k8s cluster when the pipeline runs, and automatically delete the pod after the pipeline runs


#### Self-service interactive pipeline orchestration, where users customize the build steps included in the pipeline on demand
User-defined events, such as code submission, code merge. For each type of event, users can choose different assembly lines, so that different projects, different environments, to develop different assembly lines

### Configuration file management

Docking with nacos api, the user also puts the configuration file in the code warehouse. After the configuration file is changed, the corresponding pipeline is triggered, and nacos is called in the pipeline to update the configuration file in nacos

### # Database script management

Connect to liqiubase and use liqiubase as the sql management platform

### # Quality safety management

#### LDAP Login authentication

Integrated with the ldap function, you can log in as an ldap user

#### Assembly line access card control

You can set a threshold for access control in the system. For example, BUG blocking is no greater than 5. After the code is submitted, the code quality scan is performed in the pipeline to collect sonarqube scan results. If they do not meet the quality threshold set by the system, the pipeline will be considered as a failure and the subsequent process will be stopped

#### Integrated Gitlab, MR Automatic audit

Connect to gilab api, when the project code meets the quality access, automatically create merge request, automatically merge into the integration branch

#### The submission code comment specification is strongly blocked

Hooks are set in gitlab to verify the comments of the submitted code. The comments must contain a legitimate task id. Otherwise, the code cannot be submitted

#### Message warning configuration

Design an alert configuration page that allows users to subscribe to message types such as code commit/merge, pipeline success/failure; The form of message sending can be Dingtai, email or wechat

### # R&D process management

#### Assembly line warning

Design a red light repair switch, the user can open in the pipeline, and configure the warning time interval, times, if the pipeline fails, send a red light repair warning, so that developers can pay attention to some high-risk pipeline, timely repair development integration problems
