package com.eazybuilder.sql;

import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.eazybuilder.ci.sql.vo.ProjectSQLBatchInfo;
import com.eazybuilder.ci.sql.vo.SQLSource;
import com.eazybuilder.sql.impl.ibatis.IBatisSQLExtractor;
import com.eazybuilder.sql.util.HttpUtil;
import com.eazybuilder.sql.util.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProjectSQLScanner {
	private static final long TIME_OUT=30*60*1000L;//30min
	private static final List<SQLExtractor> extractors=Arrays.asList(new IBatisSQLExtractor());
	private static final String VALIDATOR_SERVICE_URL="http://ci-sqlscan-headless:8081/sql-validate";
	private static Logger logger= LoggerFactory.getLogger(ProjectSQLScanner.class);
	public static void main(String[] args) {
		logger.info("VALIDATOR_SERVICE_URL: "+VALIDATOR_SERVICE_URL);
		logger.info("VALIDATOR_SERVICE_URL:{}",VALIDATOR_SERVICE_URL);
		String scanPath="./src";
		String projectName="SQLScan";
		if(args!=null&&args.length>1&&args[0]!=null) {
			scanPath=args[0];
			projectName=args[1];
		}
		File scanHome=new File(scanPath);
		Collection<File> xmlFiles=FileUtils.listFiles(scanHome, new String[]{"xml"}, true);
		ProjectSQLBatchInfo sqlInfo=new ProjectSQLBatchInfo();
		sqlInfo.setSqlSources(new LinkedList<>());
		sqlInfo.setProjectName(projectName);
		xmlFiles.forEach(file->{
			extractors.forEach(extractor->{
				if(extractor.match(file)) {
					try {
						Map<String,String> sqlMap=extractor.extractSQL(new FileInputStream(file), "utf-8");
						if(sqlMap!=null&&sqlMap.size()>0) {
							SQLSource source=new SQLSource();
							source.setSource(getFileRelativePath(scanHome.getAbsolutePath(),file.getAbsolutePath()));
							source.setSqlMap(sqlMap);
							sqlInfo.getSqlSources().add(source);
						}
					} catch (Exception e) {
						System.err.println(file.getAbsolutePath()+":"+e.getMessage());
					}
				}
			});
		});
		try {
			String jobId=HttpUtil.postJson(VALIDATOR_SERVICE_URL+"/batch", JsonMapper.allFieldMapper().toJson(sqlInfo));
			String status=null;
			long startTime=System.currentTimeMillis();
			do {
				status=HttpUtil.getJson(VALIDATOR_SERVICE_URL+"/batch/status", "jobId="+jobId);
				Thread.sleep(5*1000);
				if(System.currentTimeMillis()-TIME_OUT>startTime) {
					//wait timeout
					break;
				}
			}while(!"\"DONE\"".equals(status));
			
            String result=HttpUtil.getJson(VALIDATOR_SERVICE_URL+"/batch", "jobId="+jobId);
            File dir=new File("target/sql-scan");
            dir.delete();
            dir.mkdirs();
            FileUtils.write(new File(dir,"sql-scan-result.json"), result,"utf-8");
            String html=HttpUtil.postJson(VALIDATOR_SERVICE_URL+"/report/html?projectName="+URLEncoder.encode(projectName, "ISO-8859-1"), result);
            FileUtils.write(new File(dir,"sql-scan-result.html"), html,"utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private static String getFileRelativePath(String scanHome, String absolutePath) {
		if(absolutePath.contains(scanHome)) {
			return absolutePath.substring(absolutePath.indexOf(scanHome)+scanHome.length());
		}
		return absolutePath;
	}
}
