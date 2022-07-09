<#if project.projectType??&&project.projectType=="npm">
                stage('npm build') {
                        steps {
                           //get svn revision and build
                           script{
                                <#if project.profile?? && buildParam?? && buildParam != "" >
                                sh script: '${buildParam}',returnStdout: false
                                <#else>
                                sh script: 'yarn install && npm run build',returnStdout: false
                                </#if>
                           }
                        }
                }
<#elseif project.projectType??&&project.projectType=="net">
                stage('net build') {
                        steps {
                            echo '========ms build========'
                            sh '''echo `pwd`'''
                            sh '''dotnet publish `pwd`/<#if project.netPath !="">${project.netPath}<#else>src/${project.name}.Api/${project.name}.Api.csproj</#if>  -c Release'''
                        }
                }
<#elseif project.legacyProject && project.profile?? && buildParam?? && buildParam != "" && buildParam?starts_with("ant ")>
                //build ANT project 
                stage('ant build') {
                        steps {
                           //get svn revision and build
                           script{
                                sh script: '${buildParam}',returnStdout: false
                           }
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
                           script{
                            echo '========maven build start========'
                            echo '修饰后的pom.xml'
                            <#if !project.legacyProject && project.pomPath?? && project.pomPath !="">
<#--                            sh script:'cat ${project.pomPath}/pom.xml'-->
                            <#else>
<#--                             sh script:'cat pom.xml'-->
                            </#if>
                            
                   
                               <#if project.profile?? && buildParam?? && buildParam != "" >
                                    sh script: '${buildParam} -Dmaven.test.skip=${project.profile.skipUnitTest ?string("true","false")} -Dmaven.test.failure.ignore=true -Dmaven.repo.local=/usr/share/maven-repo/teams/${project.team.id}',returnStdout: false
                               <#else>
                                    sh script: 'mvn <#if !project.legacyProject && project.pomPath?? && project.pomPath !="">-f ${project.pomPath} </#if>clean <#if project.legacyProject>compile<#else>install</#if> -Dmaven.test.skip=${project.profile.skipUnitTest?string("true","false")} -Dmaven.test.failure.ignore=true -Dmaven.repo.local=/usr/share/maven-repo/teams/${project.team.id}',returnStdout: false
                               </#if>
                             echo '========maven build end========'
                           }
                           
                        }
                    }
                    <#elseif project.projectType=='gradle'>
                      //gradle build
                        stage('gradle build') {
                            steps {
                                script{
                                   echo '========gradle build start========'
                                   def revision=sh(returnStdout: true, script: 'git rev-parse HEAD')
                                   <#if project.profile?? && buildParam?? && buildParam != "" >
                                        sh script: '${buildParam} -Dgradle.user.home=/usr/share/maven-repo/teams/${project.team.id} -Denv.GIT_COMMIT='+revision,returnStdout: false
                                   <#else>
                                        sh script: 'gradle <#if !project.legacyProject && project.pomPath?? && project.pomPath !="">-b ${project.pomPath} </#if> build -Dgradle.user.home=/usr/share/maven-repo/teams/${project.team.id} -Denv.GIT_COMMIT='+revision,returnStdout: false
                                   </#if>
                                    echo '========gradle build end========'
                                }
                            }
                       }
                    <#else>
                    
                    
                      <#if project.profile?? && project.profile.skipMvnBuild>
                       echo '跳出maven build'
                       <#else>
                      
                       //maven build
                        stage('maven build') {
                            steps {
                                script{
                       echo '========maven build start========'
                             echo '修饰前的pom.xml'
                            <#if !project.legacyProject && project.pomPath?? && project.pomPath !="">
<#--                            sh script:'cat ${project.pomPath}/pom.xml'-->
                            <#else>
<#--                             sh script:'cat pom.xml'-->
                            </#if>
                                   def revision=sh(returnStdout: true, script: 'git rev-parse HEAD')
                                   <#if project.profile?? && buildParam?? && buildParam != "" >
                                        sh script: '${buildParam} -Dmaven.test.skip=${project.profile.skipUnitTest?string("true","false")} -Dmaven.test.failure.ignore=true -Dmaven.repo.local=/usr/share/maven-repo/teams/${project.team.id} -Denv.GIT_COMMIT='+revision,returnStdout: false
                                   <#else>
                                        sh script: 'mvn <#if !project.legacyProject && project.pomPath?? && project.pomPath !="">-f ${project.pomPath} </#if>clean org.jacoco:jacoco-maven-plugin:prepare-agent <#if project.legacyProject>compile<#else>install</#if> -Dmaven.test.skip=${project.profile.skipUnitTest?string("true","false")} -Dmaven.test.failure.ignore=true -Dmaven.repo.local=/usr/share/maven-repo/teams/${project.team.id} -Denv.GIT_COMMIT='+revision,returnStdout: false
                                   </#if>
                       echo '========maven build end========'
                                }
                            }
                        }
                       </#if> 
                        
                    </#if>
 </#if>