{
        kubernetes {
            yaml '''
apiVersion: v1
kind: Pod
metadata:
  name: jenkins-slave
spec:
  dnsPolicy: ClusterFirst
  containers:
  - name: jnlp
  <#if project.projectType?? && project.projectType=="net">
  image: registryxxxxx/dev/jenkins-inbound-agent
  <#else>
  image: registryxxxxx/devops/jenkins-inbound-agent
  </#if>
    resources:
      requests:
        cpu: 100m
        memory: ${jenkinsRequestMeory}Mi
      limits:
        cpu: '5000m'
        memory: ${jenkinsLimitMeory}Mi
    volumeMounts:
        - mountPath: /opt/ci-tool
          name: jenkins
        - mountPath: /usr/share/maven-repo
          name: jenkins
        - mountPath: /var/jenkins_home
          name: jenkins
        - mountPath: /home/jenkins/agent
          name: jenkins
        - name: bin-path
          mountPath: /usr/local/bin/kubectl
        - name: kube-path
          mountPath: /root/.kube/
        - name: docker-bin
          mountPath: /usr/bin/docker
        - name: docker-sock
          mountPath: /var/run/docker.sock
        - name: jenkins
          mountPath: /root/.sonar/
  imagePullSecrets:
  - name: harbor
  tolerations:
  - key: "application-type"
    operator: "Equal"
    value: ${jenkinsBuildNode}
    effect: "NoSchedule"  
  
  hostAliases:
  - hostnames:
    - core.eazybuilder.com
    ip: ${jenkinsNetworkHost}
  - hostnames:
    - eazybuilder.nacos-devops.com
    ip: ${jenkinsNetworkHost}
  - hostnames:
    - jenkinsxxxxx
    ip: ${jenkinsNetworkHost}
  - hostnames:
    - ci-ingressxxxxx
    ip: ${jenkinsNetworkHost}
  - hostnames:
    - mysqlxxxxx
    ip: ${jenkinsNetworkHost}
  - hostnames:
    - redis-clusterxxxxx
    ip: ${jenkinsNetworkHost}
  - hostnames:
    - sonarqubexxxxx
    ip: ${jenkinsNetworkHost}
  - hostnames:
    - postgresxxxxx
    ip: ${jenkinsNetworkHost}
  - hostnames:
    - sonarqubexxxxx
    ip: ${jenkinsNetworkHost}
  - hostnames:
    - redminexxxxx
    ip: ${jenkinsNetworkHost}
  - hostnames:
    - demand-managementxxxxx
    ip: ${jenkinsNetworkHost}
  - hostnames:
    - nexus3xxxxx
    ip: ${jenkinsMavenUrl}
  - hostnames:
    - nacosxxxxx
    ip: ${jenkinsNetworkHost}
  - hostnames:
    - rabbitmqxxxxx
    ip: ${jenkinsNetworkHost}
  - hostnames:
    - rancher-suportxxxxx
    ip: ${jenkinsNetworkHost}
  - hostnames:
    - dev.login.ingress-upms.cn
    ip: ${jenkinsNetworkHost}
  - hostnames:
    - redis-single-secretxxxxx
    ip: ${jenkinsNetworkHost}
  - hostnames:
    - redis-singlexxxxx
    ip: ${jenkinsNetworkHost}
  - hostnames:
    - redis-single
    ip: ${jenkinsNetworkHost}
  - hostnames:
    - redis-plat
    ip: ${jenkinsNetworkHost}
  - hostnames:
    - redis-platxxxxx
    ip: ${jenkinsNetworkHost}
  - hostnames:
    - gitlabxxxxx
    ip: ${jenkinsNetworkHost}
  - hostnames:
    - gitlab-devxxxxx
    ip: ${jenkinsNetworkHost}    
  - hostnames:
    - eazybuilder-devops.cn
    ip: ${jenkinsNetworkHost}
  - hostnames:
    - dev.upms.swagger.hpa-cloud.com
    ip: ${jenkinsNetworkHost}
  - hostnames:
    - registryxxxxx
    ip: ${jenkinsNetworkHost}
  - hostnames:
    - ci-test-ingressxxxxx
    ip: ${jenkinsNetworkHost}
  - hostnames:
    - ats-ui-ingressxxxxx
    ip: ${jenkinsNetworkHost}
  - hostnames:
    - jenkins.icip.cn
    ip: ${jenkinsNetworkHost}
  - hostnames:
    - upms-webxxxxx
    ip: ${jenkinsNetworkHost}
  - hostnames:
    - upms-ci-ingressxxxxx
    ip: ${jenkinsNetworkHost}
  - hostnames:
    - cixxxxx
    ip: ${jenkinsNetworkHost}
  - hostnames:
    - measurexxxxx
    ip: ${jenkinsNetworkHost}
  - hostnames:
    - mysqldevxxxxx
    ip: ${jenkinsNetworkHost}
  - hostnames:
    - dtpxxxxx
    ip: ${jenkinsNetworkHost}
  - hostnames:
    - ipaas-jenkinsxxxxx
    ip: ${jenkinsNetworkHost}
  - hostnames:
    - measure-consolexxxxx
    ip: ${jenkinsNetworkHost}
  - hostnames:
    - mysqldevxxxxx
    ip: ${jenkinsNetworkHost}
  <#if jenkinsTeamGitlabUrl?? && jenkinsTeamGitlabUrl !="">    
  - hostnames:
    - ${jenkinsTeamGitlabUrl}
    ip: ${jenkinsTeamGitlabHost}    
  </#if>    
  serviceAccount: nfs-client-provisioner
  serviceAccountName: nfs-client-provisioner
  terminationGracePeriodSeconds: 30
  volumes:
  
  <#if jenkinsPathType?? && jenkinsPathType =="host">
    - name: jenkins
      hostPath:
        path: ${jenkinsDataPath} 
  <#else>
    - name: jenkins
      persistentVolumeClaim:
        claimName: ${jenkinsDataPath}  
  </#if>

    - name: bin-path
      hostPath:
        path: /usr/local/bin/kubectl
    - name: kube-path
      hostPath:
        path: /root/.kube/
    - name: docker-bin
      hostPath:
        path: /usr/bin/docker
    - name: docker-sock
      hostPath:
        path: /var/run/docker.sock
'''
        }
}
