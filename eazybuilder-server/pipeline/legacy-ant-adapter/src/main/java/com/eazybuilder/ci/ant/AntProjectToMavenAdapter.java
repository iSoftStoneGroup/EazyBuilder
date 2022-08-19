package com.eazybuilder.ci.ant;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.eazybuilder.ci.ant.util.FreemarkerUtils;
import com.eazybuilder.ci.ant.vo.JarInfo;
import com.eazybuilder.ci.dto.Artifact;

import freemarker.cache.ByteArrayTemplateLoader;
import freemarker.template.Configuration;

public class AntProjectToMavenAdapter {
	/*singleton*/
	private static AntProjectToMavenAdapter INSTANCE=new AntProjectToMavenAdapter();
	
	private Configuration configuration;
	
	private AntProjectToMavenAdapter(){
		configuration=new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
		ByteArrayTemplateLoader loader=new ByteArrayTemplateLoader();
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		try(InputStream is=getClass().getClassLoader().getResourceAsStream("pom.ftl")){
			IOUtils.copy(is,baos);
			loader.putTemplate("pom.ftl",baos.toByteArray());
			configuration.setTemplateLoader(loader);
			configuration.setLocalizedLookup(false);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static AntProjectToMavenAdapter getInstance(){
		return INSTANCE;
	}
	private void moveDirectory(File home, File src,File target) throws IOException {
		String tmp=UUID.randomUUID().toString();
		File tmpFolder=new File(home,tmp);
		FileUtils.moveDirectory(src, tmpFolder);
		FileUtils.moveDirectory(tmpFolder, target);
	}
	
	private String jarFilter(String str){
		String regEx="[`~!@#$%^&*()+=|{}';',\\[\\]<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]"; 
		Pattern p = Pattern.compile(regEx); 
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}
	
	
	public void convertAntProject(File projectHome){
		convertAntProject(projectHome, null, null,null,null);
	}
	
	public void convertAntProject(File projectHome,String srcPath,String libPath,String charset,String jdk){
		
		ProjectStyle projectStyle=detectProjectStyle(projectHome,srcPath,libPath);
		
		//check project style
		assemblyMavenStyleProject(projectHome, projectStyle,srcPath,libPath);
		
		Properties props=new Properties();
		File buildPropFile=new File(projectHome,"build.properties");
		boolean completeConfig=false;
		if(buildPropFile.exists()){
			try(FileInputStream inStream=new FileInputStream(buildPropFile)){
				props.load(inStream);
			}catch(Exception e){
				throw new RuntimeException("parse build.properties error");
			}
			if(props.containsKey("app.name")&&props.containsKey("app.version")){
				completeConfig=true;
			}
		}
		if(!completeConfig){
			//just for scan
			try {
				props.setProperty("app.name", projectHome.getCanonicalFile().getName());
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
			props.setProperty("app.version", "1.0-scan");
		}
		
		File webappLib=new File(projectHome,"src/main/webapp/WEB-INF/lib");
		if(!webappLib.exists()){
			throw new IllegalArgumentException("src/main/webapp/WEB-INF/lib doest not exist in "+projectHome.getAbsolutePath());
		}
		try {
			List<Artifact> knowedCentralArtifacts=Lists.newArrayList();
			List<Artifact> knowedNexusArtifacts=Lists.newArrayList();
			List<File> unkownArtifacts=Lists.newArrayList();
			FileUtils.listFiles(webappLib, new String[]{"jar"}, true).forEach(jar->{
				String safeName=jarFilter(jar.getName());
				if(!jar.getName().equals(safeName)){
					try {
						File renamedFile=new File(jar.getParentFile(),safeName);
						FileUtils.moveFile(jar, renamedFile);
						System.out.println("[WARNING]Illegal Jar Name:"+jar.getName()+",Rename to:"+safeName);
						unkownArtifacts.add(renamedFile);
					} catch (Exception e) {
						System.out.println("[WARNING]Illegal Jar Name:"+jar.getName()+"FOUND, Auto Rename Failed!");
					}
				}else{
					unkownArtifacts.add(jar);
				}
			});
			
			Map<String,Object> model=Maps.newHashMap();
			model.put("name", props.getProperty("app.name"));
			model.put("version",props.getProperty("app.version"));
			model.put("centralJars",knowedCentralArtifacts);
			model.put("nexusJars",knowedNexusArtifacts);
			model.put("jdk", jdk==null?"1.6":jdk);
			if(StringUtils.isNotBlank(charset)){
				model.put("encoding", charset.toUpperCase());
			}else{
				model.put("encoding", projectStyle==ProjectStyle.eazybuilder?"UTF-8":"GBK");
			}
			model.put("unkownJars",getJarInfos(unkownArtifacts));
			
			String pom=FreemarkerUtils.processTemplate(configuration.getTemplate("pom.ftl"), model);
			FileUtils.write(new File(projectHome,"pom.xml"), pom, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private void assemblyMavenStyleProject(File projectHome, ProjectStyle projectStyle, String srcPath, String libPath) {
		//非Eazybuilder模板ant工程，检查是否存在：(1)src——源码存放位置 (2)WebContent
		boolean adaptable=false;
		switch(projectStyle){
		case other:
			try {
				File src=new File(projectHome,srcPath);
				if(src.exists()){
					File lib=new File(projectHome,libPath);
					if(lib.exists()){
						adaptable=true;
						moveDirectory(projectHome, src, new File(projectHome,"src/main/java"));
						moveDirectory(projectHome, lib, new File(projectHome,"src/main/webapp/WEB-INF/lib"));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return;
		case eazybuilder:
			return;
		case legacy_Eazybuilder:
			try {
				File src=new File(projectHome,"eazybuilderEJB/src");
				if(src.exists()){
					File lib=new File(projectHome,"lib");
					if(lib.exists()){
						adaptable=true;
						moveDirectory(projectHome, src, new File(projectHome,"src/main/java"));
						moveDirectory(projectHome, lib, new File(projectHome,"src/main/webapp/WEB-INF/lib"));
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case dynamicWebApp:
			try {
				File src=new File(projectHome,"src");
				if(src.exists()){
					File webcontent=new File(projectHome,"WebContent");
					if(webcontent.exists()){
						adaptable=true;
						moveDirectory(projectHome, src, new File(projectHome,"src/main/java"));
						moveDirectory(projectHome, webcontent, new File(projectHome,"src/main/webapp"));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
		if(!adaptable){
			throw new IllegalArgumentException("Project Source Code Structure Not Adaptable!");
		}
		
	}

	
	enum ProjectStyle{
		eazybuilder,
		legacy_Eazybuilder,
		dynamicWebApp,
		other
	}
	
	private ProjectStyle detectProjectStyle(File projectHome,String srcPath,String libPath) {
		if(StringUtils.isNotBlank(srcPath)&&StringUtils.isNotBlank(libPath)){
			return ProjectStyle.other;
		}
		
		if(new File(projectHome,"eazybuilderEJB").exists()){
			return ProjectStyle.legacy_Eazybuilder;
		}
		if(new File(projectHome,"src/main/java").exists()){
			return ProjectStyle.eazybuilder;
		}
		return ProjectStyle.dynamicWebApp;
	}

	private List<JarInfo> getJarInfos(Collection<File> files) {
		List<JarInfo> jars=Lists.newArrayList();
		files.forEach(jar->{
			JarInfo info=new JarInfo();
			String jarName=jar.getName();
			info.setName(jarName.substring(0,jarName.length()-4));
			info.setVersion("1.0");
			info.setFilePath(jar.getAbsolutePath());
			jars.add(info);
		});
		return jars;
	}
	
	
	
	
}
