package com.eazybuilder.ci.openApi.vo;

public class PipelineProfileVO {
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
	public boolean isBuildArm64Image() {
		return buildArm64Image;
	}
	public void setBuildArm64Image(boolean buildArm64Image) {
		this.buildArm64Image = buildArm64Image;
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
	public boolean isSkipDependencyCheck() {
		return skipDependencyCheck;
	}
	public void setSkipDependencyCheck(boolean skipDependencyCheck) {
		this.skipDependencyCheck = skipDependencyCheck;
	}
	public boolean isSkipJsScan() {
		return skipJsScan;
	}
	public void setSkipJsScan(boolean skipJsScan) {
		this.skipJsScan = skipJsScan;
	}
	public boolean isSkipSqlScan() {
		return skipSqlScan;
	}
	public void setSkipSqlScan(boolean skipSqlScan) {
		this.skipSqlScan = skipSqlScan;
	}
}
