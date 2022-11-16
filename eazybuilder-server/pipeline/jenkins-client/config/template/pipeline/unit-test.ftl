<#if !project.profile?? || !project.profile.skipUnitTest>
                       // stage('run unit test') {
                      //      steps {
                       //      sh '''mvn <#if !project.legacyProject && project.pomPath?? && project.pomPath !="">-f ${project.pomPath} </#if>test --fail-never -Dmaven.repo.local=/usr/share/maven-repo/teams/${project.team.id} -Dmirror.url=${mirrorUrl} -Dmaven.test.failure.ignore=true -Dsurefire.timeout=300  '''
                       //     }
                       // }
                        stage('aggregate test report'){
                            steps {
                                echo '========aggregate test report start========'
                                <#if project.projectType?? && project.projectType=="net">
                                    echo '========net unit test skip========'
                                    sh '''dotnet test <#if project.netTestPath !="">${project.netTestPath}<#else>src/${project.name}.Test/${project.name}.Test.csproj</#if> -p:CollectCoverage=true -p:CoverletOutputFormat=opencover -p:Exclude=\"[xunit.runner.*]*\"'''
                                    script {
                                        String[] arr = "${project.netTestPath}".split('/');
                                        String coveragePath = '';
                                        for(int i=0;i<arr.length;i++){
                                            if(i!=arr.length-1){
                                                coveragePath=coveragePath+arr[i]+"/";
                                            }
                                        }
                                        coveragePath=coveragePath+"coverage.xml";
                                        env.COVERAGEPATH = coveragePath
                                        println coveragePath;
                                    }
                                    echo "${r"${env.COVERAGEPATH}"}"
                                    <#if project.netType?? && project.netType=="net5">
                                        sh """dotnet /root/.dotnet/tools/.store/dotnet-sonarscanner/5.8.0/dotnet-sonarscanner/5.8.0/tools/net5.0/any/SonarScanner.MSBuild.dll begin -k:"net-test" -d:sonar.host.url=${sonarUrl} -d:sonar.login=${sonarUser} -d:sonar.password=${sonarPassword} -d:sonar.cs.opencover.reportsPaths=${r"${env.COVERAGEPATH}"} -d:sonar.branch.name=${tagName!'master'}"""
                                        sh '''nuget restore <#if project.netSlnPath !="">${project.netSlnPath}<#else>${project.name}.sln</#if> && dotnet build <#if project.netSlnPath !="">${project.netSlnPath}<#else>${project.name}.sln</#if>'''
                                        sh '''dotnet /root/.dotnet/tools/.store/dotnet-sonarscanner/5.8.0/dotnet-sonarscanner/5.8.0/tools/net5.0/any/SonarScanner.MSBuild.dll end -d:sonar.login=${sonarUser} -d:sonar.password=${sonarPassword}'''
                                    <#elseif project.netType?? && project.netType=="netcoreapp2">
                                        sh """dotnet /root/.dotnet/tools/.store/dotnet-sonarscanner/5.8.0/dotnet-sonarscanner/5.8.0/tools/netcoreapp2.0/any/SonarScanner.MSBuild.dll begin -k:"net-test" -d:sonar.host.url=${sonarUrl} -d:sonar.login=${sonarUser} -d:sonar.password=${sonarPassword} -d:sonar.cs.opencover.reportsPaths=${r"${env.COVERAGEPATH}"} -d:sonar.branch.name=${tagName!'master'}"""
                                        sh '''nuget restore <#if project.netSlnPath !="">${project.netSlnPath}<#else>${project.name}.sln</#if> && dotnet build <#if project.netSlnPath !="">${project.netSlnPath}<#else>${project.name}.sln</#if>'''
                                        sh '''dotnet /root/.dotnet/tools/.store/dotnet-sonarscanner/5.8.0/dotnet-sonarscanner/5.8.0/tools/netcoreapp2.0/any/SonarScanner.MSBuild.dll end -d:sonar.login=${sonarUser} -d:sonar.password=${sonarPassword}'''
                                    <#elseif project.netType?? && project.netType=="netcoreapp3">
                                        sh """dotnet /root/.dotnet/tools/.store/dotnet-sonarscanner/5.8.0/dotnet-sonarscanner/5.8.0/tools/netcoreapp3.0/any/SonarScanner.MSBuild.dll begin -k:"net-test" -d:sonar.host.url=${sonarUrl} -d:sonar.login=${sonarUser} -d:sonar.password=${sonarPassword} -d:sonar.cs.opencover.reportsPaths=${r"${env.COVERAGEPATH}"} -d:sonar.branch.name=${tagName!'master'}"""
                                        sh '''nuget restore <#if project.netSlnPath !="">${project.netSlnPath}<#else>${project.name}.sln</#if> && dotnet build <#if project.netSlnPath !="">${project.netSlnPath}<#else>${project.name}.sln</#if>'''
                                        sh '''dotnet /root/.dotnet/tools/.store/dotnet-sonarscanner/5.8.0/dotnet-sonarscanner/5.8.0/tools/netcoreapp3.0/any/SonarScanner.MSBuild.dll end -d:sonar.login=${sonarUser} -d:sonar.password=${sonarPassword}'''
                                    </#if>
                                <#else>
                                    sh '''mvn <#if !project.legacyProject && project.pomPath?? && project.pomPath !="">-f ${project.pomPath} </#if>surefire-report:report-only -Daggregate=true -Dmirror.url=${mirrorUrl} -Dmaven.repo.local=/usr/share/maven-repo/teams/${project.team.id}'''
                                    sh '''mvn <#if !project.legacyProject && project.pomPath?? && project.pomPath !="">-f ${project.pomPath} </#if>org.jacoco:jacoco-maven-plugin:0.8.5:report-aggregate -Dmirror.url=${mirrorUrl} -Dmaven.repo.local=/usr/share/maven-repo/teams/${project.team.id}'''
                                </#if>
                            echo '========aggregate test report end========'
                            }
                        } 
                    </#if>