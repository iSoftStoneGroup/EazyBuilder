package com.eazybuilder.ci.controller;

import java.io.InputStream;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.converters.AutoConverter;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.google.common.collect.Lists;
import com.eazybuilder.ci.controller.vo.ProjectVO;
import com.eazybuilder.ci.controller.vo.ScmVO;
import com.eazybuilder.ci.entity.Project;
import com.eazybuilder.ci.entity.ProjectType;
import com.eazybuilder.ci.entity.ScmType;
import com.eazybuilder.ci.service.ProjectService;
import com.eazybuilder.ci.util.AsyncTaskTracker;



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
					remind="????????????(?????????????????????)";
				}else if(remind.indexOf("DataException")>0){
					remind="??????????????????????????????????????????";
				}else{
					remind="???????????????????????????????????????";
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
		@ExcelProperty("??????????????????")
		String projectEn;
		@ExcelProperty("????????????")
		String projectName;
		@ExcelProperty(value="????????????",converter = ProjectTypeAliasConverter.class)
		ProjectTypeAlias projectType;
		@ExcelProperty("??????????????????")
		String scmUrl;
		@ExcelProperty(value="??????????????????",converter = ScmTypeConverter.class)
		ScmType scmType;
		@ExcelProperty("????????????????????????(????????????)")
		String scmUser;
		@ExcelProperty("????????????????????????")
		String scmPwd;
		@ExcelProperty("????????????(ant??????)")
		String srcPath;
		@ExcelProperty("lib?????????(ant??????)")
		String libPath;
		@ExcelProperty("????????????(ant??????)")
		String compileLevel;
		@ExcelProperty("???????????????(ant??????)")
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
				throw new Exception("?????????"+context.readRowHolder().getRowIndex()+"???????????????????????????"+exception.getMessage());
			}
			@Override
			public void invoke(ProjectTemplateVo data, AnalysisContext context) {
				if(data.getProjectType()==ProjectTypeAlias.ant) {
					if(StringUtils.isBlank(data.getSrcPath())||StringUtils.isBlank(data.getLibPath())) {
						throw new RuntimeException("ANT??????????????????????????????lib?????????");
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
