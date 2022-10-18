<#if deployConfig??>
apiVersion: apps/v1
kind: Deployment
metadata:
  labels:
    app: ${deployConfig.name}
  name: ${deployConfig.name}
  namespace: ${profile.nameSpace!'dev'}
spec:
  revisionHistoryLimit: 3
  replicas: 1
  strategy:
    type: RollingUpdate
    rollingUpdate:
      maxSurge: 1
      maxUnavailable: 0
  selector:
    matchLabels:
      app: ${deployConfig.name}
  template:
    metadata:
      labels:
        app: ${deployConfig.name}
        <#if project.dockerImageTag?? && project.dockerImageTag !="">
        imageTag: "${project.dockerImageTag!}"
        <#else>
        imageTag: "${deployConfig.tag!'latest'}"
        </#if>
    spec:
<#if deployConfig.hostname?? && deployConfig.hostname !="">    
      affinity:
        nodeAffinity:
          requiredDuringSchedulingIgnoredDuringExecution:
            nodeSelectorTerms:
            - matchExpressions:
              - key: kubernetes.io/hostname
                operator: In
                values:
                  - ${deployConfig.hostname!'ecs-ce00-0001'}
</#if>
      dnsPolicy: ClusterFirst
      <#if deployConfig.deployConfigDetailHosts?? && (deployConfig.deployConfigDetailHosts?size>0)>
      hostAliases:
      <#list deployConfig.deployConfigDetailHosts as deployConfigDetailHost>
      - hostnames:
         - ${deployConfigDetailHost.name}
        ip: ${deployConfigDetailHost.data}
      </#list>
      </#if>
      containers:
      - env:
        - name: NACOS_SPACE
          value: ${profile.nameSpace!'dev'}
       <#list deployConfig.deployConfigDetailEnvs as deployConfigDetailEnv>
        - name: ${deployConfigDetailEnv.name}
          value:  '${deployConfigDetailEnv.data}'
       </#list>
        - name: NACOS_IP
          value: nacosxxxxx
        - name: NACOS_PORT
          value: '80'
        - name: TZ
          value: Asia/Shanghai
        image: ${project.registry.url}/${profile.nameSpace}/${deployConfig.imageTag}
        imagePullPolicy: Always
        name: ${deployConfig.name}
        ports:
        - containerPort: ${deployConfig.containerPort!'8080'}
        readinessProbe:
          failureThreshold: 10
          initialDelaySeconds: 10
          periodSeconds: 10
          successThreshold: 10
          tcpSocket:
            port: ${deployConfig.containerPort!'8080'}
          timeoutSeconds: 10        
        resources:
          limits:
            cpu: ${deployConfig.limitsCpu!'500m'}
            memory: ${deployConfig.limitsMemory!'1Gi'}
          requests:
            cpu: 1m
            memory: 100Mi
      imagePullSecrets:
      - name: harbor
---
apiVersion: v1
kind: Service
metadata:
  labels:
    app: ${deployConfig.name}
  name: ${deployConfig.name}
  namespace: ${profile.nameSpace!'dev'}
spec:
  clusterIP: None
  ports:
  - port: ${deployConfig.containerPort!'8080'}
    name: server
    targetPort: ${deployConfig.containerPort!'8080'}
  selector:
    app: ${deployConfig.name}

<#if deployConfig.ingressHost??>
---
# ------------------- App Ingress ------------------- #
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: ${deployConfig.name}
  namespace: ${profile.nameSpace!'dev'}
  annotations:
    nginx.ingress.kubernetes.io/proxy-body-size: 1024m
spec:
  rules:
  - host: ${deployConfig.ingressHost}
    http:
      paths:
      - path: ${deployConfig.ingressPath!'/'}
        pathType: Prefix
        backend:
          service:
            name: ${deployConfig.name}
            port: 
             name: server
</#if>
</#if>