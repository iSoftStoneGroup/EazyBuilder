<#if project.projectType?? && project.projectType=="npm" && !project.profile.skipMvnBuild>
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
<#elseif project.projectType?? && project.projectType=="net" && !project.profile.skipMvnBuild>
                stage('net build') {
                        steps {
                            echo '========net build start========'
                        <#if referenceSource?? && referenceSource != "" >
                             println('使用自定义NuGet源: ${referenceSource}');
                             sh '''dotnet nuget add source  ${referenceSource}'''
                             sh '''dotnet nuget remove source  nuget.org'''
                             sh '''dotnet nuget list source '''
                        </#if>
                             sh '''echo `pwd`'''
                             sh '''dotnet publish `pwd`/<#if project.netPath !="">${project.netPath}<#else>src/${project.name}.Api/${project.name}.Api.csproj</#if>  -c Release'''
<#--                             <#if project.profile.secondPartySwitch>-->
<#--                             sh '''nuget spec '''-->
<#--                             sh '''nuget pack '''-->
<#--                             sh script: 'dotnet nuget push "*.nupkg"  -k  ${project.profile.secondParty.secondPartyKey} -s ${project.profile.secondParty.secondPartyPath!'http://nexus3.Eazybuilder-devops.cn/repository/ipsa-net-test/'}',returnStdout: false-->
<#--                             </#if>-->
                             echo '========net build end========'
                        }
                }
<#elseif project.legacyProject && project.profile?? && buildParam?? && buildParam != "" && buildParam?starts_with("ant ") && !project.profile.skipMvnBuild && !project.profile.skipMvnBuild>
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
                            sh script:'cat ${project.pomPath}/pom.xml'
                            <#elseif !project.legacyProject&&project.projectType=='java'>
                             sh script:'cat pom.xml'
                            </#if>
                            
                   
                               <#if project.profile?? && buildParam?? && buildParam != "" >
                                    sh script: '${buildParam} -Dmaven.test.skip=${project.profile.skipUnitTest ?string("true","false")} -Dmirror.url=${mirrorUrl} -Dmaven.test.failure.ignore=true -Dmaven.repo.local=/usr/share/maven-repo/teams/${project.team.id}',returnStdout: false
                               <#else>
                                    sh script: 'mvn <#if !project.legacyProject && project.pomPath?? && project.pomPath !="">-f ${project.pomPath} </#if>clean <#if project.legacyProject>compile<#else>install</#if> -Dmaven.test.skip=${project.profile.skipUnitTest?string("true","false")} -Dmirror.url=${mirrorUrl} -Dmaven.test.failure.ignore=true -Dmaven.repo.local=/usr/share/maven-repo/teams/${project.team.id}',returnStdout: false
                               </#if>
<#--                               <#if project.profile?? && project.profile.secondPartySwitch  >-->
<#--                                   sh script: 'mvn deploy -Ddocker.registry.serverId=<#if project.profile.secondParty.secondPartyType =='mavenRelease'>iss-releases</#if> <#if project.profile.secondParty.secondPartyType =='mavenSnapshot'>iss-snapshots</#if> -Dmirror.url=${mirrorUrl}  -Ddocker.registry.username=${project.profile.secondParty.secondPartyUser} -Ddocker.registry.password=${project.profile.secondParty.secondPartyPass}',returnStdout: false-->
<#--                               </#if>-->
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
                                   String buildPrefix=null;
                                   <#if project.profile?? && buildParam?? && buildParam != "" >
                                        buildPrefix="${buildParam}  ";
                                   <#else>
                                        buildPrefix="mvn <#if !project.legacyProject && project.pomPath?? && project.pomPath !="">-f ${project.pomPath} </#if>clean org.jacoco:jacoco-maven-plugin:prepare-agent <#if project.legacyProject>compile<#else>install</#if>  ";
                                   </#if>
                                   String buildSuffix="-Dmaven.test.skip=${project.profile.skipUnitTest?string("true","false")} -Dmirror.url=${mirrorUrl} -Dmaven.test.failure.ignore=true -Dmaven.repo.local=/usr/share/maven-repo/teams/${project.team.id} -Denv.GIT_COMMIT="+revision ;
                                   <#if buildProperty?? && buildProperty !="">
                                        buildSuffix=" ${buildProperty} " +buildSuffix;
                                   </#if>
                                   String commandLine=buildPrefix+buildSuffix;
                                    sh script: commandLine,returnStdout: false
                       echo '========maven build end========'
                                }
                            }
                        }
                       </#if> 
                        
                    </#if>
 </#if>