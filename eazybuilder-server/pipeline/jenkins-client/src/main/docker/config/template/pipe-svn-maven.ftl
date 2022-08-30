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
            stage('checkout from scm') {
                steps {
                       <#if project.legacyProject && project.eazybuilderEjbProject>
                        //ensure project folder exist
                        sh '''mkdir -p eazybuilderEJB && mkdir -p lib'''
                        //check source code from svn
                        checkout([$class: 'SubversionSCM', 
                            additionalCredentials: [], 
                            excludedCommitMessages: '', 
                            excludedRegions: '', 
                            excludedRevprop: '', 
                            excludedUsers: '', 
                            filterChangelog: false, 
                            ignoreDirPropChanges: false, 
                            includedRegions: '', 
                            locations: [[
                                cancelProcessOnExternalsFail: true, 
                                credentialsId: '${credentialsId}',
                                depthOption: 'infinity', 
                                ignoreExternalsOption: true, 
                                local: './eazybuilderEJB',
                                remote: '${project.scm.url}/eazybuilderEJB'
                                ]], 
                            quietOperation: true, 
                            workspaceUpdater: [$class: 'UpdateWithCleanUpdater']])
                        
                        //check lib from svn
                        checkout([$class: 'SubversionSCM', 
                            additionalCredentials: [], 
                            excludedCommitMessages: '', 
                            excludedRegions: '', 
                            excludedRevprop: '', 
                            excludedUsers: '', 
                            filterChangelog: false, 
                            ignoreDirPropChanges: false, 
                            includedRegions: '', 
                            locations: [[
                                cancelProcessOnExternalsFail: true, 
                                credentialsId: '${credentialsId}',
                                depthOption: 'infinity', 
                                ignoreExternalsOption: true, 
                                local: './lib',
                                remote: '${project.scm.url}/lib'
                                ]], 
                            quietOperation: true, 
                            workspaceUpdater: [$class: 'UpdateWithCleanUpdater']])
                       <#else>
                       //check source code from svn
                        checkout([$class: 'SubversionSCM', 
                            additionalCredentials: [], 
                            excludedCommitMessages: '', 
                            excludedRegions: '', 
                            excludedRevprop: '', 
                            excludedUsers: '', 
                            filterChangelog: false, 
                            ignoreDirPropChanges: false, 
                            includedRegions: '', 
                            locations: [[
                                cancelProcessOnExternalsFail: true, 
                                credentialsId: '${credentialsId}',
                                depthOption: 'infinity', 
                                ignoreExternalsOption: true, 
                                local: '.',
                                remote: '${project.scm.url}'
                                ]], 
                            quietOperation: true, 
                            workspaceUpdater: [$class: 'UpdateWithCleanUpdater']])
                       </#if>
                }
            }
            //update ci-tools
            stage('update ci-tools'){
                steps {
                    sh '''rm -rf ci-tool && mkdir -p ci-tool/lib && cp /opt/ci-tool/lib/*.jar ci-tool/lib/'''
                    sh '''echo '${projectJSON}' > ci-project.json'''
                    sh '''echo '${pipelineUID}' > pipeline-uuid'''
                    sh '''mkdir -p /usr/share/maven-repo/teams/${project.team.id}${project.team.id}'''
                    sh '''mkdir -p /usr/share/maven-repo/org'''
                    sh '''ln -sfn /usr/share/maven-repo/org /usr/share/maven-repo/teams/${project.team.id}/org'''
                }
            }
            
            
           <#-- build project -->
            <#include "pipeline/svn-build.ftl" >
            
            <#-- OWASP DependencyCheck-->
            <#include "pipeline/dependency-check.ftl" >
            
            <#-- SQL Compatible check-->
            <#include "pipeline/sql-compatible-check.ftl" >
            <#if project.projectType??&&project.projectType=='npm'>
                //prepare sonar scan
                stage('decorate project'){
                        //invoke java program
                        steps {
                            script{
                                def out = sh script: 'java -Dmaven.local.repo=/usr/share/maven-repo/teams/${project.team.id} -jar ci-tool/lib/buildfile-decorator*.jar node ./<#if !project.legacyProject && project.pomPath?? && project.pomPath !="">${project.pomPath}</#if>', returnStdout: true
                                println(out)
                            }
                        }
                 }
                <#include "pipeline/sonar-scan.ftl">
            <#else>
                    <#include "pipeline/sonar-scan.ftl">
                    stage('decorate project'){
                        //invoke java program
                        steps {
                            script{
                                def out = sh script: 'java -Dmaven.local.repo=/usr/share/maven-repo/teams/${project.team.id} -jar ci-tool/lib/buildfile-decorator*.jar maven ./<#if !project.legacyProject && project.pomPath?? && project.pomPath !="">${project.pomPath}</#if>', returnStdout: true
                                println(out)
                            }
                        }
                     }
                    <#include "pipeline/unit-test.ftl">
                    <#include "pipeline/build-docker-image.ftl">
                    <#include "pipeline/push-war.ftl">
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