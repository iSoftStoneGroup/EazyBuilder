##ANT项目编译期临时转Maven项目

### 目的

CI平台基于Maven插件/自动装饰pom，提供代码质量扫描、依赖扫描等自动化流水线功能；
部分老的工程项目由于历史原因仍在使用ant/人工管理依赖，但这部分工程也想接入CI平台进行提供扫描报告。

本工程旨在提供一种兼容老项目的临时措施，对符合固定规则的ANT工程项目自动生成pom.xml Maven工程核心配置文件

**注意:ANT方式的依赖管理不再推荐使用！CI平台后续所有的扩展功能以Maven工程项目为基准 **

#### 前置条件

- 符合以下标准目录结构

   src/main/java    —— Java源代码  
   src/main/resources   ——  运行期配置文件、资源文件等  
   src/main/webapp      ——  webContent  
   src/test/java       ——   单元测试代码  （可选）
   src/test/resources   ——  单元测试配置文件、资源文件等  （可选）
       
- 依赖包库文件符合以下规则：

   1. 运行期依赖均包含在src/main/webapp/WEB-INF/lib/下   
   2. 编译期依赖均包含在lib/compile/下

- 根目录下存在build.properties，并且其中标注了应用名称和版本号，例如:

    #应用名称
    app.name=ifinance-loan   
    #版本号
    app.version=1.0.0

#### 使用
 
    #需java8
    java -jar legacy-ant-adapter.jar <ant project root path>
    
运行完成将自动在该目录下创建pom.xml，可直接通过mvn clean package打包

#### 原理

将lib库以system scope引入工程，并且添加jar的绝对路径  

例如：

    <dependency>
       <groupId>eazybuilder.legacy</groupId>
       <artifactId>${jar.name}</artifactId>
       <version>${jar.version}</version>
       <scope>system</scope>
       <systemPath>${jar.filePath}</systemPath>
    </dependency>