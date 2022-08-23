package com.eazybuilder.ci.entity;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Table;

import org.hibernate.annotations.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Entity
@Table(name="CI_PROJECT",uniqueConstraints=@UniqueConstraint(columnNames= {"NAME","TEAM_ID"}))
//@SQLDelete(sql = "update ci_project set isRemove = 1 where id = ?")
@Where(clause = "IS_DEL = 0 or IS_DEL is null")
public class Project extends BaseEntry{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",columnDefinition="int(5)")
	private String id;

	private String devopsProjectId;

	//加@Transient是因为这些字段是渲染模板的时候使用的，不需要保存数据库。
	@Transient
	private String redmineCode;
	@Transient
	private String redmineUser;
	@Transient
	private String dockerImageTag;
	//是否更新配置文件
	@Transient
	private String configEdit;
	@Transient
	private String nameSpace;
	@Transient
	private String rolloutVersion;
	@Transient
	private Date releaseDate;
	@Transient
	private String dbUrl;
	@Transient
	private String dbUserName;
	@Transient
	private String dbPassword;
	@Transient
	private String gitlabApiUrl;

	//tag的具体描述
	@Transient
	private String tagDetail;
	//分支版本
	@Transient
	private String branchVersion;

	//tag 版本
	@Transient
	private String tagVersion;

	//更新时间
//    @LastModifiedDate
	private Date updateTime;



    /**
     *     部署方式
     */
	@Column(name="deploy_type",columnDefinition="int(1) default 1")
	private DeployType  deployType;


	/**
	 * 自定义
	 */
	private String buildParam;

	/**
	 * k8s配置 一个项目可以有多个配置
	 */
//	级联关系是CascadeType.ALL，所以save时会保存级联的对象Role，但是Role已经存在，因此就报错了。
//	将cascade改为CascadeType.MERGE或者CascadeType.REFRESH即可，表示级联对象在Role表存在则进行update操作，
//	而不做save操作。级联操作时谨慎用CascadeType.ALL
	@OneToMany(fetch = FetchType.EAGER,cascade=CascadeType.ALL)
//	@OneToMany(fetch = FetchType.EAGER,cascade=CascadeType.MERGE)
	@Fetch(FetchMode.SUBSELECT)
	private List<DeployConfig> deployConfigList;

    private String name;
    private String description;

//    @OneToMany(fetch = FetchType.LAZY,cascade=CascadeType.MERGE)
//    private List<ProjectHistory> projectHistoryList;

    @OneToOne
    private Team team;
    
	@OneToOne(cascade=CascadeType.ALL)
    private Scm scm;
    //ANT 项目
	@Column(name="legacy_project",columnDefinition="int(1) default 0")
	private boolean legacyProject;
	
	@Column(name="project_type",columnDefinition="int(1) default 0")
	private ProjectType projectType;
	/**
	 * pom.xml位置，!legacyProject时有效，不指定默认为当前目录(jenkins_workspace/{projectName}/pom.xml)
	 */
	private String pomPath;
    //eazybuilder4/5 project;

	private String netPath;

	private String netSlnPath;

	private String netTestPath;

	private NetType netType;

	private String sqlPath;

	private SqlType sqlType;

	@Column(name="eazybuilder_ejb_project",columnDefinition="int(1) default 0")
	private boolean eazybuilderEjbProject;
	
	@Column(name="eazybuilder_style_project",columnDefinition="int(1) default 0")
	private boolean eazybuilderStyleProject;
	
	
	private String codeCharset;
	/**
	 * 源码位置（相对于SVN地址、!eazybuilderEjbProject时有效）
	 */
	private String srcPath;
	
	/**
	 * lib包位置(相对于SVN地址、!eazybuilderEjbProject时有效)
	 */
	private String libPath;
	
	private String jdk;
	
	@ManyToOne(cascade=CascadeType.ALL)
	private Deploy deployInfo;
	
	@ManyToOne(optional=true)
	@JoinColumn(name = "REGISTRY_ID", foreignKey = @ForeignKey(value=ConstraintMode.NO_CONSTRAINT))  
	@NotFound(action=NotFoundAction.IGNORE)
	private DockerRegistry registry;
	/**
	 * 镜像仓库中的命名空间(例如${registry_IP}:5000/ci/xxx  命名空间即为ci)
	 */
	private String imageSchema;
	
	private String sonarKey;
	
	private String jobName;
	
	private String gitBranch;
	
	private String imageTag;
	
	/**
	 * 是否允许自动构建（由钩子触发）
	 */
	@Column(name="auto_build",columnDefinition="int default 1")
	private boolean autoBuild;
	
	/**
	 * 默认的构建流水
	 */
	@ManyToOne
	private PipelineProfile defaultProfile;

