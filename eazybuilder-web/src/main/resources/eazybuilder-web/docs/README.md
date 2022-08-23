## 产品研发持续集成和自动化构建工具

[使用指南下载](docs/CI平台使用指南.doc)
<br/> 
#### 目录  

<li><a href="#!/help#intro">功能介绍</a></li>
<li><a href="#!/help#quickstart">快速开始</a></li>
<li><a href="#!/help#job">自动构建任务</a></li>
<li><a href="#!/help#pipeline">自定义流水线步骤</a></li>
<li><a href="#!/help#build">常见工程构建方式说明</a>
	<ul>
        <li><a href="#!/help#buildMaven">Maven构建</a></li>
		<li><a href="#!/help#buildAnt">ANT构建</a></li>
		<li><a href="#!/help#buildNpm">前端工程NPM构建</a></li>
    </ul>
</li>
<li><a href="#!/help#gitWebhook">GITLAB钩子设置说明</a></li>
<li><a href="#!/help#docker">自动构建和推送docker镜像说明</a></li>
<li><a href="#!/help#deploy">自动部署说明</a></li>
<li><a href="#!/help#maven">Maven库管理</a></li>
<li><a href="#!/help#qa">其他常见问题</a></li>

<br/>  

<span id="intro" />  

#### 功能介绍 


&emsp;&emsp;只需提供源码仓库地址，``工程无需额外配置``，提供:  

``工程构建 > 单元测试 > 质量扫描 > SQL兼容性扫描 > Docker镜像构建/推送仓库 > 自动部署   ``  

的全流程标准化/自动化流水线 。


**核心特性：** 

- 支持GIT、SVN

- 支持Maven项目、NPM前端项目(VUE、AngularJS、React)

- 标准化的流水线，开箱即用：  

<img src="docs/images/pipeline.png" width="600px"></img>

- 全过程关键质量指标汇总和邮件报告  

<img src="docs/images/report.png" height="400px"></img>

- 整体研发项目工程质量汇总统计  

<img src="docs/images/statistic.png" width="600px"></img>

<br/>  

<span id="quickstart" />

#### 快速开始

- (1)使用公司域用户登录系统：

<img src="docs/images/login.png" width="400px"></img>

- (2)项目负责人添加成员(新项目请联系管理员添加项目)

<img src="docs/images/add-member.png" width="900px"></img>

可以指定一个或多个用户为您项目的配置管理员，配置管理员可以协助项目负责人维护项目成员、添加项目工程信息

<i>注：配置管理员可设置项目组成员。每个成员都需保证至少登录过一次系统，系统下拉列表才可勾选到对应员工。</i>

- (3) 添加docker镜像仓库(可选)

添加项目的镜像仓库和访问凭证信息，用于后续流水线自动构建后，推送docker镜像到仓库。

<i>注：该功能只有项目负责人或配置管理员可见</i>

- (4) 配置项目工程信息  

添加项目的工程信息，包括工程名、对应的源码仓库地址和访问信息，默认的构建方式等。

<i>注：该功能只有项目负责人或配置管理员可见，工程信息名称（中文名和英文名）必须为实际意义上的名称，不可设类似“123”名称。且不可与已有工程名称重名</i>

<img src="docs/images/create-project.png" width="900px"></img>

*示例配置：* 

<img src="docs/images/project-setting.png" height="400px"></img>

- (4) 通过流水线 > 立即构建 可人工立即触发自动化流水线

<img src="docs/images/trigger-pipeline.png" width="900px"></img>

在弹出框中选择自定义构建配置，或者按默认步骤执行（默认为工程设置的时候选择的构建过程）：

<img src="docs/images/select-profile.png" width="500px"></img>

- (4) 人工触发的构建流水线，邮件报告只会发送给当前操作人

<img src="docs/images/mail-report.png" width="500px"></img>

- (5) 也可以通过添加定时构建任务，按指定时间计划(CRON表达式)自动运行流水线

<br/>

<span id="pipeline" />

#### 自定义流水线构建步骤  

&emsp;&emsp;默认的流水线步骤在工程设置时指定，系统中预置了一些常用的公共构建步骤定义;  

如果需要跳过特定步骤，可以添加自定义构建设置：

<img src="docs/images/pipeline-menu.png" width="900px"></img>

其中：公开-其他所有项目组可见，私有-只有指定项目组成员可见

新增自定义设置即可在工程设置、手工执行流水线或维护自动构建任务时使用：

<img src="docs/images/pipeline-setting.png" width="900px"></img>

<br/>

<span id="job" />

#### 自动构建任务

&emsp;&emsp;自动构建任务支持按指定时间计划、GitLab钩子或手工触发，运行批量工程的构建流水线任务：

<img src="docs/images/build-job.png" width="900px"></img>

以下是一个基于GitLab钩子自动触发，构建运行完成后会发送通知邮件，同时在钉钉群里发送结果的配置示例：

<img src="docs/images/job-detail.png" width="900px"></img>

对应gitlab项目的设置：

<img src="docs/images/gitlab-entry.png" width="900px"></img>

