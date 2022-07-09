<#--执行SQL兼容性检查，目前只支持mybatis/ibatis sqlmapper xml文件-->
<#if !project.profile?? || !project.profile.skipSqlScan>
  <#if project.projectType !='npm'>
                stage('SQL Scan'){
                   steps {
                         echo '========SQL Scan start========'
                         script{
                                    def out = sh script: 'java -jar ci-tool/lib/sql-extract-jar-with-dependencies.jar ./ ${project.name}', returnStdout: true
                                    println(out)
                         }
                         echo '========SQL Scan end========'
                   }
                }
  </#if>
</#if>