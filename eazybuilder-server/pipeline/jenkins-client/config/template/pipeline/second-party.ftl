<#if project.profile?? && project.profile.secondPartySwitch>
    <#if  project.projectType=='java'&& !project.legacyProject>
        stage('maven deploy') {
            steps{
                echo '========maven deploy start========'
                sh script: 'mvn <#if !project.legacyProject && project.pomPath?? && project.pomPath !="">-f ${project.pomPath} </#if> clean deploy -Ddocker.registry.serverId=<#if project.profile.secondParty.secondPartyType =='mavenRelease'>iss-releases</#if><#if project.profile.secondParty.secondPartyType =='mavenSnapshot'>iss-snapshots</#if>   -Ddocker.registry.username=${project.profile.secondParty.secondPartyUser} -Ddocker.registry.password=${project.profile.secondParty.secondPartyPass} -Dmaven.test.skip=true',returnStdout: false
                echo '========maven deploy end========'
            }
        }
    </#if>
    <#if  project.projectType=='net'>
        stage('net pack') {
            steps {
                echo '========net pack start========'
                <#if referenceSource?? && referenceSource != "" >
                    println('使用自定义NuGet源: ${referenceSource}');
                    sh '''dotnet nuget add source  ${referenceSource}'''
                    sh '''dotnet nuget remove source  nuget.org'''
                    sh '''dotnet nuget list source '''
                </#if>
                <#if project.profile.secondPartySwitch>
                    sh '''nuget spec '''
                    sh '''nuget pack '''
                    sh script: 'dotnet nuget push "*.nupkg"  -k  ${project.profile.secondParty.secondPartyKey} -s ${project.profile.secondParty.secondPartyPath!'http://nexus3.eazybuilder-devops.cn/repository/ipsa-net-test/'}',returnStdout: false
                </#if>
                echo '========net pack end========'
            }
        }
    </#if>
</#if>