 <#if project.profile?? && project.profile.restartDeploy?string == "true">
 
           <#if deployConfig.appType?? && deployConfig.appType=='job'>
               stage('k8s restart job'){
                        steps {
            echo '========k8s restart job start========'
                          echo '重新运行job'
                          sh 'kubectl <#if project.profile.kubectlConfig?? && project.profile.kubectlConfig!=''> --kubeconfig ${project.profile.kubectlConfig} </#if> delete job ${deployConfig.name} --ignore-not-found=true -n ${project.profile.nameSpace!'dev'}'
                          sh 'find ${project.profile.assignYamlPath!'k8s'} -type f -exec sed -i \'s/<#noparse>${NAMESPACE}</#noparse>/${project.profile.nameSpace}/g\' <#noparse>{}</#noparse> \\;'
                             sh 'kubectl <#if project.profile.kubectlConfig?? && project.profile.kubectlConfig!=''> --kubeconfig ${project.profile.kubectlConfig} </#if> apply -R -f ${project.profile.assignYamlPath!'k8s'} -n ${project.profile.nameSpace}'
                             sh 'sleep 30'
                             sh 'kubectl <#if project.profile.kubectlConfig?? && project.profile.kubectlConfig!=''> --kubeconfig ${project.profile.kubectlConfig} </#if> logs --timestamps=true  --ignore-errors=true job/${project.name} -n ${project.profile.nameSpace}'
            echo '========k8s restart job end========'
                          }
                    }
 
           <#else>
                     //k8s restart deploy
                    stage('k8s restart deploy'){
                        steps {
                           echo '========k8s restart job start========'
                             sh 'kubectl <#if project.profile.kubectlConfig?? && project.profile.kubectlConfig!=''> --kubeconfig ${project.profile.kubectlConfig} </#if> rollout restart ${deployConfig.appType!'deployment'}/${rolloutName} -n ${project.profile.nameSpace!'dev'}'
                           
                             echo '升级状态'
                             sh 'kubectl <#if project.profile.kubectlConfig?? && project.profile.kubectlConfig!=''> --kubeconfig ${project.profile.kubectlConfig} </#if> rollout status ${deployConfig.appType!'deployment'}/${rolloutName} -n ${project.profile.nameSpace!'dev'}'
                             
                             
                            // sh 'kubectl <#if project.profile.kubectlConfig?? && project.profile.kubectlConfig!=''> --kubeconfig ${project.profile.kubectlConfig} </#if> rollout restart statefulset/${rolloutName} -n ${project.profile.nameSpace!'dev'}'
                           
                            // echo '升级状态'
                            // sh 'kubectl <#if project.profile.kubectlConfig?? && project.profile.kubectlConfig!=''> --kubeconfig ${project.profile.kubectlConfig} </#if> rollout status statefulset/${rolloutName} -n ${project.profile.nameSpace!'dev'}'
                            echo '========k8s restart job end========'
                        }
                    }
           </#if>        
                    
</#if>