<#if !project.profile?? || !project.profile.skipScan>
                    stage('sonar scan'){
                       steps {
                            <#if project.projectType?? && project.projectType=="net">
                                echo '========sonar scan start========'
                                <#if project.netType?? && project.netType=="net5">
                                    sh '''dotnet /root/.dotnet/tools/.store/dotnet-sonarscanner/5.5.3/dotnet-sonarscanner/5.5.3/tools/net5.0/any/SonarScanner.MSBuild.dll begin -k:"net-test" -d:sonar.host.url=${sonarUrl} -d:sonar.login=${sonarUser} -d:sonar.password=${sonarPassword} -d:sonar.branch.name=${tagName!'master'}'''
                                    sh '''nuget restore <#if project.netSlnPath !="">${project.netSlnPath}<#else>${project.name}.sln</#if> && dotnet build <#if project.netSlnPath !="">${project.netSlnPath}<#else>${project.name}.sln</#if>'''
                                    sh '''dotnet /root/.dotnet/tools/.store/dotnet-sonarscanner/5.5.3/dotnet-sonarscanner/5.5.3/tools/net5.0/any/SonarScanner.MSBuild.dll end -d:sonar.login=${sonarUser} -d:sonar.password=${sonarPassword}'''
                                <#elseif project.netType?? && project.netType=="netcoreapp2">
                                    sh '''dotnet /root/.dotnet/tools/.store/dotnet-sonarscanner/5.5.3/dotnet-sonarscanner/5.5.3/tools/netcoreapp2.0/any/SonarScanner.MSBuild.dll begin -k:"net-test" -d:sonar.host.url=${sonarUrl} -d:sonar.login=${sonarUser} -d:sonar.password=${sonarPassword} -d:sonar.branch.name=${tagName!'master'}'''
                                    sh '''nuget restore <#if project.netSlnPath !="">${project.netSlnPath}<#else>${project.name}.sln</#if> && dotnet build <#if project.netSlnPath !="">${project.netSlnPath}<#else>${project.name}.sln</#if>'''
                                    sh '''dotnet /root/.dotnet/tools/.store/dotnet-sonarscanner/5.5.3/dotnet-sonarscanner/5.5.3/tools/netcoreapp2.0/any/SonarScanner.MSBuild.dll end -d:sonar.login=${sonarUser} -d:sonar.password=${sonarPassword}'''
                                <#elseif project.netType?? && project.netType=="netcoreapp3">
                                    sh '''dotnet /root/.dotnet/tools/.store/dotnet-sonarscanner/5.5.3/dotnet-sonarscanner/5.5.3/tools/netcoreapp3.0/any/SonarScanner.MSBuild.dll begin -k:"net-test" -d:sonar.host.url=${sonarUrl} -d:sonar.login=${sonarUser} -d:sonar.password=${sonarPassword} -d:sonar.branch.name=${tagName!'master'}'''
                                    sh '''nuget restore <#if project.netSlnPath !="">${project.netSlnPath}<#else>${project.name}.sln</#if> && dotnet build <#if project.netSlnPath !="">${project.netSlnPath}<#else>${project.name}.sln</#if>'''
                                    sh '''dotnet /root/.dotnet/tools/.store/dotnet-sonarscanner/5.5.3/dotnet-sonarscanner/5.5.3/tools/netcoreapp3.0/any/SonarScanner.MSBuild.dll end -d:sonar.login=${sonarUser} -d:sonar.password=${sonarPassword}'''
                                </#if>
                            <#else>
                                echo '========sonar scan start========'
                                sh '''if [ ! -d "node_modules" ]; then
                                mvn <#if !project.legacyProject && project.pomPath?? && project.pomPath !="">-f ${project.pomPath} </#if>sonar:sonar  -Dsonar.branch.name=${tagName!'master'} -Dmaven.repo.local=/usr/share/maven-repo/teams/${project.team.id} -Dsonar.scm.disabled=true -Dsonar.host.url=${sonarUrl} -Dsonar.login=${sonarUser} -Dsonar.password=${sonarPassword} -Dsonar.projectKey=${project.name}:${project.id} <#if project.profile?? && project.profile.skipJsScan?? && project.profile.skipJsScan>-Dsonar.exclusions=**/*.js  <#if project.profile.sonarProfile?? && project.profile.sonarProfile !="">-Dsonar.profile=${project.profile.sonarProfile} </#if> </#if>
                                else
                                mvn <#if !project.legacyProject && project.pomPath?? && project.pomPath !="">-f ${project.pomPath} </#if>sonar:sonar -Dsonar.branch.name=${tagName!'master'} -Dmaven.repo.local=/usr/share/maven-repo/teams/${project.team.id} -Dsonar.scm.disabled=true -Dsonar.host.url=${sonarUrl} -Dsonar.login=${sonarUser} -Dsonar.password=${sonarPassword} -Dsonar.projectKey=${project.name}:${project.id} <#if project.profile?? && project.profile.skipJsScan?? && project.profile.skipJsScan>-Dsonar.exclusions=**/*.js</#if> -Dsonar.modules=javascript-module  -Djavascript-module.sonar.projectBaseDir=./ -Djavascript-module.sonar.sources=src  <#if project.profile.sonarProfile?? && project.profile.sonarProfile !="">-Dsonar.profile=${project.profile.sonarProfile} </#if>
                                fi'''
                                echo '========sonar scan end========'
                            </#if>


                       }
                    }
                    </#if>