<img src="docs/images/gitlab-callback.png" width="900px"></img>

上述示例中将在gitlab仓库接收到commit和tag时触发CI自动构建任务。

对于钉钉群组机器人的配置，我们先在【群设置】-【智能群助手】中添加自定义webhook机器人：

<img src="docs/images/dingtalk-entry.png" width="400px"></img>

<img src="docs/images/webhook-robot.png" width="400px"></img>

获取webhook URL并勾选加签、记住secret：

<img src="docs/images/robot-setting.png" width="400px"></img>

将相应信息维护在自动构建任务中即可。


<br/>

<span id="build" />

#### 常见工程构建方式说明

<span id="buildMaven" />

##### 1. Maven工程构建

&emsp;&emsp;Maven工程默认在当前workspace(即根据源码URL拉取的目录根路径)执行`mvn install`；如果需要单独构建某个子模块，可以在构建过程定义的`自定义构建参数中`输入：

        mvn -f sub_module/pom.xml install

如果需要推送到nexus等Maven私有仓库

		<!--在pom.xml中配置distributionManagement-->
		<distributionManagement>
			<repository>
				<id>team-release</id>
				<url>${release.repo}</url>
			</repository>
			<snapshotRepository>
				<id>team-snapshot</id>
				<url>${snapshot.repo}</url>
				<uniqueVersion>false</uniqueVersion>
			</snapshotRepository>
	   </distributionManagement>

添加自定义构建参数：
		
		#发布release到maven私有仓库
		mvn deploy -Drelease.repo=http://YOUR_REPO_URL  -Drelease.repo.id=team-release -Drelease.repo.username=xxxx -Drelease.repo.password=xxxx

		#发布snapshot到maven私有仓库
		mvn deploy -Dsnapshot.repo=http://YOUR_REPO_URL  -Dsnapshot.repo.id=team-snapshot -Dsnapshot.repo.username=xxxx -Dsnapshot.repo.password=xxxx

<span id="buildAnt" />

##### 2. Ant工程构建

&emsp;&emsp;构建ANT工程请确保您配置的源码URL下包含了所有ant build过程中需要的所有资源（源码、jar等），在`自定义构建参数中`输入：

        ant //如果有build.xml
        ant -f build-project.xml  //指定ant build文件位置

<span id="buildNpm" />

##### 3. 前端工程NPM构建

&emsp;&emsp;如果您需要NPM构建后自动创建Docker镜像，推荐添加pom.xml作为Java-Maven工程构建(使用maven-frontend-plugin)，直接的NPM构建适用于构建后推送到nexus、verdaccio等NPM私服的场景；在`自定义构建参数中`输入：

		#指定taobao registry并构建
        nrm use taobao && npm install && npm run build 
        #发布到指定NPM仓库
		npm-cli-login -u USER -p PASSWORD -e EMAIL -r http://YOUR_NPM_SERVER && npm publish --registry=http://YOUR_NPM_SERVER 



<span id="gitWebhook"/>

##### GITLAB系统钩子设置说明

CI支持Gitlab系统钩子设置，当gitlab仓库有push事件时，自动匹配并触发对应工程的默认自动构建。   

设置方式如下： 

- 使用管理员用户登录gitlab，进入"管理区域">"系统钩子"
<img src="docs/images/gitlab-systemhook-entry.png" width="900px"></img>

- 参考以下信息添加gitlab系统钩子
<table>
  <tr><td colspan="2">gitlab系统钩子设置</td></tr>
  <tr><td>链接(URL)</td><td>http://127.0.0.1:8080/ci/wh/system</td></tr>
  <tr><td>安全令牌</td><td>Eazybuilder-CI</td></tr>
<tr><td>触发器</td><td>勾选“推送事件”</td></tr>
<tr><td>SSL 证书验证</td><td>不勾选</td></tr>
</table>
- 在对应工程设置中开启自动构建开关：(默认为关闭，需要手工开启)

<img src="docs/images/project-autobuild.png" width="900px"></img>

<br/>

<span id="docker" />

#### Docker镜像构建说明（不需要可跳过）


&emsp;&emsp;自动构建Docker镜像，需要在对应工程/子工程中添加src/main/docker文件夹，并将Dockerfile放入其中；

以下内容在构建docker镜像时会自动include到Dockerfile同级：

1. ``target/\*.jar``

2. ``target/\*.war``

3. ``config``  ——工程下的config目录及其中的所有文件/子目录

在Dockerfile中，可以直接COPY/ADD到镜像中：

        COPY  config    /target_path/config
        ADD   xxx.jar   /target_path/xxx.jar

如果src/main/docker/Dockerfile存在，流水线会自动触发构建docker镜像，并以下述镜像名推送到仓库：

  ``<docker镜像仓库>/<Organization Name or GroupId>/<artifactId>:latest``

<br/>

<span id="deploy" />

#### 自动部署说明（不需要可跳过）


&emsp;&emsp;支持Ansible playbook

