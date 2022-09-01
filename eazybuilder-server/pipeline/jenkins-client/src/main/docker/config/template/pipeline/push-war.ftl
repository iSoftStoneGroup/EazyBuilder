<#if project.profile?? && project.profile.deployWar && project.profile.deployInfo??>
                        //deploy
                        stage('push war file') {
                            steps{
 echo '========push war file start========'
                             script {
                               sh 'echo "[dev]" > '+env.WORKSPACE+'/hosts'
                               sh 'echo "dev-server  ansible_ssh_host=\\"${project.profile.deployInfo.host.ip}\\"  ansible_ssh_user=\\"${project.profile.deployInfo.host.user}\\" ansible_ssh_pass=\\"${project.profile.deployInfo.host.pass}\\" ansible_ssh_port=${project.profile.deployInfo.host.port}" >> '+env.WORKSPACE+'/hosts'
                               ansiblePlaybook inventory: env.WORKSPACE+'/hosts', playbook: '/opt/push-war.yml',extras: '--extra-vars \'host=dev deploy_home=${project.profile.deployInfo.path}\''
                             }
 echo '========push war file end========'
                         }
                        }
                    </#if>