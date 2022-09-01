<?xml version='1.1' encoding='UTF-8'?>
<flow-definition plugin="workflow-job@2.18">
  <actions/>
  <description></description>
  <keepDependencies>false</keepDependencies>
  <properties>
  </properties>
  <definition class="org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition" plugin="workflow-cps@2.47">
    <!-- 需要参数化的: credentialId,remote -->
    <script>
      <![CDATA[
      pipeline{
        agent <#if !k8sSupport>any<#else><#include "pipeline/k8s-agent.ftl" ></#if>

        stages {
          //update ci-tools必做的步骤，无论任何类型的流水线，这一步都不能少
          stage('update ci-tools'){
            steps {
              echo '========update ci-tools start========'
              sh '''rm -rf ci-tool && mkdir -p ci-tool/lib && cp /opt/ci-tool/lib/*.jar ci-tool/lib/'''
              sh '''echo '${projectJSON}' > ci-project.json'''
              sh '''echo '${pipelineUID}' > pipeline-uuid'''
              sh '''mkdir -p /usr/share/maven-repo/teams/${project.team.id}'''
              sh '''mkdir -p /usr/share/maven-repo/org'''
              sh '''ln -sfn /usr/share/maven-repo/org /usr/share/maven-repo/teams/${project.team.id}/org'''
              echo '========update ci-tools end========'
            }
          }
        
          <#if project.profile?? &&project.deployType?? && project.deployType=='k8s' && project.profile.rollout?string == "true">
          <#include "pipeline/k8s-rollout.ftl" >
         
          <#elseif project.profile??  && project.profile.restartDeploy && project.profile.onlineDeploy>          
           <#include "pipeline/k8s-restart.ftl" >
          <#else>
            <#if project.profile?? && project.profile.skipCloneCode>
          stage('skip clone code'){
            steps {
              echo '跳出下载代码'
            }
          }
          <#if project.projectType??&&project.projectType=='npm'>
          stage('decorate project'){
            //invoke java program
            steps {
              echo '========decorate project start========'
              //update ci-tools必做的步骤，无论任何类型的流水线，这一 步都不能少,执行maven build命令后，所有的lib会被清理，需要再次安装jar
              sh '''rm -rf ci-tool && mkdir -p ci-tool/lib && cp /opt/ci-tool/lib/*.jar ci-tool/lib/'''
              sh '''echo '${projectJSON}' > ci-project.json'''
              sh '''echo '${pipelineUID}' > pipeline-uuid'''
              sh '''mkdir -p /usr/share/maven-repo/teams/${project.team.id}'''
              sh '''mkdir -p /usr/share/maven-repo/org'''
              sh '''ln -sfn /usr/share/maven-repo/org /usr/share/maven-repo/teams/${project.team.id}/org'''
              script{
                def out = sh script: 'java -Dmaven.local.repo=/usr/share/maven-repo/teams/${project.team.id} -jar ci-tool/lib/buildfile-decorator*.jar node ./<#if !project.legacyProject && project.pomPath?? && project.pomPath !="">${project.pomPath}</#if>', returnStdout: true
                echo '========decorate project end========'
                println(out)

              }
            }
          }
          <#else>
          stage('decorate project'){
            //invoke java program
            steps {
              echo '========decorate project start========'
              //update ci-tools必做的步骤，无论任何类型的流水线，这一 步都不能少,执行maven build命令后，所有的lib会被清理，需要再次安装jar
              sh '''rm -rf ci-tool && mkdir -p ci-tool/lib && cp /opt/ci-tool/lib/*.jar ci-tool/lib/'''
              sh '''echo '${projectJSON}' > ci-project.json'''
              sh '''echo '${pipelineUID}' > pipeline-uuid'''
              sh '''mkdir -p /usr/share/maven-repo/teams/${project.team.id}'''
              sh '''mkdir -p /usr/share/maven-repo/org'''
              sh '''ln -sfn /usr/share/maven-repo/org /usr/share/maven-repo/teams/${project.team.id}/org'''
              script{
                def out = sh script: 'java -Dmaven.local.repo=/usr/share/maven-repo/teams/${project.team.id} -jar ci-tool/lib/buildfile-decorator*.jar maven ./<#if !project.legacyProject && project.pomPath?? && project.pomPath !="">${project.pomPath}</#if>', returnStdout: true
                println(out)

                // echo '修饰后的pom.xml'
                <#if !project.legacyProject && project.pomPath?? && project.pomPath !="">
                <#--sh script:'cat ${project.pomPath}/pom.xml'-->
                <#else>
                // sh script:'cat pom.xml'
                </#if>
              }
              echo '========decorate project end========'
            }
          }
          <#-- OWASP DependencyCheck-->
          <#include "pipeline/dependency-check.ftl" >
          </#if>
         
            <#else>
          stage('checkout from scm') {
            steps {
              echo '========checkout from scm start========'
               echo 'EazybuilderDevOps持续集成平台'
              //check source code from svn
              checkout([$class: 'GitSCM',
              <#if tagName?? >
                  branches: [[name: '${tagName}']],
                <#else>
              branches: [[name: '*/master']],
              </#if>
              doGenerateSubmoduleConfigurations: false,
                      extensions: [[$class: 'SubmoduleOption', disableSubmodules: false, parentCredentials: true, recursiveSubmodules: true, reference: '', trackingSubmodules: true]],
                      submoduleCfg: [],
                      userRemoteConfigs: [[credentialsId: '${credentialsId}',
                      url: '${project.scm.url}']]])
              echo '========checkout from scm end========'
            }
          }

       
          // update pom version
          <#--            <#include "pipeline/update-pom.ftl">-->
          // git tag
          <#include "pipeline/git-release.ftl">
          <#include "pipeline/git-tag.ftl">

          <#-- build project -->
          <#include "pipeline/git-build.ftl" >
          <#if project.projectType??&&project.projectType=='npm'>
          //prepare sonar scan
          stage('decorate project'){
            //invoke java program
            steps {
              echo '========decorate project start========'
              //update ci-tools必做的步骤，无论任何类型的流水线，这一 步都不能少,执行maven build命令后，所有的lib会被清理，需要再次安装jar
                sh '''rm -rf ci-tool && mkdir -p ci-tool/lib && cp /opt/ci-tool/lib/*.jar ci-tool/lib/'''
                sh '''echo '${projectJSON}' > ci-project.json'''
                sh '''echo '${pipelineUID}' > pipeline-uuid'''
                sh '''mkdir -p /usr/share/maven-repo/teams/${project.team.id}'''
                sh '''mkdir -p /usr/share/maven-repo/org'''
                sh '''ln -sfn /usr/share/maven-repo/org /usr/share/maven-repo/teams/${project.team.id}/org'''
              script{
                def out = sh script: 'java -Dmaven.local.repo=/usr/share/maven-repo/teams/${project.team.id} -jar ci-tool/lib/buildfile-decorator*.jar node ./<#if !project.legacyProject && project.pomPath?? && project.pomPath !="">${project.pomPath}</#if>', returnStdout: true
                echo '========decorate project end========'
                println(out)

              }
            }
          }
          <#include "pipeline/sonar-scan.ftl">
          <#else>
          stage('decorate project'){
            //invoke java program
            steps {
              echo '========decorate project start========'
              //update ci-tools必做的步骤，无论任何类型的流水线，这一 步都不能少,执行maven build命令后，所有的lib会被清理，需要再次安装jar
              sh '''rm -rf ci-tool && mkdir -p ci-tool/lib && cp /opt/ci-tool/lib/*.jar ci-tool/lib/'''
              sh '''echo '${projectJSON}' > ci-project.json'''
              sh '''echo '${pipelineUID}' > pipeline-uuid'''
              sh '''mkdir -p /usr/share/maven-repo/teams/${project.team.id}'''
              sh '''mkdir -p /usr/share/maven-repo/org'''
              sh '''ln -sfn /usr/share/maven-repo/org /usr/share/maven-repo/teams/${project.team.id}/org'''
              script{
                def out = sh script: 'java -Dmaven.local.repo=/usr/share/maven-repo/teams/${project.team.id} -jar ci-tool/lib/buildfile-decorator*.jar maven ./<#if !project.legacyProject && project.pomPath?? && project.pomPath !="">${project.pomPath}</#if>', returnStdout: true
                println(out)
                
                            // echo '修饰后的pom.xml'
                            <#if !project.legacyProject && project.pomPath?? && project.pomPath !="">
                            <#--sh script:'cat ${project.pomPath}/pom.xml'-->
                            <#else>
                            // sh script:'cat pom.xml'
                            </#if>                
                
              }
              echo '========decorate project end========'
            }
          }
          
     

          <#-- OWASP DependencyCheck-->
          <#include "pipeline/dependency-check.ftl" >

           <#--单元测试，在sonar流水线之前执行-->
          <#include "pipeline/unit-test.ftl">
          <#-- SQL Compatible check-->
          <#include "pipeline/sql-compatible-check.ftl" >
          <#include "pipeline/sql-remind.ftl" >
          <#include "pipeline/sonar-scan.ftl">
          </#if>
          <#if !project.profile.skipMvnBuild>
          <#include "pipeline/build-docker-image.ftl">
          </#if>
          <#include "pipeline/db-script.ftl">
          <#include "pipeline/update-config.ftl">
          </#if>
          <#include "pipeline/upgrade-docker-image.ftl">
          <#include "pipeline/k8s-compose.ftl">
          <#include "pipeline/k8s-restart.ftl" >
          <#include "pipeline/check-pom.ftl">
          <#include "pipeline/k8s-job-update.ftl">
          <#include "pipeline/createMergeRequest.ftl">
          </#if>
        }
                      
        post {
          always {
            script{
              <#include "pipeline/post-action.ftl">

            }
          }
        }
 
      }

      ]]>
    </script>
    <sandbox>false</sandbox>
  </definition>
  <triggers/>
  <disabled>false</disabled>
</flow-definition>