package com.eazybuilder.ci.entity;

import javax.persistence.*;
/**
 * 流水线配置（用于精细化设置是否启用每一步）
 *
 */

import com.eazybuilder.ci.constant.AutoTestSwitch;
import com.eazybuilder.ci.constant.SendMailSwitch;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;
import org.springframework.util.StringUtils;

@Entity
@Table(name="CI_PIPELINE_PROFILE",uniqueConstraints=@UniqueConstraint(columnNames="NAME"))
@Where(clause = "is_del = 0 or is_del is null")
public class PipelineProfile {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",columnDefinition="int(8)")
	private String id;

	private String name;

	private String teamId;

	private String teamName;

	/**
	 * kubectl config 配置
	 */
	private  String kubectlConfig;

	/**
	 * 是否开启二方包自动上传
	 */
	private boolean secondPartySwitch;
	/**
	 * 是否扫描静态代码
	 */
	private Boolean staticJs;
	/**
	 * 是否开启数据库脱敏
	 */
	private boolean sqlTakeMin;
	/**
	 * 数据库脱敏脚本存放路径
	 */
	private String sqlTakeMinUrl;

	@Column(name="public_profile",columnDefinition="int(1) default 1")
	private boolean publicProfile;

	private boolean upgradeDocker;

	/**
	 * 是否上线部署。
	 */
	private boolean onlineDeploy;

	/**
	 * 命名空间
	 */
	private String nameSpace;
	/**
	 * 创建新分支
	 */
	@Column(name="update_pom")
	private boolean createBranch;

	/**
	 * 是否属于回滚k8s部署 ,true回滚
	 */
	private boolean rollout;

	/**
	 * 是否属于重启k8s部署 ,true
	 */
	private boolean restartDeploy;


	/**
	 * 是否跳过单元测试及测试覆盖率检测
	 */
	private boolean skipUnitTest;
	/**
	 * 是否跳过质量扫描
	 */
	private boolean skipScan;
	/**
	 * 是否跳过docker镜像构建
	 */
	private boolean skipDockerBuild;

	/**
	 * 是否构建ARM平台镜像(默认x86)
	 */
	private boolean buildArm64Image;
	/**
	 * 是否跳过自动部署
	 */
	private boolean skipDeploy;
	
	/**
	 * 是否使用工程自己的yaml文件进行，k8s部署 ,true
	 */
	private boolean assignYaml;
	
	/**
	 * 是否自动部署(包含初始化应用) ,true
	 */
	private boolean initDeploy;
	
	/**
	 * 是否检查pom文件 ,true
	 */
	private boolean checkPom;

	/**
	 * 是否跳过clone code
	 */
	private boolean skipCloneCode;
	
	/**
	 * 是否跳过maven build
	 */
	private boolean skipMvnBuild;
	
	/**
	 * 是否更新job
	 */
	private boolean updateJob;

	/**
	 * 推送war
	 */
	@Column(name="deploy_war",columnDefinition="int(1) default 0")
	private boolean deployWar;
	/**
	 * 部署设置
	 */
	@ManyToOne(cascade=CascadeType.ALL)
	@NotFound(action=NotFoundAction.IGNORE)
	private Deploy deployInfo;
	/**
	 * 是否跳过anchore
	 */
	private boolean skipAnchore;

	/**
	 * 自定义的扩展参数
	 */
	private String buildParam;

	/**
	 * 是否跳过依赖安全检查
	 */
	private boolean skipDependencyCheck;

	/**
	 * 是否跳过js扫描
	 */
	private boolean skipJsScan;
	/**
	 * 是否跳过sql扫描
	 */
	private boolean skipSqlScan;

	/**
	 * 是否做全量测试  默认为增量测试
	 */
	private AutoTestSwitch testSwitch;

	/**
	 * 是否开启邮件通知 默认为全量通知
	 */
	private SendMailSwitch sendMailSwitch;

	private String allName;
	
	private boolean addTag;
	
	private String tagPrefix;	
	
	private String releasePrefix;	

	private boolean focusRedlightRepair;

	private boolean rollbackYaml;

	private String assignYamlPath;

	private String rollbackYamlPath;

	private boolean sqlScript;
	
	private boolean updateConfig;
	
	private String configPath;

	/**
	 * sql脚本敏感提醒
	 */
	private boolean sqlRemind;
	
	//更新版本仓库标签
	private boolean updateTag;
	
    //ANT 项目
	@Column(name="is_del",columnDefinition="int(1) default 0")
	private boolean isDel;

	/**
	 * 自定义
	 * 对应mvn命令后的-D参数，多个请用空格分隔
	 */
	private String buildProperty;

	/**
	 * 发起自动化测试延时
	 */
	private Integer testDelayTime;

	/**
	 * 是否自动创建merge request请求
	 */
	private boolean createMR;

	/**
	 * 被指派合并人员邮箱
	 */
	private String mergeDesignee;