	/**
	 * 流水线设置
	 */
	@Transient
	@JsonIgnore(false)
	private PipelineProfile profile;


	/**
	 * 自定义
	 * 对应mvn命令后的-D参数，多个请用空格分隔
	 */
	private String buildProperty;

	public DeployType getDeployType() {
		return deployType;
	}


	public String getDevopsProjectId() {
		return devopsProjectId;
	}

	public String getImageTag() {
		return imageTag;
	}

	public void setImageTag(String imageTag) {
		this.imageTag = imageTag;
	}

	public void setDevopsProjectId(String devopsProjectId) {
		this.devopsProjectId = devopsProjectId;
	}

	public List<DeployConfig> getDeployConfigList() {
		return deployConfigList;
	}

	public void setDeployConfigList(List<DeployConfig> deployConfigList) {
		this.deployConfigList = deployConfigList;
	}
	public void setDeployType(DeployType deployType) {
		this.deployType = deployType;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Scm getScm() {
		return scm;
	}
	public void setScm(Scm scm) {
		this.scm = scm;
	}
	public Deploy getDeployInfo() {
		return deployInfo;
	}
	public void setDeployInfo(Deploy deployInfo) {
		this.deployInfo = deployInfo;
	}
	public DockerRegistry getRegistry() {
		return registry;
	}
	public void setRegistry(DockerRegistry registry) {
		this.registry = registry;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSonarKey() {
		return sonarKey;
	}
	public void setSonarKey(String sonarKey) {
		this.sonarKey = sonarKey;
	}
	public PipelineProfile getProfile() {
		return profile;
	}
	public void setProfile(PipelineProfile profile) {
		this.profile = profile;
	}
	public Team getTeam() {
		return team;
	}
	public void setTeam(Team team) {
		this.team = team;
	}
	public boolean isLegacyProject() {
		return legacyProject;
	}
	public void setLegacyProject(boolean legacyProject) {
		this.legacyProject = legacyProject;
	}
	public String getImageSchema() {
		return imageSchema;
	}
	public void setImageSchema(String imageSchema) {
		this.imageSchema = imageSchema;
	}
	public boolean iseazybuilderEjbProject() {
		return eazybuilderEjbProject;
	}
	public void seteazybuilderEjbProject(boolean eazybuilderEjbProject) {
		this.eazybuilderEjbProject = eazybuilderEjbProject;
	}
	public PipelineProfile getDefaultProfile() {
		return defaultProfile;
	}
	public void setDefaultProfile(PipelineProfile defaultProfile) {
		this.defaultProfile = defaultProfile;
	}
	public String getSrcPath() {
		return srcPath;
	}
	public void setSrcPath(String srcPath) {
		this.srcPath = srcPath;
	}
	public String getLibPath() {
		return libPath;
	}
	public void setLibPath(String libPath) {
		this.libPath = libPath;
	}
	public String getPomPath() {
		return pomPath;
	}
	public void setPomPath(String pomPath) {
		this.pomPath = pomPath;
	}
	public boolean isEazybuilderStyleProject() {
		return eazybuilderStyleProject;
	}
	public void setEazybuilderStyleProject(boolean eazybuilderStyleProject) {
		this.eazybuilderStyleProject = eazybuilderStyleProject;
	}
	public String getCodeCharset() {
		return codeCharset;
	}
	public void setCodeCharset(String codeCharset) {
		this.codeCharset = codeCharset;
	}
	public String getJdk() {
		return jdk;
	}
	public void setJdk(String jdk) {
		this.jdk = jdk;
	}
	public ProjectType getProjectType() {
		return projectType;
	}
	public void setProjectType(ProjectType projectType) {
		this.projectType = projectType;
	}
	public boolean isAutoBuild() {
		return autoBuild;
	}
	public void setAutoBuild(boolean autoBuild) {
		this.autoBuild = autoBuild;
	}

	public String getBranchVersion() {
		return branchVersion;
	}

	public void setBranchVersion(String branchVersion) {
		this.branchVersion = branchVersion;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getTagDetail() {
		return tagDetail;
	}

	public void setTagDetail(String tagDetail) {
		this.tagDetail = tagDetail;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getGitBranch() {
		return gitBranch;
	}

	public void setGitBranch(String gitBranch) {
		this.gitBranch = gitBranch;
	}


	public String getPiplineEventProjectId(){
		return StringUtils.isEmpty(this.getDevopsProjectId()) ? this.getId() :  this.getDevopsProjectId();
	}

	public String getNameSpace() {
		return nameSpace;
	}

	public void setNameSpace(String nameSpace) {
		this.nameSpace = nameSpace;
	}

	public String getBuildParam() {
		return buildParam;
	}

	public void setBuildParam(String buildParam) {
		this.buildParam = buildParam;
	}

	public String getRolloutVersion() {
		return rolloutVersion;
	}

	public void setRolloutVersion(String rolloutVersion) {
		this.rolloutVersion = rolloutVersion;
	}

	public String getDbUrl() {
		return dbUrl;
	}

	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}

	public String getDbUserName() {
		return dbUserName;
	}

	public void setDbUserName(String dbUserName) {
		this.dbUserName = dbUserName;
	}

	public String getConfigEdit() {
		return configEdit;
	}

	public void setConfigEdit(String configEdit) {
		this.configEdit = configEdit;
	}

	public String getDbPassword() {
		return dbPassword;
	}

	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}

	public String getGitlabApiUrl() {
		return gitlabApiUrl;
	}

	public void setGitlabApiUrl(String gitlabApiUrl) {
		this.gitlabApiUrl = gitlabApiUrl;
	}

	public String getDockerImageTag() {
		return dockerImageTag;
	}

	public void setDockerImageTag(String dockerImageTag) {
		this.dockerImageTag = dockerImageTag;
	}

	public String getTagVersion() {
		return tagVersion;
	}

	public void setTagVersion(String tagVersion) {
		this.tagVersion = tagVersion;
	}

	public String getRedmineCode() {
		return redmineCode;
	}

	public void setRedmineCode(String redmineCode) {
		this.redmineCode = redmineCode;
	}

	public String getRedmineUser() {
		return redmineUser;
	}

	public void setRedmineUser(String redmineUser) {
		this.redmineUser = redmineUser;
	}

	public String getNetPath() {
		return netPath;
	}

	public void setNetPath(String netPath) {
		this.netPath = netPath;
	}

	public String getNetSlnPath() {
		return netSlnPath;
	}

	public void setNetSlnPath(String netSlnPath) {
		this.netSlnPath = netSlnPath;
	}

	public NetType getNetType() {
		return netType;
	}

	public void setNetType(NetType netType) {
		this.netType = netType;
	}

	public String getNetTestPath() {
		return netTestPath;
	}

	public void setNetTestPath(String netTestPath) {
		this.netTestPath = netTestPath;
	}

	public String getBuildProperty() {
		return buildProperty;
	}

	public void setBuildProperty(String buildProperty) {
		this.buildProperty = buildProperty;
	}

	public String getSqlPath() {
		return sqlPath;
	}

	public void setSqlPath(String sqlPath) {
		this.sqlPath = sqlPath;
	}

	public SqlType getSqlType() {
		return sqlType;
	}

	public void setSqlType(SqlType sqlType) {
		this.sqlType = sqlType;
	}

	@Override
	public String toString() {
		return "Project{" +
				"id='" + id + '\'' +
				", devopsProjectId='" + devopsProjectId + '\'' +
				", redmineCode='" + redmineCode + '\'' +
				", redmineUser='" + redmineUser + '\'' +
				", dockerImageTag='" + dockerImageTag + '\'' +
				", configEdit='" + configEdit + '\'' +
				", nameSpace='" + nameSpace + '\'' +
				", rolloutVersion='" + rolloutVersion + '\'' +
				", releaseDate=" + releaseDate +
				", dbUrl='" + dbUrl + '\'' +
				", dbUserName='" + dbUserName + '\'' +
				", dbPassword='" + dbPassword + '\'' +
				", gitlabApiUrl='" + gitlabApiUrl + '\'' +
				", tagDetail='" + tagDetail + '\'' +
				", branchVersion='" + branchVersion + '\'' +
				", tagVersion='" + tagVersion + '\'' +
				", updateTime=" + updateTime +
				", deployType=" + deployType +
				", buildParam='" + buildParam + '\'' +
				", deployConfigList=" + deployConfigList +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", team=" + team +
				", scm=" + scm +
				", legacyProject=" + legacyProject +
				", projectType=" + projectType +
				", pomPath='" + pomPath + '\'' +
				", netPath='" + netPath + '\'' +
				", netSlnPath='" + netSlnPath + '\'' +
				", netType=" + netType +
				", eazybuilderEjbProject=" + eazybuilderEjbProject +
				", eazybuilderStyleProject=" + eazybuilderStyleProject +
				", codeCharset='" + codeCharset + '\'' +
				", srcPath='" + srcPath + '\'' +
				", libPath='" + libPath + '\'' +
				", jdk='" + jdk + '\'' +
				", deployInfo=" + deployInfo +
				", registry=" + registry +
				", imageSchema='" + imageSchema + '\'' +
				", sonarKey='" + sonarKey + '\'' +
				", jobName='" + jobName + '\'' +
				", gitBranch='" + gitBranch + '\'' +
				", imageTag='" + imageTag + '\'' +
				", autoBuild=" + autoBuild +
				", defaultProfile=" + defaultProfile +
				", profile=" + profile +
				'}';
	}
}
