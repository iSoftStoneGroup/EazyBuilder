<#if project.profile?? && project.profile.createBranch>

//第二步，创建relase分支
                        stage('git create release branch') {
                            steps{
                                 echo '========git create release branch start========'
    <#if project.branchVersion?? && project.branchVersion !="">
                             script {
                                     echo 'begin create release branch'
                                     echo 'RUN git config --global credential.helper store'
<#--                                  <#if project.tagDetail?? && project.tagDetail !="">-->
                                     sh 'git config --global user.email  boot@eazybuilder.com '
                                     sh 'git config --global user.name robot'
                                      sh 'git checkout -B ${project.branchVersion}'
<#--                                      sh 'git commit -m "Auto Create By Pipeline"'-->
                                      sh 'git push origin  ${project.branchVersion}:${project.branchVersion}'
<#--                                  </#if>                                   -->
                                  echo 'end create release branch'
                             }
                             <#else>
                                 script {
                                 echo 'create release branch, tag version is null'
                                 }
                             </#if>
echo '========git create release branch end========'
                         }
                        }
</#if>