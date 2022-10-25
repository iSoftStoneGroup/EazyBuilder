### 禅道钩子脚本（pre-receive）一些说明：



总体分为三部分，

1. 变量定义部分

2. 校验部分：代码提交前的合规校验&代码扫描

3. 初始化入口

   

#### 172-225行是一些入口脚本

首先176行根据参数值判断是否为命令行模式。如果真则直接进行代码扫描和合规性校验。

188-204行是判断如果分支名以判断分支名称如果是“test"、"TEST"、"RELEASE*"、"release"*则不做处理。直接跳过。

205-207行调用自己写的微服务进行一些分支检测的逻辑。这块可以自己实现，主要是结合自己的业务逻辑去做一些校验。



#### 4-111行是validate_ref() 代码提交前的合规校验的一些逻辑

首先获取当前所有的提交注释，获取一些提交用户信息（作者和提交人信息）

32行判断是不是合并操作，如果是则退出循环校验。

如39行所示，将提交人信息，分支名称，提交信息。项目等信息发送到微服务中做校验。比如这里可以进行检测提交人是否是公司账号，防止有不合法账号提交信息。

```shell
result=`curl -H "Content-Type: application/json" -X POST -d "{\"userName\":\"$committer\",\"branchName\":\"$branchName\",\"notes\":\"$note\",\"projectPath\":\"$GL_PROJECT_PATH\",\"commit\":\"$s\"}" http://XX:XX/XXXX/preReceive`

```


55-63行根据提交信息，查找出本次提交的有关的".sql"结尾的sql文件，和以一些“properties”或者“.yml”等结尾的配置文件。

64-77

遍历本次所有提交的sql文件。获取文件内容，文件名称等信息。然后在72行将sql文件的发送到后台进行一些校验和钩子处理（比如此处可以进行一些sql语法检测及优化等一些操作）

46-95行

当前部分是对配置文件的一些自动发布到配置中心等一些hook处理。获取文件的内容，分支名，用户名等信息发送到后台微服务做一些校验或者后置处理。如果不合规则返回结果，中断执行。

98-110行是对提交信息如果为merge*的一些处理，这里使用标识，分别以不同标识去执行部署或者执行ci流水线代码质量扫描一些信息



#### 112-168行是代码扫描的一些逻辑

定义一些java_home等变量信息，因为后面执行阿里的p3c代码扫描会使用到。判断如果是初次提交则跳过校验。

136-166行判断如果不是MR操作，则查找出所有变化的".java"文件，将使用阿里的p3c工具进行扫描。

155行是调用后台微服务，对扫描结果进行合规性校验。如果不合规则中断执行，且输出扫描数据。
```shell
check_result=`curl -H "Content-Type: application/json" -X POST -d "{\"baseXml\":\"$baseXml\",\"group\":\"$groupName\"}" http://xx:xx/xxxx/`
```



#### 注意：

使用到的p3c-pmd-2.1.1-jar-with-dependencies.jar 可以[github地址](https://github.com/pmd/pmd)中下载。

一些curl调用，需要自己实现逻辑。样例已做脱敏处理，这里具体不做展示，根据实际需求可以做扩展。



#### 参考：

GitLab 钩子使用说明：https://docs.gitlab.com/ee/user/project/repository/mirror/bidirectional.html#prevent-conflicts-by-using-a-pre-receive-hook

阿里p3c扫描工具：https://github.com/alibaba/p3c

PMD - 源代码分析器： https://github.com/pmd/pmd









