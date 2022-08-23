package com.eazybuilder.ci.controller;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.tmatesoft.svn.core.SVNException;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.converters.AutoConverter;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.eazybuilder.ci.controller.vo.ProjectVO;
import com.eazybuilder.ci.controller.vo.ScmVO;
import com.eazybuilder.ci.entity.Project;
import com.eazybuilder.ci.entity.ProjectType;
import com.eazybuilder.ci.entity.Scm;
import com.eazybuilder.ci.entity.ScmType;
import com.eazybuilder.ci.service.ProjectService;
import com.eazybuilder.ci.svn.DisplayRepositoryTree;
import com.eazybuilder.ci.svn.ProjectSourceInfo;
import com.eazybuilder.ci.util.AsyncTaskTracker;
import com.eazybuilder.ci.util.AsyncTaskTracker.TaskTracker;


@RestController
@RequestMapping("/api/projectBatchImport")
public class ProjectBatchImportController {

	@Autowired
	ProjectService projectService;
	@Autowired
	AsyncTaskTracker taskTracker;
	
	@RequestMapping(value="/upload",method=RequestMethod.POST)
	public List<ProjectVO> storeResource(@RequestParam("uploadfile")MultipartFile request) throws Exception{
		return parseBatchProject(request.getInputStream());
	}
	
	@RequestMapping(value="/scanSvn",method=RequestMethod.POST)
	public Map<String,String> scanSvn(@RequestBody Scm scm){
		//异步执行
		String taskUid=taskTracker.submitTask(new TaskTracker<List<Project>>(){
			@Override
			public List<Project> doRun() {
				List<Project> prjs=Lists.newArrayList();
				try {
					List<ProjectSourceInfo> psis=DisplayRepositoryTree
							.list(scm.getUrl(), scm.getUser(), scm.getPassword(),logQueue);
					if(psis!=null){
						psis.forEach(psi->{
							Project project=new Project();
							project.setName(scm.getName()+"_"+psi.getName());
							project.setDescription(scm.getName()+"_"+psi.getName());
							project.setLegacyProject(!psi.isMaven());
							project.seteazybuilderEjbProject(false);
							project.setSrcPath(psi.getSrcPath());
							project.setLibPath(psi.getLibPath());
							project.setProjectType(psi.getProjectType());
							Scm projectScm=new Scm();
							projectScm.setUrl(scm.getUrl()+"/"+psi.getProjectPath());
							projectScm.setType(ScmType.svn);
							projectScm.setUser(scm.getUser());
							projectScm.setPassword(scm.getPassword());
							project.setScm(projectScm);
							
							prjs.add(project);
						});
					}
					
				} catch (SVNException e) {
					logQueue.add("执行异常:"+e.getMessage());
				}
				return prjs;
			}
		});
		Map<String,String> result=Maps.newHashMap();
		result.put("uid", taskUid);
		return result;
	}
	
	
	@RequestMapping(method=RequestMethod.POST)
	public List<ProjectImportResult> importResult(@RequestBody List<Project> projects){
		List<ProjectImportResult> results=Lists.newArrayList();
		for(Project project:projects){
			ProjectImportResult result=new ProjectImportResult();
			result.setProjectSimpleName(project.getDescription());
			if(project.getScm()!=null&&project.getScm().getType()==null){
				project.getScm().setType(ScmType.svn);
			}
			try {
				projectService.save(project);
				result.setSuccess(true);
			} catch (Exception e) {
				e.printStackTrace();
				String remind=e.getMessage();
				if(remind.indexOf("ConstraintViolationException")>0){
					remind="项目重复(已存在同名项目)";
				}else if(remind.indexOf("DataException")>0){
					remind="保存失败，请检查是否有列超长";
				}else{
					remind="其他错误，请联系系统管理员";
				}
				result.setSuccess(false);
				result.setRemindMsg(remind);
			}
			results.add(result);
		}
		return results;
	}
	
	public static class ProjectImportResult{
		String projectSimpleName;
		boolean success;
		String remindMsg;
		
		public String getProjectSimpleName() {
			return projectSimpleName;
		}
		public void setProjectSimpleName(String projectSimpleName) {
			this.projectSimpleName = projectSimpleName;
		}
		public boolean isSuccess() {
			return success;
		}
		public void setSuccess(boolean success) {
			this.success = success;
		}
		public String getRemindMsg() {
			return remindMsg;
		}
		public void setRemindMsg(String remindMsg) {
			this.remindMsg = remindMsg;
		}
	}
	
	public static enum ProjectTypeAlias{
		npm,
		maven,
		ant,
		gradle
	}
	
	public static class ProjectTypeAliasConverter extends AutoConverter{

		@Override
		public Object convertToJavaData(CellData cellData, ExcelContentProperty contentProperty,
				GlobalConfiguration globalConfiguration) {
			return ProjectTypeAlias.valueOf(cellData.getStringValue());
		}

		@Override
		public CellData convertToExcelData(Object value, ExcelContentProperty contentProperty,
				GlobalConfiguration globalConfiguration) {
			return new CellData(((ProjectTypeAlias)value).name());
		}
		
	}
	
