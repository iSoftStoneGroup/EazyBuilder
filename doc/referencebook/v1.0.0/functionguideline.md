 

# **EazyBuilder功能指引**

**创建第一条流水线**

- 菜单->系统管理->项目组初始化进入初始化页面
<img src="./images/1.0.0/chushihua-1.png" width="900px"></img>

- 初始化菜单页面包含两个按钮
【新增】点击新增进入项目组新增页面
【重新执行初始化】根据选中的项目组重新执行初始化任务
<img src="./images/1.0.0/chushihua-2.png" width="900px"></img>

- 进入新增页面，填写项目基本信息
<img src="./images/1.0.0/chushihua-3.png" width="900px"></img>
- 其中组名要和代码仓库关联，所以只支持小写英文
<img src="./images/1.0.0/chushihua-4.png" width="900px"></img>

- 最下方是命名空间设定，主要用于docker镜像发布
<img src="./images/1.0.0/chushihua-5.png" width="900px"></img>
<img src="./images/1.0.0/chushihua-6.png" width="900px"></img>

- 填写完基本信息之后点击下一步进入用户信息设置页面
<img src="./images/1.0.0/chushihua-7.png" width="900px"></img>

- 点击新增按钮查询用户
<img src="./images/1.0.0/chushihua-8.png" width="900px"></img>

- 右上方筛选框可筛选用户
<img src="./images/1.0.0/chushihua-9.png" width="900px"></img>

- 点击确认添加用户
<img src="./images/1.0.0/chushihua-10.png" width="900px"></img>

- 点击下一步进入消息通知页面
<img src="./images/1.0.0/chushihua-11.png" width="900px"></img>

- 通知类型有邮件和钉钉两种，若选择钉钉需要设置机器人webhook信息
<img src="./images/1.0.0/chushihua-12.png" width="900px"></img>

- 页面下方为具体的通知类型开关
<img src="./images/1.0.0/chushihua-13.png" width="900px"></img>

- 点击下一步进入项目信息设置页面（也可以直接点保存跳过此步骤，在项目设置菜单里进行设置）
<img src="./images/1.0.0/chushihua-14.png" width="900px"></img>

- 点击新增按钮新增项目
<img src="./images/1.0.0/chushihua-15.png" width="900px"></img>

- 填写项目基本信息，其中源码仓库地址、构建文件（pom\csproj等）路径、k8s参数有具体默认值
<img src="./images/1.0.0/chushihua-16.png" width="900px"></img>
<img src="./images/1.0.0/chushihua-17.png" width="900px"></img>

- 点击确定保存项目
<img src="./images/1.0.0/chushihua-18.png" width="900px"></img>

- 最后点击保存按钮完成项目组初始化
<img src="./images/1.0.0/chushihua-19.png" width="900px"></img>

- 回到项目列表页面可以看到刚刚添加的项目组记录
<img src="./images/1.0.0/chushihua-20.png" width="900px"></img>

## 用户指南

**权限认证**

EazyBuilder支持本地认证与Ldap认证，初始密码admin/admin123


LDAP认证需要手工开启，配置项（jenkins-cleint.yml）如下：
```yaml
ldap:
enable: true
url: "ldap://xxxxx:389"
base: "DC=xxx,DC=com"
userDn: "xxxxi@xxxx.com"
userPwd: "xxxx"
referral: ignore
domainName: "%s@xxxx.com"
```


## 控制台手册
EazyBuilder控制台主要旨在于增强持续集成流水线的自动化编排，以便进一步帮助用户降低持续集成流水线的开发的成本，将提供包括下列基本功能:

**项目总览**

展示项目组的项目数量，代码总行数等度量数据
<img src="./images/1.0.0/xiangmuzonglan.png" width="900px"></img>

**项目质量管理**



- **质量汇总报表**

统计项目组当前不符合规范的代码数量
<img src="./images/1.0.0/xiangmuzhiliang.png" width="900px"></img>

- **工程项目分组**

展示项目组下所有的工程数量
<img src="./images/1.0.0/gongchengxiangmu.png" width="900px"></img>

- **定期质量报告发送设置**

订阅质量报表
<img src="./images/1.0.0/zhiliangbaobiao.png" width="900px"></img>

- **质量预警规则设置**

设置发送质量报表的阈值
<img src="./images/1.0.0/baobiaoyuzhi.png" width="900px"></img>

- **门禁设置**

设置质量门禁详情
<img src="./images/1.0.0/zhiliangmenjin.png" width="900px"></img>


- **开发者活动统计**

统计开发人员提交代码的日志
<img src="./images/1.0.0/huodongtongji.png" width="900px"></img>

**系统管理**


- **项目组初始化**

使用此菜单，可快速创建工程流水线，包含基本信息，用户信息，消息通知，项目信息

**基本信息**

<img src="./images/1.0.0/jibenxinxi.png" width="900px"></img>

**用户信息**

<img src="./images/1.0.0/yonghuxinxi.png" width="900px"></img>

**消息通知**

<img src="./images/1.0.0/xiaoxitongzhi.png" width="900px"></img>

**项目信息**

<img src="./images/1.0.0/xiangmuxinxi.png" width="900px"></img>



- **用户设置**

设置项目组包含的用户
<img src="./images/1.0.0/yonghushezhi.png" width="900px"></img>


- **事件设定**

配置不同的环节，对应的构建过程
<img src="./images/1.0.0/shijiansheding.png" width="900px"></img>

<img src="./images/1.0.0/shijiansheding2.png" width="900px"></img>





- **Docker镜像仓库**

配置私有镜像仓库，流水线生成的镜像，会往此仓库推送
<img src="./images/1.0.0/docker.png" width="900px"></img>



- **系统参数设置**

设置系统全局参数，配合后端源码使用
<img src="./images/1.0.0/setting.png" width="900px"></img>

- **部署主机管理**

如果不采用kubernetes部署，可以单独配置应用部署的服务器，一个项目组可以配置多台
<img src="./images/1.0.0/host.png" width="900px"></img>

- **API接入认证**

配置API密钥，提供给第三方系统对接使用
<img src="./images/1.0.0/api.png" width="900px"></img>


- **源码仓库活动分析设置**

配置代码仓库，系统会定时同步代码仓库的活动日志
<img src="./images/1.0.0/gitlab.png" width="900px"></img>


- **操作记录查询**

查看用户在系统的操作日志
<img src="./images/1.0.0/log.png" width="900px"></img>


- **项目组资源设置**

配置jenkins,sonarqube等第三方插件，项目组可以设置多个，系统采用随机算法去调用
<img src="./images/1.0.0/resource.png" width="900px"></img>


**项目组设置**

配置项目组的质量门禁
<img src="./images/1.0.0/xiangmuzushezhi.png" width="900px"></img>

**项目工程设置**

可以不采用初始化，直接在此页面设置工程流水线
<img src="./images/1.0.0/gongchengshezhi.png" width="900px"></img>

**流水线管理**

- **构建过程定义**

设置流水线包含的步骤
<img src="./images/1.0.0/goujianguocheng.png" width="900px"></img>

- **运行流水线**

手工触发流水线执行，也可以查看流水线最近一次的构建日志
<img src="./images/1.0.0/yunxingliushuixian.png" width="900px"></img>

- **历史构建记录**

流水线构建日志
<img src="./images/1.0.0/goujianrizhi.png" width="900px"></img>

- **定时自动构建**

设置定时任务，自动执行流水线
<img src="./images/1.0.0/dingshigoujian.png" width="900px"></img>


