<#if project.projectType??&&project.projectType=="npm">
                stage('npm build') {
                        steps {
                           //get svn revision and build
echo '========npm build start========'
                           script{
                                <#if project.profile?? && buildParam?? && buildParam != "" >
                                sh script: '${buildParam}',returnStdout: false
                                <#else>
                                sh script: 'npm run build',returnStdout: false
                                </#if>
                           }
echo '========npm build end========'
                        }
                }
<#elseif project.legacyProject && project.profile?? && buildParam?? && buildParam != "" && buildParam?starts_with("ant ")>
                //build ANT project 
                stage('ant build') {
                        steps {
echo '========ant build start========'
                           //get svn revision and build
                           script{
                                sh script: '${buildParam}',returnStdout: false
                           }
echo '========ant build end========'
                        }
                }
                //convert to maven project so we can scan it 
                stage('convert legacy project'){
                        steps {
echo '========convert legacy project start========'
                            script{
                                def out = sh script: 'java -jar ci-tool/lib/legacy-ant-adapter-jar-with-dependencies.jar ./ <#if !project.eazybuilderStyleProject && project.srcPath?? && project.srcPath!="">${project.srcPath}</#if> <#if !project.eazybuilderStyleProject && project.libPath?? && project.libPath!="">${project.libPath}</#if> <#if !project.eazybuilderStyleProject && project.codeCharset?? && project.codeCharset!="">${project.codeCharset}</#if> <#if project.jdk?? && project.jdk!="">${project.jdk}</#if>', returnStdout: true
                                println(out)
                            }
echo '========convert legacy project end========'
                        }
                }
<#else>
                    <#if project.legacyProject>
                    stage('convert legacy project'){
                        steps {
                    echo '========convert legacy project start========'
                            script{
                                def out = sh script: 'java -jar ci-tool/lib/legacy-ant-adapter-jar-with-dependencies.jar ./ <#if !project.eazybuilderStyleProject && project.srcPath?? && project.srcPath!="">${project.srcPath}</#if> <#if !project.eazybuilderStyleProject && project.libPath?? && project.libPath!="">${project.libPath}</#if> <#if !project.eazybuilderStyleProject && project.codeCharset?? && project.codeCharset!="">${project.codeCharset}</#if> <#if project.jdk?? && project.jdk!="">${project.jdk}</#if>', returnStdout: true
                                println(out)
                            }
                    echo '========convert legacy project end========'
                        }
                     }
                    </#if>
                    <#if project.legacyProject && project.eazybuilderEjbProject>
                    //maven build
                    stage('maven build') {
                        steps {
                      echo '========maven build start========'
                           script{
                               <#if project.profile?? && buildParam?? && buildParam != "" >
                                    sh script: '${buildParam} -Dmaven.test.skip=true -Dmirror.url=${mirrorUrl} -Dmaven.repo.local=/usr/share/maven-repo/teams/${project.team.id}',returnStdout: false
                               <#else>
                                    sh script: 'mvn <#if !project.legacyProject && project.pomPath?? && project.pomPath !="">-f ${project.pomPath} </#if>clean <#if project.legacyProject>compile<#else>install</#if> -Dmaven.test.skip=true -Dmirror.url=${mirrorUrl} -Dmaven.repo.local=/usr/share/maven-repo/teams/${project.team.id}',returnStdout: false
                               </#if>
                           }
                       echo '========maven build end========'
                        }
                    }
                    <#elseif project.projectType=='gradle'>
                      //gradle build
                        stage('gradle build') {
                            steps {
                        echo '========gradle build start========'
                                script{
                                   def revision=sh(returnStdout: true, script: 'git rev-parse HEAD')
                                   <#if project.profile?? && buildParam?? && buildParam != "" >
                                        sh script: '${buildParam} -Dgradle.user.home=/usr/share/maven-repo/teams/${project.team.id} -Denv.GIT_COMMIT='+revision,returnStdout: false
                                   <#else>
                                        sh script: 'gradle <#if !project.legacyProject && project.pomPath?? && project.pomPath !="">-b ${project.pomPath} </#if> build -Dgradle.user.home=/usr/share/maven-repo/teams/${project.team.id} -Denv.GIT_COMMIT='+revision,returnStdout: false
                                   </#if>
                                }
                            }
                        echo '========gradle build end========'
                       }
                    <#else>
                       //maven build
                        stage('maven build') {
                            steps {
                    echo '========maven build start========'
                                script{
                                   def revision=sh(returnStdout: true, script: 'git rev-parse HEAD')
                                   <#if project.profile?? && buildParam?? && buildParam != "" >
                                        sh script: '${buildParam} -Dmaven.test.skip=true -Dmaven.repo.local=/usr/share/maven-repo/teams/${project.team.id} -Denv.GIT_COMMIT='+revision,returnStdout: false
                                   <#else>
                                        sh script: 'mvn <#if !project.legacyProject && project.pomPath?? && project.pomPath !="">-f ${project.pomPath} </#if>clean <#if project.legacyProject>compile<#else>install</#if> -Dmaven.test.skip=true -Dmirror.url=${mirrorUrl} -Dmaven.repo.local=/usr/share/maven-repo/teams/${project.team.id} -Denv.GIT_COMMIT='+revision,returnStdout: false
                                   </#if>
                                }
                    echo '========maven build end========'
                            }
                        }
                    </#if>
 </#if>