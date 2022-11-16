<#if project.profile?? && project.profile.upgradeDocker>

    //upgrade docker image
    stage('upgrade docker image'){
    steps {
echo '========upgrade docker image start========'
    <#if project.registry?? && project.registry.url??>
        <#list dockerDigests as dockerDigest>
        echo '========upgrade docker image start========'
        sh '''docker login -u ${project.registry.user}  -p ${project.registry.password}  ${project.registry.url}'''
        sh '''docker pull  ${project.registry.url}/${project.nameSpace}/${dockerDigest.imageName}:v-${dockerDigest.tag} '''
        sh '''docker tag ${project.registry.url}/${project.nameSpace}/${dockerDigest.imageName}:v-${dockerDigest.tag} ${project.registry.url}/${project.profile.nameSpace}/${dockerDigest.imageName}:v-${project.tagVersion} '''
        sh '''docker push  ${project.registry.url}/${project.profile.nameSpace}/${dockerDigest.imageName}:v-${project.tagVersion} '''


        sh '''docker tag ${project.registry.url}/${project.nameSpace}/${dockerDigest.imageName}:v-${dockerDigest.tag} ${project.registry.url}/${project.profile.nameSpace}/${dockerDigest.imageName}:latest '''
        sh '''docker push  ${project.registry.url}/${project.profile.nameSpace}/${dockerDigest.imageName}:latest '''

<#--        sh '''docker rmi   ${project.registry.url}/${project.nameSpace}/${dockerDigest.imageName}:v-${dockerDigest.tag} '''-->
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
    echo '========create merge request start========'
    <#if project.registry?? && project.registry.url??>
        echo 'create merge request'
    <#else>
        script {
        echo 'create merge request'
        }
    </#if>
    echo '========create merge request end========'
    }
    }

</#if>