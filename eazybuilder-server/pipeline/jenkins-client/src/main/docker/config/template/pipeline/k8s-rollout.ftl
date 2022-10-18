//k8s deploy
stage('k8s rollout'){
steps {
sh 'kubectl <#if project.profile.kubectlConfig?? && project.profile.kubectlConfig!=''> --kubeconfig ${project.profile.kubectlConfig} </#if> rollout history ${deployConfig.appType!'deployment'}/${rolloutName} -n ${namespace!'dev'}'
<#if project.rolloutVersion??>
    script {
    echo '开始回滚应用到指定版本'
    sh 'kubectl <#if project.profile.kubectlConfig?? && project.profile.kubectlConfig!=''> --kubeconfig ${project.profile.kubectlConfig} </#if> rollout undo ${deployConfig.appType!'deployment'}/${rolloutName} --to-revision=${project.rolloutVersion} -n ${namespace!'dev'}'

    }
<#else>
    script {
    echo '回滚应用到上一个版本'
    sh 'kubectl <#if project.profile.kubectlConfig?? && project.profile.kubectlConfig!=''> --kubeconfig ${project.profile.kubectlConfig} </#if> rollout undo ${deployConfig.appType!'deployment'}/${rolloutName}  -n ${namespace!'dev'}'

    }
</#if>

echo '回滚状态'
sh 'kubectl <#if project.profile.kubectlConfig?? && project.profile.kubectlConfig!=''> --kubeconfig ${project.profile.kubectlConfig} </#if> rollout status ${deployConfig.appType!'deployment'}/${rolloutName} -n ${namespace!'dev'}'
}
}