	private String gitlabApiDomain;

	@OneToOne(cascade=CascadeType.ALL)
    private SecondParty secondParty;
	
	
	public boolean isDel() {
		return isDel;
	}
	public void setDel(boolean isDel) {
		this.isDel = isDel;
	}
	public boolean isUpdateTag() {
		return updateTag;
	}
	public void setUpdateTag(boolean updateTag) {
		this.updateTag = updateTag;
	}
	public boolean isUpdateConfig() {
		return updateConfig;
	}
	public String getConfigPath() {
		return configPath;
	}
	public void setUpdateConfig(boolean updateConfig) {
		this.updateConfig = updateConfig;
	}
	public void setConfigPath(String configPath) {
		this.configPath = configPath;
	}
	public boolean isFocusRedlightRepair() {
		return focusRedlightRepair;
	}
	public void setFocusRedlightRepair(boolean focusRedlightRepair) {
		this.focusRedlightRepair = focusRedlightRepair;
	}
	public String getReleasePrefix() {
		return releasePrefix;
	}
	public void setReleasePrefix(String releasePrefix) {
		this.releasePrefix = releasePrefix;
	}
	public boolean isAddTag() {
		return addTag;
	}
	public void setAddTag(boolean addTag) {
		this.addTag = addTag;
	}
	public String getTagPrefix() {
		return tagPrefix;
	}
	public void setTagPrefix(String tagPrefix) {
		this.tagPrefix = tagPrefix;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean isSkipUnitTest() {
		return skipUnitTest;
	}
	public void setSkipUnitTest(boolean skipUnitTest) {
		this.skipUnitTest = skipUnitTest;
	}
	public boolean isSkipScan() {
		return skipScan;
	}
	public void setSkipScan(boolean skipScan) {
		this.skipScan = skipScan;
	}
	public boolean isSkipDockerBuild() {
		return skipDockerBuild;
	}
	public void setSkipDockerBuild(boolean skipDockerBuild) {
		this.skipDockerBuild = skipDockerBuild;
	}
	public boolean isSkipDeploy() {
		return skipDeploy;
	}
	public void setSkipDeploy(boolean skipDeploy) {
		this.skipDeploy = skipDeploy;
	}
	public String getBuildParam() {
		return buildParam;
	}
	public void setBuildParam(String buildParam) {
		this.buildParam = buildParam;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTeamId() {
		return teamId;
	}
	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}
	public boolean isPublicProfile() {
		return publicProfile;
	}
	public void setPublicProfile(boolean publicProfile) {
		this.publicProfile = publicProfile;
	}
	public boolean isSkipDependencyCheck() {
		return skipDependencyCheck;
	}
	public void setSkipDependencyCheck(boolean skipDependencyCheck) {
		this.skipDependencyCheck = skipDependencyCheck;
	}
	public boolean isSkipAnchore() {
		return skipAnchore;
	}
	public void setSkipAnchore(boolean skipAnchore) {
		this.skipAnchore = skipAnchore;
	}
	public boolean isSkipJsScan() {
		return skipJsScan;
	}
	public void setSkipJsScan(boolean skipJsScan) {
		this.skipJsScan = skipJsScan;
	}
	public Deploy getDeployInfo() {
		return deployInfo;
	}
	public void setDeployInfo(Deploy deployInfo) {
		this.deployInfo = deployInfo;
	}
	public boolean isDeployWar() {
		return deployWar;
	}
	public void setDeployWar(boolean deployWar) {
		this.deployWar = deployWar;
	}
	public boolean isSkipSqlScan() {
		return skipSqlScan;
	}
	public void setSkipSqlScan(boolean skipSqlScan) {
		this.skipSqlScan = skipSqlScan;
	}
	public boolean isBuildArm64Image() {
		return buildArm64Image;
	}
	public void setBuildArm64Image(boolean buildArm64Image) {
		this.buildArm64Image = buildArm64Image;
	}

	public boolean isCreateBranch() {
		return createBranch;
	}

	public void setCreateBranch(boolean createBranch) {
		this.createBranch = createBranch;
	}

	public boolean isRollout() {
		return rollout;
	}

	public void setRollout(boolean rollout) {
		this.rollout = rollout;
	}

	public String getNameSpace() {
		if(StringUtils.isEmpty(nameSpace)){
			return "dev";
		}
		return nameSpace;
	}

	public void setNameSpace(String nameSpace) {
		this.nameSpace = nameSpace;
	}
	public boolean isRestartDeploy() {
		return restartDeploy;
	}
	public void setRestartDeploy(boolean restartDeploy) {
		this.restartDeploy = restartDeploy;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getAllName() {
		return allName;
	}

	public void setAllName(String allName) {
		this.allName = allName;
	}
	public boolean isAssignYaml() {
		return assignYaml;
	}
	public void setAssignYaml(boolean assignYaml) {
		this.assignYaml = assignYaml;
	}
	public boolean isInitDeploy() {
		return initDeploy;
	}
	public void setInitDeploy(boolean initDeploy) {
		this.initDeploy = initDeploy;
	}

	public AutoTestSwitch getTestSwitch() {
		return testSwitch;
	}

	public void setTestSwitch(AutoTestSwitch testSwitch) {
		this.testSwitch = testSwitch;
	}

	public boolean isUpgradeDocker() {
		return upgradeDocker;
	}

	public void setUpgradeDocker(boolean upgradeDocker) {
		this.upgradeDocker = upgradeDocker;
	}

	public boolean isUpdateJob() {
		return updateJob;
	}
	public void setUpdateJob(boolean updateJob) {
		this.updateJob = updateJob;
	}
	public boolean isSkipCloneCode() {
		return skipCloneCode;
	}
	public boolean isSkipMvnBuild() {
		return skipMvnBuild;
	}
	public void setSkipCloneCode(boolean skipCloneCode) {
		this.skipCloneCode = skipCloneCode;
	}
	public void setSkipMvnBuild(boolean skipMvnBuild) {
		this.skipMvnBuild = skipMvnBuild;
	}
	public boolean isCheckPom() {
		return checkPom;
	}
	public void setCheckPom(boolean checkPom) {
		this.checkPom = checkPom;
	}

	public boolean isOnlineDeploy() {
		return onlineDeploy;
	}

	public void setOnlineDeploy(boolean onlineDeploy) {
		this.onlineDeploy = onlineDeploy;
	}

	public boolean isRollbackYaml() {
		return rollbackYaml;
	}

	public void setRollbackYaml(boolean rollbackYaml) {
		this.rollbackYaml = rollbackYaml;
	}

	public String getAssignYamlPath() {
		return assignYamlPath;
	}

	public void setAssignYamlPath(String assignYamlPath) {
		this.assignYamlPath = assignYamlPath;
	}

	public String getRollbackYamlPath() {
		return rollbackYamlPath;
	}

	public void setRollbackYamlPath(String rollbackYamlPath) {
		this.rollbackYamlPath = rollbackYamlPath;
	}

	public boolean isSqlScript() {
		return sqlScript;
	}

	public void setSqlScript(boolean sqlScript) {
		this.sqlScript = sqlScript;
	}

	public boolean isSqlRemind() {
		return sqlRemind;
	}

	public void setSqlRemind(boolean sqlRemind) {
		this.sqlRemind = sqlRemind;
	}

	public SendMailSwitch getSendMailSwitch() {
		return sendMailSwitch;
	}

	public void setSendMailSwitch(SendMailSwitch sendMailSwitch) {
		this.sendMailSwitch = sendMailSwitch;
	}

	public boolean isSqlTakeMin() {
		return sqlTakeMin;
	}

	public void setSqlTakeMin(boolean sqlTakeMin) {
		this.sqlTakeMin = sqlTakeMin;
	}

	public String getSqlTakeMinUrl() {
		return sqlTakeMinUrl;
	}

	public void setSqlTakeMinUrl(String sqlTakeMinUrl) {
		this.sqlTakeMinUrl = sqlTakeMinUrl;
	}

	public String getBuildProperty() {
		return buildProperty;
	}

	public void setBuildProperty(String buildProperty) {
		this.buildProperty = buildProperty;
	}

	public Integer getTestDelayTime() {
		return testDelayTime;
	}

	public void setTestDelayTime(Integer testDelayTime) {
		this.testDelayTime = testDelayTime;
	}

	public boolean isCreateMR() {
		return createMR;
	}

	public void setCreateMR(boolean createMR) {
		this.createMR = createMR;
	}

	public String getMergeDesignee() {
		return mergeDesignee;
	}

	public void setMergeDesignee(String mergeDesignee) {
		this.mergeDesignee = mergeDesignee;
	}

	public Boolean getStaticJs() {
		return staticJs;
	}

	public void setStaticJs(Boolean staticJs) {
		this.staticJs = staticJs;
	}

	public String getKubectlConfig() {
		return kubectlConfig;
	}

	public void setKubectlConfig(String kubectlConfig) {
		this.kubectlConfig = kubectlConfig;
	}

	public String getGitlabApiDomain() {
		return gitlabApiDomain;
	}

	public void setGitlabApiDomain(String gitlabApiDomain) {
		this.gitlabApiDomain = gitlabApiDomain;
	}

	public boolean getSecondPartySwitch() {
		return secondPartySwitch;
	}

	public void setSecondPartySwitch(boolean secondPartySwitch) {
		this.secondPartySwitch = secondPartySwitch;
	}

	public SecondParty getSecondParty() {
		return secondParty;
	}

	public void setSecondParty(SecondParty secondParty) {
		this.secondParty = secondParty;
	}

}
