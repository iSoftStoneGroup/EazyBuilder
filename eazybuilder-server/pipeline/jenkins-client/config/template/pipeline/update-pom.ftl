<#if project.profile?? && project.profile.release >
                        stage('update pom version') {
                            steps{
                            echo '========update pom version start========'
                              <#if release.pomVersion?? && release.pomVersion !="">

                             script {

                                     echo 'begin update pom version '
                                     sh 'mvn versions:set -Dmirror.url=${mirrorUrl} -DnewVersion=${release.pomVersion}'
                                     sh 'mvn -N versions:update-child-modules -Dmirror.url=${mirrorUrl}'
                                     echo 'end update pom version'
                                     sh 'git commit -m '[FEATURE] 更新pom 版本'
                                     sh 'git push https://${project.scm.user}:${project.scm.password}@${project.scm.url} –all'
                             }
                             <#else>
                                 script {
                                 echo 'update pom version is null'
                                 }
                             </#if>
                             echo '========update pom version end========'
                            }
                        }
</#if>