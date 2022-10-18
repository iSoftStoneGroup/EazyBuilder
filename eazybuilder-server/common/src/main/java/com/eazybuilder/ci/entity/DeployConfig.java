package com.eazybuilder.ci.entity;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="CI_DEPLOY_CONFIG")
public class DeployConfig {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id",columnDefinition="int(5)")
    private String id;
    /**
     * yaml文件保存方式
     */
    private String storageType;

    /**
     * k8s imagetag
     */
    private String tag;

    /**
     * k8s 命名空间
     */
    private String nameSpace;

    /**
     * k8s 容器名
     */
    @Column(name="name")
    private String name;

    /**
     * k8s生成yaml文件后，将yaml存入obs中，查询时用id查询。
     */
    private String yamlId;

    /**
     * k8s ingress 域名，用来外外部访问的时候使用。
     */
    private String ingressHost;

    /**
     * k8s ingress path，。
     */
    private String ingressPath;


    /**
     * k8s 镜像名称
     */
    private String imageTag;

    /**
     * 前端默认80，后端默认8080
     */
    private String containerPort;

    /**
     * k8s 副本数，默认1
     * @return
     */
    private String replicas;

    /**
     * k8s cpu 容器能使用资源的最大值
     * @return
     */
    private String limitsCpu;
    /**
     * k8s 内存 容器能使用资源的最大值
     * @return
     */
    private String limitsMemory;
    
    
    /**
     * k8s 初始化容器
     * @return
     */
    private String initImageTag;
    
    
    /**
     * k8s 应用类型
     * @return
     */
    private AppType appType;
    
    
    /**
     * k8s 部署到指定的服务器名称
     * @return
     */
    private String hostname;

    /**
     * 环境变量
     * @return
     */
    @OneToMany(fetch = FetchType.EAGER,cascade=CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
    private List<DeployConfigDetailEnv> deployConfigDetailEnvs;
    /**
     * 环境变量
     * @return
     */
    @OneToMany(fetch = FetchType.EAGER,cascade=CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
    private List<DeployConfigDetailEnv> deployConfigDetailHosts;
    public AppType getAppType() {
		return appType;
	}

	public void setAppType(AppType appType) {
		this.appType = appType;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getLimitsCpu() {
        return limitsCpu;
    }

    public void setLimitsCpu(String limitsCpu) {
        this.limitsCpu = limitsCpu;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getReplicas() {
        return replicas;
    }

    public void setReplicas(String replicas) {
        this.replicas = replicas;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIngressHost() {
        return ingressHost;
    }

    public void setIngressHost(String ingressHost) {
        this.ingressHost = ingressHost;
    }

    public String getContainerPort() {
        return containerPort;
    }

    public void setContainerPort(String containerPort) {
        this.containerPort = containerPort;
    }

    public String getImageTag() {
        return imageTag;
    }

    public void setImageTag(String imageTag) {
        this.imageTag = imageTag;
    }

    public String getYamlId() {
        return yamlId;
    }

    public void setYamlId(String yamlId) {
        this.yamlId = yamlId;
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }


    public String getIngressPath() {
        return ingressPath;
    }

    public void setIngressPath(String ingressPath) {
        this.ingressPath = ingressPath;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

	public String getInitImageTag() {
		return initImageTag;
	}

	public void setInitImageTag(String initImageTag) {
		this.initImageTag = initImageTag;
	}

    public List<DeployConfigDetailEnv> getDeployConfigDetailEnvs() {
        return deployConfigDetailEnvs;
    }

    public void setDeployConfigDetailEnvs(List<DeployConfigDetailEnv> deployConfigDetailEnvs) {
        this.deployConfigDetailEnvs = deployConfigDetailEnvs;
    }

    public String getLimitsMemory() {
        return limitsMemory;
    }

    public void setLimitsMemory(String limitsMemory) {
        this.limitsMemory = limitsMemory;
    }

    public List<DeployConfigDetailEnv> getDeployConfigDetailHosts() {
        return deployConfigDetailHosts;
    }

    public void setDeployConfigDetailHosts(List<DeployConfigDetailEnv> deployConfigDetailHosts) {
        this.deployConfigDetailHosts = deployConfigDetailHosts;
    }

    public String getStorageType() {
        return storageType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }
}
