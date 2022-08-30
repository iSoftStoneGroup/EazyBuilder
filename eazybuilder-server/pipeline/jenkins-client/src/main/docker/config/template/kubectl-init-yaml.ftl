<#if deployConfig??>
apiVersion: apps/v1
kind: Deployment
metadata:
  labels:
    app: ${deployConfig.name}
  name: ${deployConfig.name}
  namespace: ${deployConfig.nameSpace!'dev'}
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
        imageTag: "${deployConfig.tag!'latest'}"
    spec:
      initContainers:
        - name: sql-init
          image: ${deployConfig.intiImageTag}
          imagePullPolicy: Always    
      containers:
      - image: ${deployConfig.imageTag}
        imagePullPolicy: Always
        name:  ${deployConfig.name}
        #cpu大小
        resources:
         requests:
            cpu: 100m
            memory: 1Gi
         limits:
            cpu: ${deployConfig.limitsCpu!'100m'}
            memory: 1Gi
        # 定义端口信息
        ports:
          - containerPort: ${deployConfig.containerPort!'8080'}
  #-因为是私有镜像仓库，需要登录
      imagePullSecrets:
      - name: harbor
---
apiVersion: v1
kind: Service
metadata:
  labels:
    app: ${deployConfig.name}-headless
  name: ${deployConfig.name}-headless
  namespace: ${deployConfig.nameSpace!'dev'}
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
  namespace: ${deployConfig.nameSpace!'dev'}

spec:
  rules:
  - host: ${deployConfig.ingressHost}
    http:
      paths:
      - path: ${deployConfig.ingressPath!'/'}
        pathType: Prefix
        backend:
          service:
            name: ${deployConfig.name}-headless
            port: 
             name: server
</#if>
</#if>