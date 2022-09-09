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
  image: ${registryUrl}/prod/jenkins-inbound-agent-net
  <#else>
  image: ${registryUrl}/prod/jenkins-inbound-agent-java
  </#if>
    resources:
      requests:
        cpu: 100m
        memory: ${jenkinsRequestMeory}Mi
      limits:
        cpu: '5000m'
        memory: ${jenkinsLimitMeory}Mi
    volumeMounts:
        <#if storageType?? && storageType =="local">
        - mountPath: /home/jenkins/agent/workspace/
          name: ci
        </#if>
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
    value: ${project.team.code}-jenkins
    effect: "NoSchedule"
  hostAliases:
  <#if jenkinsTeamGitlabUrl?? && jenkinsTeamGitlabUrl !="">
  hostAliases:
  - hostnames:
    - ${jenkinsTeamGitlabUrl}
    ip: ${jenkinsTeamGitlabHost}
  </#if>
  serviceAccount: nfs-client-provisioner
  serviceAccountName: nfs-client-provisioner
  terminationGracePeriodSeconds: 30
  volumes:
    <#if storageType?? && storageType =="local">
    - name: ci
      hostPath:
        path: ${k8sYamlPath}
    </#if>
    <#if jenkinsWorkType?? && jenkinsWorkType =="host">
    - name: jenkins
      hostPath:
        path: ${jenkinsWorkPath}
    <#else>
    - name: jenkins
      persistentVolumeClaim:
        claimName: ${jenkinsWorkPath}
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
