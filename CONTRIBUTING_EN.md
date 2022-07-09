We very much welcome your contributions and additions, whether it's a trivial cleanup or a big new feature. We want to provide high-quality, well-documented code for every programming language.
Nor is code the only way to contribute to a project. We take documentation, integration with other projects very seriously, and welcome improvements in these areas.

**Contributed code**

**Contribution code instructions**

When contributing code, please confirm and check the following:
EazyBuilder uses Ali p3c as the code specification, can follow Ali p3c for development, and set the IDE's codeStyle and verification plug-ins according to the guidelines.
If the changes are small, write some unit tests that cover the new functionality.
If you are introducing a brand new feature or API, start the wiki first and agree on the basic design before you start investing.

**Contribution Process**

Here's the rough workflow for contributors:
1. Fork the current storage github repository.
1. Create a branch as a basis for contributions, this is usually the develop branch.
1. Make some change commits.
1. Make sure the commit message is properly formatted (see below).
1. Push the changes to your fork repository.
1. Follow the checklist in the pull request template.
1. Synchronize your fork repository with the remote repository before sending a pull request, this will make your pull request simple and straightforward. See **Contribution Process** below for details


**Contributed Documentation**

**Contribution Documentation Notes**

When contributing documentation, please confirm and check the following:
1. The document has been confirmed to be in error or missing.
1. Familiar with Markdown.
1. Familiar with docsite, at least be able to complete local debugging according to the guidance of the official document README.md

**Become a committer**

We actively include new contributors. We are more concerned with a series of ongoing contributions, good taste and continued interest in the maintenance of the project. If you want to become a Committer, please let an existing Committer know and they will help you join us by contributing.
Now, we have several important contribution points:
Wiki & JavaDoc
EazyBuilder Console
EazyBuilder CI (java)

**Premise**

If you want to contribute to the above items, please follow some of our prerequisites:
For readability, an API must have JavaDoc, and some very important methods must also have JavaDoc.
Testability, unit test coverage (80%) regarding the test process
Maintainability to meet our code specifications and update frequency of at least 3 months
Deployability, we can encourage you to deploy to maven repository


## Contribution process

**EazyBuilder Contribution Process**

This contribution process applies to all EazyBuilder community content.
The following takes contribution EazyBuilder as an example to describe the contribution process in detail.
1. Fork eazybuilder/EazyBuilder project to your github repository
2. Clone or download your fork EazyBuilder code repository to your local
git clone ${your fork EazyBuilderrepo address}

cd EazyBuilder
3. Add eazybuilder/EazyBuilder repository as upstream repository
git remote add upstream https://github.com/eazybuilder/eazybuilder.git

git remote -v

    origin ${your fork EazyBuilderrepo address} (fetch)
    origin ${your fork EazyBuilderrepo address} (push)
    upstream https://github.com/eazybuilder/eazybuilder.git (fetch)
    upstream https://github.com/eazybuilder/eazybuilder.git (push)
    
git fetch origin
git fetch upstream
4. Pick a base branch of development, usually upstream/develop, and create a new branch based on that
(pull the branch from the remote repository to the local)
git checkout -b upstream-develop upstream/develop

(Create a development branch from a local branch, usually with the issue number corresponding to the PR as the development branch name)
git checkout -b develop-issue#${issue-number}

5. Make various modifications on the newly created development branch locally
First of all, please ensure that you read and set the EazyBuildercode style correctly. For related content, please read the Ali P3C Code Specification.
When modifying, please ensure that the modification on the branch is only related to the issue, and try to be as detailed as possible, so that only one thing is modified in a branch, and only one thing is modified in a PR.
At the same time, please use English description as much as possible for your submission record, mainly describe with predicate + object, such as: Fix xxx problem/bug. A small number of simple commits can be described using For xxx, such as: For codestyle. If the commit is related to an ISSUE, you can add the ISSUE number as a prefix, such as: For #10000, Fix xxx problem/bug.
6. Rebase base branch and development branch
When you modify, other people's modifications may have been submitted and merged. At this time, there may be conflicts. Please use the rebase command to merge and resolve. There are two main advantages:
1. Your submission record will be very elegant, and there will be no words such as Merge xxxx branch
2. After rebase, the commit log of your branch is also a single chain, basically there will be no interleaving of various branches, and it is easier to check back
git fetch upstream

git rebase -i upstream/develop

OR
git checkout upstream-develop
git pull
git checkout develop-issue#${issue-number}
git rebase -i upstream-develop
If you are using Intellij IDEA, it is recommended to use the version management module of the IDE, which has a visual interface, which is more convenient for conflict resolution and squash operations
7. Upload the branch you developed after rebase to your fork repository
git push origin develop-issue#${issue-number}
8. Follow the manifest in the pull request template to create a Pull Request
Pull Request Template
The EazyBuilder community will review your Pull Request and may propose revisions. You can go back to step 5 to make revisions and use step 6 to resubmit.
If you are prompted that there is a commit record conflict when you commit again, this is because other modifications have been merged into the base branch during your modification. After you rebase, the commit ID has changed. At this time, you need to force push to your fork branch. Can
9. If there is no problem, the EazyBuilder community will merge your changes into the base branch, congratulations on becoming an official EazyBuilder contributor.


**How ​​to submit a problem report**

If there are issues or documentation issues with any part of the EazyBuilders project, please let us know by opening an issue (https://github.com/eazybuilder/eazybuild/issues/new). We take mistakes and mistakes very seriously and there are no unimportant issues in front of the product. However, before creating a bug report, please check for issues reporting the same issue.
To make your bug report accurate and easy to understand, try creating the following bug report:

1. Be specific to the details. Include as many details as possible: which version, what environment, what configuration, etc. If the error is related to running the EazyBuilders server, please attach the EazyBuilders log (starting logs with EazyBuilders configuration are especially important).
1. Reproducible. Includes steps to reproduce the problem. We understand that some issues may be difficult to reproduce, please include steps that may have caused the issue. If possible, please attach the affected EazyBuilders data directory and stack strace to the bug report.
1. Do not repeat. Do not duplicate existing bug reports.

Before creating a bug report, it is best to read Elika Etemad's article on submitting good bug reports, I believe it will give you inspiration.
We may ask you for more information to find errors. Duplicate bug reports will be closed.

**How ​​to submit a security question**

If you find a security issue in your EazyBuilders project, please let us know by mail().


# Community

**Contact us**

- Gitter: EazyBuilder's IM tool for community messaging, collaboration, and discovery.
- Twitter: Follow the latest EazyBuilder news on Twitter.
- Weibo: Follow the latest EazyBuilder news on Weibo (Chinese version of Twitter).
- Email group: