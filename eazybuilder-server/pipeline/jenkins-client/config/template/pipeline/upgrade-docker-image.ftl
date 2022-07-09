<#if project.profile?? && project.profile.upgradeDocker>

    //upgrade docker image
    stage('upgrade docker image'){
    steps {
echo '========upgrade docker image start========'
    <#if project.registry?? && project.registry.url??>
        echo '========upgrade docker image start========'
        <#list deployConfigList as deployConfig>
         
     
        sh '''docker login -u ${project.registry.user}  -p ${project.registry.password}  ${project.registry.url}'''
        sh '''docker pull  ${project.registry.url}/${project.nameSpace}/${deployConfig.imageTag}:v-${project.dockerImageTag} '''
        sh '''docker tag ${project.registry.url}/${project.nameSpace}/${deployConfig.imageTag}:v-${project.dockerImageTag} ${project.registry.url}/${project.profile.nameSpace}/${deployConfig.imageTag}:v-${project.tagVersion} '''
        sh '''docker push  ${project.registry.url}/${project.profile.nameSpace}/${deployConfig.imageTag}:v-${project.tagVersion} '''


        sh '''docker tag ${project.registry.url}/${project.nameSpace}/${deployConfig.imageTag}:v-${project.dockerImageTag} ${project.registry.url}/${project.profile.nameSpace}/${deployConfig.imageTag}:latest '''
        sh '''docker push  ${project.registry.url}/${project.profile.nameSpace}/${deployConfig.imageTag}:latest '''

        sh '''docker rmi   ${project.registry.url}/${project.nameSpace}/${deployConfig.imageTag}:v-${project.dockerImageTag} '''
        echo '========upgrade docker image end========'
        </#list> 
    <#else>
        script {
        echo 'skip docker image build for registry or registry url is null'
        }
    </#if>
echo '========upgrade docker image end========'
    }
    }

    //auto create merge request
    stage('create merge request'){
    steps {
    <#if project.registry?? && project.registry.url??>
        echo 'create merge request'
    <#else>
        script {
        echo 'create merge request'
        }
    </#if>
    }
    }

</#if>