	public static class ScmTypeConverter extends AutoConverter{

		@Override
		public Object convertToJavaData(CellData cellData, ExcelContentProperty contentProperty,
				GlobalConfiguration globalConfiguration) {
			return ScmType.valueOf(cellData.getStringValue());
		}

		@Override
		public CellData convertToExcelData(Object value, ExcelContentProperty contentProperty,
				GlobalConfiguration globalConfiguration) {
			return new CellData(((ScmType)value).name());
		}
		
	}
	public static class ProjectTemplateVo{
		@ExcelProperty("工程英文缩写")
		String projectEn;
		@ExcelProperty("工程名称")
		String projectName;
		@ExcelProperty(value="项目类型",converter = ProjectTypeAliasConverter.class)
		ProjectTypeAlias projectType;
		@ExcelProperty("源码仓库地址")
		String scmUrl;
		@ExcelProperty(value="源码仓库类型",converter = ScmTypeConverter.class)
		ScmType scmType;
		@ExcelProperty("源码仓库访问用户(建议只读)")
		String scmUser;
		@ExcelProperty("源码仓库访问密码")
		String scmPwd;
		@ExcelProperty("源码路径(ant项目)")
		String srcPath;
		@ExcelProperty("lib库路径(ant项目)")
		String libPath;
		@ExcelProperty("编译版本(ant项目)")
		String compileLevel;
		@ExcelProperty("源码字符集(ant项目)")
		String charset;
		
		
		public String getProjectEn() {
			return projectEn;
		}
		public void setProjectEn(String projectEn) {
			this.projectEn = projectEn;
		}
		public String getProjectName() {
			return projectName;
		}
		public void setProjectName(String projectName) {
			this.projectName = projectName;
		}
		public String getScmUrl() {
			return scmUrl;
		}
		public void setScmUrl(String scmUrl) {
			this.scmUrl = scmUrl;
		}
		public ScmType getScmType() {
			return scmType;
		}
		public void setScmType(ScmType scmType) {
			this.scmType = scmType;
		}
		public String getScmUser() {
			return scmUser;
		}
		public void setScmUser(String scmUser) {
			this.scmUser = scmUser;
		}
		public String getScmPwd() {
			return scmPwd;
		}
		public void setScmPwd(String scmPwd) {
			this.scmPwd = scmPwd;
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
		public String getCompileLevel() {
			return compileLevel;
		}
		public void setCompileLevel(String compileLevel) {
			this.compileLevel = compileLevel;
		}
		public String getCharset() {
			return charset;
		}
		public void setCharset(String charset) {
			this.charset = charset;
		}
		public ProjectTypeAlias getProjectType() {
			return projectType;
		}
		public void setProjectType(ProjectTypeAlias projectType) {
			this.projectType = projectType;
		}
		
	}
	
	private List<ProjectVO> parseBatchProject(InputStream excelInputStream) throws Exception{
		List<ProjectTemplateVo> excelData=Lists.newArrayList();
		EasyExcel.read(excelInputStream,ProjectTemplateVo.class,new AnalysisEventListener<ProjectTemplateVo>() {
			@Override
			public void onException(Exception exception, AnalysisContext context) throws Exception {
				throw new Exception("导入第"+context.readRowHolder().getRowIndex()+"行数据出错，原因："+exception.getMessage());
			}
			@Override
			public void invoke(ProjectTemplateVo data, AnalysisContext context) {
				if(data.getProjectType()==ProjectTypeAlias.ant) {
					if(StringUtils.isBlank(data.getSrcPath())||StringUtils.isBlank(data.getLibPath())) {
						throw new RuntimeException("ANT项目需指定源码路径和lib库路径");
					}
					if(StringUtils.isBlank(data.getCompileLevel())) {
						data.setCompileLevel("1.8");
					}
					if(StringUtils.isBlank(data.getCharset())) {
						data.setCharset("utf-8");
					}
				}
				excelData.add(data);
			}
			@Override
			public void doAfterAllAnalysed(AnalysisContext context) {
				
			}
		}).sheet().doRead();
		
		 
		 List<ProjectVO> projects=Lists.newArrayList();
		 
		 for(ProjectTemplateVo data:excelData){
			 ProjectVO project=new ProjectVO();
			 project.setDescription(data.getProjectName());
			 project.setName(data.getProjectEn());
			 switch(data.getProjectType()) {
			 case ant:
				 project.setJdk(data.getCompileLevel());
				 project.setCodeCharset(data.getCharset());
				 project.setSrcPath(data.getSrcPath());
				 project.setLibPath(data.getLibPath());
				 project.setLegacyProject(true);
			 case maven:
			 case gradle:
				 project.setProjectType(ProjectType.java);
				 break;
			 case npm:
				 project.setProjectType(ProjectType.npm);
				 break;
			 }
			 ScmVO scm=new ScmVO();
			 scm.setUrl(data.getScmUrl());
			 scm.setType(data.getScmType());
			 scm.setUser(data.getScmUser());
			 scm.setPassword(data.getScmPwd());
			 project.setScm(scm);
			 projects.add(project);
		 }
		return projects;
	}
}
