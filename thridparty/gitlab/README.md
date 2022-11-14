# [中文说明](./README_CN.md)
### GitLab hook script (pre receive):
It is generally divided into three parts,
1. Variable definition
2. Verification part: compliance verification before code submission&code scanning
3. Initialization entry
#### Lines 172-225 are some entry scripts
First, line 176 determines whether it is command line mode according to the parameter value. If it is true, code scanning and compliance verification will be performed directly.
Line 188-204 is used to judge whether the branch name is "test", "TEST", "RELEASE *" or "release" *. Skip it directly.
Lines 205-207 call their own written microservices for some branch detection logic. This can be implemented by yourself, mainly by combining your own business logic.
#### Line 4-111 is validate_ Some logics of compliance verification before submitting ref() code
First, obtain all the current submission comments, and obtain some submission user information (author and submitter information)
32 lines to determine whether it is a merge operation. If it is, it will exit circular verification.
As shown in line 39, the submitter information, branch name and submitter information will be submitted. Items and other information are sent to the microservice for verification. For example, you can check whether the submitter is a company account to prevent illegal accounts from submitting information.
```shell
result=`curl -H "Content-Type: application/json" -X POST -d "{\"userName\":\"$committer\",\"branchName\":\"$branchName\",\"notes\":\"$note\",\"projectPath\":\"$GL_PROJECT_PATH\",\"commit\":\"$s\"}"  http://XX:XX/XXXX/preReceive `
```
Lines 55-63 find out the relevant sql files ending in ". sql" and configuration files ending in "properties" or ". yml" according to the submission information.
64-77
Traverse all submitted sql files this time. Get the file content, file name and other information. Then send the SQL file to the background in line 72 for some checksum and hook processing (you can perform some operations such as SQL syntax detection and optimization here)
46-95 rows
The current part is to automatically publish some configuration files to the configuration center and other hooks. Get the file content, branch name, user name and other information and send it to the background microservice for some verification or post-processing. If the result is returned in case of irregularity, the execution is interrupted.
Lines 98-110 are used to process the submitted information if it is merge *. Here, we use different identifiers to deploy or perform ci pipeline code quality scanning for some information
#### Lines 112-168 are some logic for code scanning
Define some java_ Home and other variable information will be used in the following Alibaba p3c code scanning. It is judged that if it is the initial submission, the verification will be skipped.
If the 136-166 line is not an MR operation, check out all the changed ". java" files and scan them with Alibaba's p3c tool.
Line 155 calls the background microservice to verify the compliance of the scanning results. If the execution is interrupted due to irregularities, the scan data is output.
```shell
check_ result=`curl -H "Content-Type: application/json" -X POST -d "{\"baseXml\":\"$baseXml\",\"group\":\"$groupName\"}"  http://xx:xx/xxxx/ `
```
#### Note:
Used p3c-pmd-2.1.1-jar with dependencies Jar can [github address]（ https://github.com/pmd/pmd ）Download from.
Some curl calls need to implement their own logic. The sample has been desensitized. It will not be shown here, but can be expanded according to actual needs.
#### Reference:
GitLab Hook Instructions: https://docs.gitlab.com/ee/user/project/repository/mirror/bidirectional.html#prevent -conflicts-by-using-a-pre-receive-hook
Alibaba p3c scanning tool: https://github.com/alibaba/p3c
PMD - Source Code Analyzer: https://github.com/pmd/pmd
