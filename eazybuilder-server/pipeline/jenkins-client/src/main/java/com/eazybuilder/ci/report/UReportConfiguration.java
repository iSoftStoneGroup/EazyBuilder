package com.eazybuilder.ci.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Component;

import com.bstek.ureport.console.UReportServlet;
import com.bstek.ureport.definition.datasource.BuildinDatasource;
import com.bstek.ureport.exception.ReportException;
import com.bstek.ureport.provider.report.ReportFile;
import com.bstek.ureport.provider.report.ReportProvider;

@Configuration
@ConditionalOnClass(name = "com.bstek.ureport.console.UReportServlet")
@ConditionalOnProperty(name = "ci.report.enable",havingValue = "true")
@ImportResource("classpath:ureport-console-context.xml")
public class UReportConfiguration {
	/**
	 * UReport2 Servlet
	 */
	@Bean
	public ServletRegistrationBean ureportServlet() {
		return new ServletRegistrationBean(
				new UReportServlet(), "/ureport/*");
	}
	
	
	@Component
	public static class UReportHPADefaultDataSource implements BuildinDatasource{

		@Autowired
		DataSource defaultDataSource;
		
		@Override
		public Connection getConnection() {
			try {
				return defaultDataSource.getConnection();
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		public String name() {
			return "CI平台数据源";
		}
		
	}
	
	@Component
	public static class UReportHPALocalStorageProvider implements ReportProvider,ApplicationContextAware {

		private String prefix="ci-local:";
		private String fileStoreDir="config/reports";
		private boolean disabled;
		@Override
		public InputStream loadReport(String file) {
			if(file.startsWith(prefix)){
				file=file.substring(prefix.length(),file.length());
			}
			String fullPath=getStoreDir()+"/"+file;
			try {
				return new FileInputStream(fullPath);
			} catch (FileNotFoundException e) {
				throw new ReportException(e);
			}
		}
		
		@Override
		public void deleteReport(String file) {
			if(file.startsWith(prefix)){
				file=file.substring(prefix.length(),file.length());
			}
			String fullPath=getStoreDir()+"/"+file;
			File f=new File(fullPath);
			if(f.exists()){
				f.delete();
			}
		}

		@Override
		public List<ReportFile> getReportFiles() {
			File file=new File(getStoreDir());
			List<ReportFile> list=new ArrayList<ReportFile>();
			for(File f:file.listFiles()){
				Calendar calendar=Calendar.getInstance();
				calendar.setTimeInMillis(f.lastModified());
				list.add(new ReportFile(f.getName(),calendar.getTime()));
			}
			Collections.sort(list, new Comparator<ReportFile>(){
				@Override
				public int compare(ReportFile f1, ReportFile f2) {
					return f2.getUpdateDate().compareTo(f1.getUpdateDate());
				}
			});
			return list;
		}

		@Override
		public String getName() {
			return "项目报表存储";
		}
		
		@Override
		public void saveReport(String file,String content) {
			if(file.startsWith(prefix)){
				file=file.substring(prefix.length(),file.length());
			}
			String fullPath=getStoreDir()+"/"+file;
			FileOutputStream outStream=null;
			try{
				outStream=new FileOutputStream(new File(fullPath));
				IOUtils.write(content, outStream,"utf-8");
			}catch(Exception ex){
				throw new ReportException(ex);
			}finally{
				if(outStream!=null){
					try {
						outStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			
		}

		public String getStoreDir() {
			return this.fileStoreDir;
		}
		@Override
		public boolean disabled() {
			return disabled;
		}
		
		public void setDisabled(boolean disabled) {
			this.disabled = disabled;
		}
		
		public void setFileStoreDir(String fileStoreDir) {
			this.fileStoreDir = fileStoreDir;
		}
		
		@Override
		public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
			File file=new File(fileStoreDir);
			if(file.exists()){
				return;
			}
			file.mkdirs();
		}

		@Override
		public String getPrefix() {
			return prefix;
		}
	}
}
