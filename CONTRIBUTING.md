我们非常欢迎您的贡献和加入，无论是微不足道的清理或大的新功能。我们希望为每个编程语言提供高质量、有良好文档的代码。
这也不是代码是唯一有贡献项目的方式。我们非常重视文档、与其他项目的集成，并欣然接受这些方面的改进。

**贡献代码**

**贡献代码须知**

请贡献代码时候，请先确认和检查以下内容：
EazyBuilder使用阿里p3c作为代码规约， 可遵照阿里p3c进行开发，并根据指引设置IDE的codeStyle及校验插件。
如果变化不大，请编写一些覆盖新功能的单元测试。
如果你正在引入一个全新的特性或API，那么首先启动wiki并在基本设计上达成共识，再开始投入。

**贡献流程**

这是贡献者的大致工作流程：
1. fork当前存储github库。
1. 创建一个分支，作为贡献的基础，这通常是develop分支。
1. 做出一些变更提交。
1. 确保提交消息的格式正确（见下文）。
1. 推送变更到你的fork仓库中。
1. 按照拉取请求模板中的清单进行操作。
1. 在发送拉取请求之前，请将您的fork仓库与远程存储库同步，这将使您的拉取请求变得简单明了。详情见下面的**贡献流程**


**贡献文档**

**贡献文档须知**

请贡献文档时候，请先确认和检查以下内容：
1. 已确认过文档确实有误或存在缺失。
1. 熟悉Markdown 。
1. 熟悉docsite ，至少能够根据官方文档README.md 的引导完成本地调试

**成为提交者**

我们会积极纳入新的贡献者。我们更关注的是一系列的持续贡献，良好的品味和对项目维护的持续兴趣。如果你想成为一个提交者（Committer），请让一个现有的提交者(Committer)知道，他们会帮助你通过贡献加入我们。
现在，我们有几个重要的贡献点：
Wiki & JavaDoc
EazyBuilder Console
EazyBuilder CI(java)

**前提**

如果你想贡献以上的项，请你必须遵守我们的一些先决条件：
可读性，一个API必须具有JavaDoc，一些非常重要的方法也必须有JavaDoc。
可测性，关于测试过程的单元测试覆盖率（80%）
可维护性，可满足我们的代码规约 ，以及至少3个月的更新频率
可部署性，我们可以鼓励您部署到maven repository


## 贡献流程

**EazyBuilder贡献流程**

此贡献流程适用于所有的EazyBuilder社区内容。
以下以贡献EazyBuilder为例，详细说明贡献流程。
1. fork eazybuilder/EazyBuilder项目到您的github库
2. 克隆或下载您fork的EazyBuilder代码仓库到您本地
git clone ${your fork EazyBuilderrepo address}

cd EazyBuilder
3. 添加eazybuilder/EazyBuilder仓库为upstream仓库
git remote add upstream https://github.com/eazybuilder/eazybuilder.git

git remote -v 

    origin	   ${your fork EazyBuilderrepo address} (fetch)
    origin	   ${your fork EazyBuilderrepo address} (push)
    upstream	https://github.com/eazybuilder/eazybuilder.git (fetch)
    upstream	https://github.com/eazybuilder/eazybuilder.git (push)
    
git fetch origin
git fetch upstream
4. 选择一个开发的基础分支，通常是upstream/develop，并基于此创建一个新的分支
(从远程仓库拉取分支到本地）
git checkout -b upstream-develop upstream/develop

(从本地分支创建开发分支, 通常以该PR对应的issue号作为开发分支名）
git checkout -b develop-issue#${issue-number}

5. 在本地新建的开发分支上进行各种修改
首先请保证您阅读并正确设置EazyBuildercode style, 相关内容请阅读阿里P3C代码规约 。
修改时请保证该分支上的修改仅和issue相关，并尽量细化，做到一个分支只修改一件事，一个PR只修改一件事。
同时，您的提交记录请尽量使用英文描述，主要以谓 + 宾进行描述，如：Fix xxx problem/bug。少量简单的提交可以使用For xxx来描述，如：For codestyle。 如果该提交和某个ISSUE相关，可以添加ISSUE号作为前缀，如：For #10000, Fix xxx problem/bug。
6. Rebase 基础分支和开发分支
您修改的时候，可能别人的修改已经提交并被合并，此时可能会有冲突，这里请使用rebase命令进行合并解决，主要有2个好处：
1.您的提交记录将会非常优雅，不会出现Merge xxxx branch 等字样
2.rebase后您分支的提交日志也是一条单链，基本不会出现各种分支交错的情况，回查时更轻松
git fetch upstream

git rebase -i upstream/develop

OR
git checkout upstream-develop
git pull 
git checkout develop-issue#${issue-number}
git rebase -i upstream-develop
如果您使用的是Intellij IDEA，建议使用IDE的版本管理模块，有可视化界面，更方便解决冲突和squash操作
7. 将您开发完成rebase后的分支，上传到您fork的仓库
git push origin develop-issue#${issue-number}
8. 按照拉取请求模板中的清单创建Pull Request
拉取请求模板
EazyBuilder社区将会Review您的Pull Request，并可能提出修改意见，您可以根据修改意见回到步骤5进行修改，并使用步骤6进行重新提交。
如果您再次提交时提示您存在提交记录冲突，这是因为您修改期间，有其他的修改合并到了基础分支，您rebase后，commit ID发生了变化，此时需要force push 到您的fork分支上即可
9. 如果没有问题，EazyBuilder社区将会把您的修改合并到基础分支中，恭喜您成为EazyBuilder的官方贡献者。


**如何提交问题报告**

如果EazyBuilders项目的任何部分存在问题或文档问题，请通过opening an issue(https://github.com/eazybuilder/eazybuild/issues/new) 告诉我们。我们非常认真地对待错误和错误，在产品面前没有不重要的问题。不过在创建错误报告之前，请检查是否存在报告相同问题的issues。
为了使错误报告准确且易于理解，请尝试创建以下错误报告：

1. 具体到细节。包括尽可能多的细节：哪个版本，什么环境，什么配置等。如果错误与运行EazyBuilders服务器有关，请附加EazyBuilders日志（具有EazyBuilders配置的起始日志尤为重要）。
1. 可复现。包括重现问题的步骤。我们理解某些问题可能难以重现，请包括可能导致问题的步骤。如果可能，请将受影响的EazyBuilders数据目录和堆栈strace附加到错误报告中。
1. 不重复。不要复制现有的错误报告。

在创建错误报告之前，最好阅读下Elika Etemad关于提交好错误报告的文章，相信 会给你启发。
我们可能会要求您提供更多信息以查找错误。将关闭重复的错误报告。

**如何提交安全问题**

如果您发现EazyBuilders项目中存在安全问题，请通过邮件（） 告知我们。


# 社区

**联系我们**

- Gitter：EazyBuilder 用于社区消息传递、协作和发现的 IM 工具。
- Twitter：在 Twitter 上关注最新的 EazyBuilder 新闻。
- 微博：关注微博（中国版推特）最新的EazyBuilder新闻。
- 电子邮件组：
