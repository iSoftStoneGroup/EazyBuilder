<#if project.profile?? && project.profile.addTag>

//基于release分支，打标签                        
                        stage('git tag') {
                            steps{
                             echo '========git tag start========'
                              <#if project.tagVersion?? && project.tagVersion !="">
                             script {
                                     echo 'begin git tag'

                                       sh 'git config --global user.email 395352154@qq.com'
                                       sh 'git config --global user.name develop1'
                                     
                                  <#if project.tagDetail?? && project.tagDetail !="">

                                      sh 'git tag -a "${project.tagVersion}" -m "Auto Create By Pipeline ${project.tagDetail}"'
                                  <#else>
                                      sh 'git tag ${project.tagVersion}'
                                  </#if>
                                   sh 'git push origin  --tags'

                                  echo 'end git tag'
                             }
                             <#else>
                                 script {
                                 echo 'git tag version is null'
                                 }
                             </#if>
                            echo '========git tag end========'

                           }
                        }
</#if>