(Ansible是一个简单的自动化运维管理工具，可用于自动化部署应用、配置、编排task(持续交付、无宕机更新等)

需要：

1. 在工程根目录下创建deploy目录，并将playbook.yml和hosts文件放到deploy目录下

  ``playbook.yml``——ansible自动部署标准配置文件
  
  ``hosts`` —— ansible inventory配置文件，配置部署节点主机信息(/etc/ansible/hosts) 

2. 在流水线自动部署阶段，将自动执行上述playbook：

        ansible-playbook -f playbook.yml -i hosts  
    

3. 如果你对ansible和playbook.yml编写有疑问，可以参考以下教程：

   [**Ansible Quick Start**](http://www.ansible.com.cn/docs/quickstart.html)
   
4. 以下是平台本身自动部署的hosts和playbook.yml示例：  


<pre>
#hosts
[dev]
dev-server43  ansible_ssh_host="127.0.0.1"  ansible_ssh_pass="password" ansible_ssh_port=22
#可以在分组下添加多个，每行一个
#主机别名  ansible_ssh_host=#  ansible_ssh_pass=# ansible_ssh_port=#
</pre>
  
<pre>
#playbook.yml
---
- hosts: dev
  #远程用户
  remote_user: root
  #部署任务，会顺序执行
  tasks: 
    #创建工作目录
    - name: create dir
      shell: mkdir -p /opt/ci
    
    #上传/更新部署脚本
    - name: upload /update script
      copy:
         src: docker-compose/
         dest: /opt/ci/
    
    #检查是否已安装过
    - name: check if already installed
      #这里直接通过docker inspect容器名，如果容器不存在会报错(视为未安装过)
      shell: docker inspect ci_ci-console_1
      ignore_errors: True
      register: installed

    #没有安装过，执行安装脚本
    - name: install
      shell: cd /opt/ci && sh install.sh
      when: installed is failed

    #已安装过，执行更新脚本
    - name: update
      shell: cd /opt/ci && nohup sh update.sh 2>&1 &
      when: installed is succeeded
</pre>
   
``注：install.sh和update.sh为通过docker-compose 安装/更新容器的脚本``

例如:``update.sh``
<pre>
#!/bin/bash
docker-compose pull #更新镜像
docker-compose stop ci-nginx && docker-compose rm -vf ci-nginx #移除nginx容器
docker-compose stop ci-console && docker-compose rm -vf ci-console #移除console容器
docker-compose up -d #重新创建并启动发生变更的容器
</pre>

<br/>

<span id="maven" />

#### Maven库管理  

平台自动化构建的包除可以通过自动部署的方式自动拷贝/安装到目标机器上，在平台服务器Maven本地库上也有暂存，如有需要针对Maven本地库维护管理，需要向持续集成组申请入库处理（提供项目组、以及入库jar的Maven GAV信息），以下入库操作由管理员账号处理：

1. 下载包

点击【系统管理】>【Maven本地仓库管理】

<img src="docs/images/maven-repo.png" width="900px"></img>

通过包层级结构浏览目录(双击打开目录)，在想要下载的文件上右键，选择download即可下载：

<img src="docs/images/maven-download.png" width="900px"></img>

2. 删除无效的包/目录

如同下载包步骤，在右键菜单中选择remove即可删除

3. 自定义上传包

点击右上角图标如图，弹出入库资料填写页面：

<img src="docs/images/maven-add-local.png" width="900px"></img>

填写包相关资料，选择文件后，点击【确认】即可：

<img src="docs/images/maven-local-form.png" width="600px"></img>

<span id="qa" />

#### 其他常见问题


- maven构建失败，提示无法获取依赖包 ?

&emsp;&emsp;请检查工程中是否有配置项目maven私有仓库(例如nexus)，或者maven私有仓库中是否已包含该依赖包；
如果依赖无法从maven公有仓库获取，而且项目没有自己的私有仓库；请联系持续集成组手工添加依赖包到构建环境的maven本地库

- 编译失败，怀疑是本地Maven依赖没有更新，如何清理CI的本地Maven库?

&emsp;&emsp;在构建过程定义中，添加以下自定义构建参数：

	mvn dependency:purge-local-repository

- 怎样在Maven打包时添加源代码版本号(SVN Revision或者Git Commit)?

&emsp;&emsp;自动化平台在构建时会自动添加以下Maven环境变量：`SVN_REVISION`和`GIT_COMMIT`，可以在maven-jar-plugin或者maven-war-plugin中引用并在打包时添加到META-INF/manifest中，例如：

    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-jar-plugin</artifactId>
        <configuration>
            <archive>
                <manifestEntries>
                    <ci-svn-revision>${env.SVN_REVISION}</ci-svn-revision>
                    <ci-git-commit>${env.GIT_COMMIT}</ci-git-commit>
                    <!--打包的时间戳-->
                    <ci-build-time>${maven.build.timestamp}</ci-build-time>
                </manifestEntries>
            </archive>
        </configuration>
    </plugin>

- Sonar质量扫描时，如何跳过js等其他不想扫描的文件?  

在Maven工程 pom.xml中添加以下属性(properties):

    <!--排除扫描js文件,多个排除规则以逗号(,)隔开-->
    <sonar.exclusions>**/*.js</sonar.exclusions>

        




