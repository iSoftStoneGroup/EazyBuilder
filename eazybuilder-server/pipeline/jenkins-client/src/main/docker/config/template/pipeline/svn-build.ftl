<#if project.projectType??&&project.projectType=="npm" && !project.profile.skipMvnBuild>
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
            <#elseif project.legacyProject && project.profile?? && buildParam?? && buildParam != "" && buildParam?starts_with("ant ") && !project.profile.skipMvnBuild>
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
                            script{
                                def out = sh script: 'java -jar ci-tool/lib/legacy-ant-adapter-jar-with-dependencies.jar ./ <#if !project.eazybuilderStyleProject && project.srcPath?? && project.srcPath!="">${project.srcPath}</#if> <#if !project.eazybuilderStyleProject && project.libPath?? && project.libPath!="">${project.libPath}</#if> <#if !project.eazybuilderStyleProject && project.codeCharset?? && project.codeCharset!="">${project.codeCharset}</#if> <#if project.jdk?? && project.jdk!="">${project.jdk}</#if>', returnStdout: true
                                println(out)
                            }
                        }
                }
            <#elseif !project.profile.skipMvnBuild>
                    <#if project.legacyProject>
                    stage('convert legacy project'){
                        steps {
                            script{
                                def out = sh script: 'java -jar ci-tool/lib/legacy-ant-adapter-jar-with-dependencies.jar ./ <#if !project.eazybuilderStyleProject && project.srcPath?? && project.srcPath!="">${project.srcPath}</#if> <#if !project.eazybuilderStyleProject && project.libPath?? && project.libPath!="">${project.libPath}</#if> <#if !project.eazybuilderStyleProject && project.codeCharset?? && project.codeCharset!="">${project.codeCharset}</#if> <#if project.jdk?? && project.jdk!="">${project.jdk}</#if>', returnStdout: true
                                println(out)
                            }
                        }
                     }
                    </#if>
                    <#if project.legacyProject && project.eazybuilderEjbProject>
                    //maven build
                    stage('maven build') {
                        steps {
                        echo '========maven build start========'
                           //get svn revision and build
                           script{
                               <#if project.profile?? && buildParam?? && buildParam != "" >
                                    sh script: '${buildParam} -Dmaven.test.skip=true -Dmaven.repo.local=/usr/share/maven-repo/teams/${project.team.id}',returnStdout: false
                               <#else>
                                    sh script: 'mvn <#if !project.legacyProject && project.pomPath?? && project.pomPath !="">-f ${project.pomPath} </#if>clean <#if project.legacyProject>compile<#else>install</#if> -Dmaven.test.skip=true -Dmaven.repo.local=/usr/share/maven-repo/teams/${project.team.id}',returnStdout: false
                               </#if>
                           }
                           
                        }
                        echo '========maven build end========'
                    }
                    <#else>
                    //maven build
                    stage('maven build') {
                        steps {
                           //upgrade local svn files
                        echo '========maven build start========'
                           sh ''' svn upgrade -q '''  
                                
                           //get svn revision and build
                           script{
                               def revision = sh(
                                    script: "svn info --show-item last-changed-revision",
                                    returnStdout: true
                               )
                               <#if project.profile?? && buildParam?? && buildParam != "" >
                                    sh script: '${buildParam} -Dmaven.test.skip=true -Dmaven.repo.local=/usr/share/maven-repo/teams/${project.team.id} -Denv.SVN_REVISION='+revision,returnStdout: false
                               <#else>
                                    sh script: 'mvn <#if !project.legacyProject && project.pomPath?? && project.pomPath !="">-f ${project.pomPath} </#if>clean <#if project.legacyProject>compile<#else>install</#if> -Dmaven.test.skip=true -Dmaven.repo.local=/usr/share/maven-repo/teams/${project.team.id} -Denv.SVN_REVISION='+revision,returnStdout: false
                               </#if>
                           }
                        echo '========maven build end========'
                        }
                    }
                    </#if>
            </#if>