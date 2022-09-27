<#if !project.profile?? || !project.profile.skipDockerBuild>
                    //build docker image
                    stage('build docker image'){
                        steps {
                           echo '========build docker image start========'
                           sh '''docker login -u ${project.registry.user}  -p ${project.registry.password}  ${project.registry.url}''' 
                           echo 'iss devops docker image namespace is: ${project.profile.nameSpace}'     
                           echo 'iss devops docker image tag are: ${dockerImageTag},latest'                   
                           <#if project.registry?? && project.registry.url??>
                                <#if project.projectType?? && project.projectType=="net">
                                    sh '''docker build -t ${project.registry.url}/${project.profile.nameSpace}/${project.name}:latest -f ./Dockerfile .'''
                                    sh '''docker push ${project.registry.url}/${project.profile.nameSpace}/${project.name}:latest'''
                                    sh '''docker tag ${project.registry.url}/${project.profile.nameSpace}/${project.name}:latest ${project.registry.url}/${project.profile.nameSpace}/${project.name}:v-${dockerImageTag!.now?string("yyyyMMddHHmm")}'''
                                    sh '''docker push ${project.registry.url}/${project.profile.nameSpace}/${project.name}:v-${dockerImageTag!.now?string("yyyyMMddHHmm")}'''
                                <#else>
                                    <#if !project.legacyProject && project.pomPath?? && project.pomPath !="">
                                         sh '''mkdir -p <#if project.pomPath?ends_with(".xml") && project.pomPath?contains("/")>${project.pomPath?keep_before_last("/")}/target<#else>${project.pomPath}/target</#if> '''
                                    </#if>
                                    //build
                                    sh '''mvn <#if !project.legacyProject && project.pomPath?? && project.pomPath !="">-f ${project.pomPath} </#if> <#if project.profile?? && project.profile.buildArm64Image>-Ddocker.platform=arm64</#if> -Ddocker.build.host=${dockerBuildHost} -Ddocker.build.groupId=${project.profile.nameSpace} <#if project.profile?? && project.profile.buildArm64Image>-Ddocker.build.version=arm64<#else>-Ddocker.build.version=latest</#if> -Ddocker.registry=${project.registry.url} <#if project.registry.user??> -Dmirror.url=${mirrorUrl} -Ddocker.registry.serverId=docker-${project.registry.id} -Ddocker.registry.username=${project.registry.user} -Ddocker.registry.password=${project.registry.password} -Ddocker.registry.email=${project.registry.email} </#if>  docker:build -DdockerImageTags=latest,v-${dockerImageTag!.now?string("yyyyMMddHHmm")} -DpushImageTag -Dmaven.repo.local=/usr/share/maven-repo/teams/${project.team.id}'''
                                </#if>
                           <#else>
                                script {
                                    echo 'skip docker image build for registry or registry url is null'
                                }
                           </#if>
                           echo '========build docker image end========'
                        }
                    }
                    </#if>