 <#if project.profile?? && project.profile.updateJob>
                     //k8s update job
                    stage('k8s update job'){
                        steps {
 echo '========k8s update job start========'
                             sh 'kubectl <#if project.profile.kubectlConfig?? && project.profile.kubectlConfig!=''> --kubeconfig ${project.profile.kubectlConfig} </#if> delete job ${deployConfig.name} --ignore-not-found=true -n ${project.profile.nameSpace!'dev'}'
                             sh 'find ${project.profile.assignYamlPath!'k8s'} -type f -exec sed -i \'s/<#noparse>${NAMESPACE}</#noparse>/${project.profile.nameSpace}/g\' <#noparse>{}</#noparse> \\;'
                             sh 'kubectl <#if project.profile.kubectlConfig?? && project.profile.kubectlConfig!=''> --kubeconfig ${project.profile.kubectlConfig} </#if> apply -R -f  ${project.profile.assignYamlPath!'k8s'} -n ${project.profile.nameSpace}'
                             sh 'sleep 30'
                             sh 'kubectl <#if project.profile.kubectlConfig?? && project.profile.kubectlConfig!=''> --kubeconfig ${project.profile.kubectlConfig} </#if> logs --timestamps=true  --ignore-errors=true job/${project.name} -n ${project.profile.nameSpace}'
   echo '========k8s update job end========'
                        }
                    }
</#if>