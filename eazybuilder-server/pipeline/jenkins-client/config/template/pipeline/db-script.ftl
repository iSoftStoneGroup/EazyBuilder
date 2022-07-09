<#if project.profile?? && project.profile.sqlScript>

    stage('步骤1 执行正向脚本'){
        steps {
            echo '========数据库正反正验证 start========'
            sh 'kubectl delete job ${project.name} --ignore-not-found=true -n ${project.profile.nameSpace!'dev'}'
            sh 'find ${project.profile.assignYamlPath!'validate'} -type f -exec sed -i \'s/<#noparse>${NAMESPACE}</#noparse>/${project.profile.nameSpace}/g\' <#noparse>{}</#noparse> \\;'
            sh 'kubectl apply -R -f ${project.profile.assignYamlPath!'validate'} -n ${project.profile.nameSpace}'
            sh 'sleep 60'
            sh 'kubectl logs --timestamps=true  --ignore-errors=true job/${project.name} -n ${project.profile.nameSpace}'
        }
    }
    stage('步骤2 执行反向脚本'){
        steps {
            sh 'kubectl delete job ${project.name} --ignore-not-found=true -n ${project.profile.nameSpace!'dev'}'
            sh 'find ${project.profile.rollbackYamlPath!'rollback'} -type f -exec sed -i \'s/<#noparse>${NAMESPACE}</#noparse>/${project.profile.nameSpace}/g\' <#noparse>{}</#noparse> \\;'
            sh 'kubectl apply -R -f ${project.profile.rollbackYamlPath!'rollback'} -n ${project.profile.nameSpace}'
            sh 'sleep 60'
            sh 'kubectl logs --timestamps=true  --ignore-errors=true job/${project.name} -n ${project.profile.nameSpace}'
        }
    }
    stage('步骤3 执行正向脚本'){
        input {
            message 'continue?'
            ok 'confirm'
            parameters {
                choice choices: ['yes','no'],description: '正反脚本验证完成是否继续',name: 'sqlConfirm'
            }
        }
        when {
            expression {
                return (sqlConfirm=='yes')
            }
        }
        steps {
            sh 'kubectl delete job ${project.name} --ignore-not-found=true -n ${project.profile.nameSpace!'dev'}'
            sh 'find execute -type f -exec sed -i \'s/<#noparse>${NAMESPACE}</#noparse>/${project.profile.nameSpace}/g\' <#noparse>{}</#noparse> \\;'
            sh 'kubectl apply -R -f execute -n ${project.profile.nameSpace}'
            sh 'sleep 60'
            sh 'kubectl logs --timestamps=true --ignore-errors=true job/${project.name} -n ${project.profile.nameSpace}'
            echo '========数据库正反正验证 end========'
        }

    }
</#if>