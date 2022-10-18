package com.eazybuilder.ci.collector.sonar;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eazybuilder.ci.collector.sonar.client.BaseHttpClient;

	

public class SonarServer {
    private final Logger logger;
    private final BaseHttpClient client;

    public SonarServer(URI serverUri, String username, String passwordOrToken) {
        this.logger = LoggerFactory.getLogger(this.getClass());
        System.out.println("serverUri---"+serverUri);
        System.out.println("username---"+username);
        System.out.println("passwordOrToken---"+passwordOrToken);
        this.client = new BaseHttpClient(serverUri, username, passwordOrToken);
    }
    
    public boolean checkTaskStatus(String url) throws IOException, URISyntaxException{
    	Map taskMap=client.get(url,Map.class);
    	if("IN_PROGRESS".equals(((Map)taskMap.get("task")).get("status"))){
    		return false;
    	}
    	return true;
    }

	public String getTestSuccess(String projectKey, String branch) throws IOException, URISyntaxException{
		String url="/api/measures/component?metricKeys=test_success_density&component="+projectKey+"&branch="+branch;
		Map result=client.get(url, Map.class);
		Map component=(Map) result.get("component");
		List<Map> measures=(List<Map>)component.get("measures");
		if(measures!=null&&measures.size()>0){
			return String.valueOf(measures.get(0).get("value"));
		}else{
			return "0";
		}
	}

    public String getCoverage(String projectKey, String branch) throws IOException, URISyntaxException{
		String url="/api/measures/component?metricKeys=coverage&component="+projectKey+"&branch="+branch;
		Map result=client.get(url, Map.class);
		Map component=(Map) result.get("component");
		List<Map> measures=(List<Map>)component.get("measures");
		if(measures!=null&&measures.size()>0){
			return String.valueOf(measures.get(0).get("value"));
		}else{
			return "0";
		}
	}
    /**
     * 获取代码行数
     * @param projectKey
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */
    public String getCodeLineNumbers(String projectKey, String branch) throws IOException, URISyntaxException{
//    	http://0.0.0.0/sonarqube/api/measures/component?metricKeys=ncloc&component=com.eazybulider:iss-workflow
//    	{"component":{"id":"AWQcPencuakdCDmv4422","key":"com.eazybulider:iss-workflow","name":"iss-workflow","qualifier":"TRK","measures":[{"metric":"ncloc","value":"30708"}]}}
    	String url="/api/measures/component?metricKeys=ncloc&component="+projectKey+"&branch="+branch;
    	Map result=client.get(url, Map.class);
    	Map component=(Map) result.get("component");
    	List<Map> measures=(List<Map>)component.get("measures");
    	if(measures!=null&&measures.size()>0){
    		return String.valueOf(measures.get(0).get("value"));
    	}else{
    		return "0";
    	}
    }

	/**
	 * 获取新单元测试覆盖率
	 * @param projectKey
	 * @return
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public String getNewCoverage(String projectKey, String branch) throws IOException, URISyntaxException{
		String url="/api/measures/component?metricKeys=new_coverage&component="+projectKey+"&branch="+branch;
		Map result=client.get(url, Map.class);
		Map component=(Map) result.get("component");
		List<Map> measures=(List<Map>)component.get("measures");
		if((measures!=null&&measures.size()>0) &&(measures.get(0).get("period")!=null)){
//			if(null!=measures.get(0).get("value")) {
//			return String.valueOf(measures.get(0).get("value"));
//			}else {
//				return "0";
//			}
			Map innerMap=(Map) measures.get(0).get("period");
			return String.valueOf(innerMap.get("value"));

		}else{
			return "0";
		}
	}

	/**
	 * 获取代码行数
	 * @param projectKey
	 * @return
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public String getNewLineNumbers(String projectKey, String branch) throws IOException, URISyntaxException{
		String url="/api/measures/component?metricKeys=new_lines&component="+projectKey+"&branch="+branch;
		Map result=client.get(url, Map.class);
		Map component=(Map) result.get("component");
		List<Map> measures=(List<Map>)component.get("measures");
		if((measures!=null&&measures.size()>0) &&(measures.get(0).get("period")!=null)){
//			if(null!=measures.get(0).get("value")) {
//			return String.valueOf(measures.get(0).get("value"));
//			}else {
//				return "0";
//			}
			Map innerMap=(Map) measures.get(0).get("period");
			return String.valueOf(innerMap.get("value"));
			
		}else{
			return "0";
		}
	}

	public String getNewUncoveredLines(String projectKey, String branch) throws IOException, URISyntaxException{
		String url="/api/measures/component?metricKeys=new_uncovered_lines&component="+projectKey+"&branch="+branch;
		Map result=client.get(url, Map.class);
		Map component=(Map) result.get("component");
		List<Map> measures=(List<Map>)component.get("measures");
		if((measures!=null&&measures.size()>0) &&(measures.get(0).get("period")!=null)){
//			if(null!=measures.get(0).get("value")) {
//			return String.valueOf(measures.get(0).get("value"));
//			}else {
//				return "0";
//			}
			Map innerMap=(Map) measures.get(0).get("period");
			return String.valueOf(innerMap.get("value"));

		}else{
			return "0";
		}
	}

	public String getNewBugNumbers(String projectKey, String branch) throws IOException, URISyntaxException{
		String url="/api/measures/component?metricKeys=new_bugs&component="+projectKey+"&branch="+branch;
		Map result=client.get(url, Map.class);
		Map component=(Map) result.get("component");
		List<Map> measures=(List<Map>)component.get("measures");
		if((measures!=null&&measures.size()>0) &&(measures.get(0).get("period")!=null)){
//			if(null!=measures.get(0).get("value")) {
//			return String.valueOf(measures.get(0).get("value"));
//			}else {
//				return "0";
//			}
			Map innerMap=(Map) measures.get(0).get("period");
			return String.valueOf(innerMap.get("value"));

		}else{
			return "0";
		}
	}

	public String getNewVulnerabilitieNumbers(String projectKey, String branch) throws IOException, URISyntaxException{
		String url="/api/measures/component?metricKeys=new_vulnerabilities&component="+projectKey+"&branch="+branch;
		Map result=client.get(url, Map.class);
		Map component=(Map) result.get("component");
		List<Map> measures=(List<Map>)component.get("measures");
		if((measures!=null&&measures.size()>0) &&(measures.get(0).get("period")!=null)){
//			if(null!=measures.get(0).get("value")) {
//			return String.valueOf(measures.get(0).get("value"));
//			}else {
//				return "0";
//			}
			Map innerMap=(Map) measures.get(0).get("period");
			return String.valueOf(innerMap.get("value"));

		}else{
			return "0";
		}
	}

	public String getNewCodeSmellNumbers(String projectKey, String branch) throws IOException, URISyntaxException{
		String url="/api/measures/component?metricKeys=new_code_smells&component="+projectKey+"&branch="+branch;
		Map result=client.get(url, Map.class);
		Map component=(Map) result.get("component");
		List<Map> measures=(List<Map>)component.get("measures");
		if((measures!=null&&measures.size()>0) &&(measures.get(0).get("period")!=null)){
//			if(null!=measures.get(0).get("value")) {
//			return String.valueOf(measures.get(0).get("value"));
//			}else {
//				return "0";
//			}
			Map innerMap=(Map) measures.get(0).get("period");
			return String.valueOf(innerMap.get("value"));

		}else{
			return "0";
		}
	}

	
	/**
	 * 技术债
	 * @param projectKey
	 * @return
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public String getTechnologyDept(String projectKey, String branch) throws IOException, URISyntaxException{
		String url="/api/measures/component?metricKeys=sqale_index&component="+projectKey+"&branch="+branch;
		Map result=client.get(url, Map.class);
		Map component=(Map) result.get("component");
		List<Map> measures=(List<Map>)component.get("measures");
		if(measures!=null&&measures.size()>0){
			return String.valueOf(measures.get(0).get("value"));
		}else{
			return "0";
		}
	}
	
	
	/**
	 * 新增代码技术债
	 * @param projectKey
	 * @return
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public String getNewCodeTechnologyDept(String projectKey, String branch) throws IOException, URISyntaxException{
		String url="/api/measures/component?metricKeys=new_technical_debt&component="+projectKey+"&branch="+branch;
		Map result=client.get(url, Map.class);
		Map component=(Map) result.get("component");
		List<Map> measures=(List<Map>)component.get("measures");
		if((measures!=null&&measures.size()>0) &&(measures.get(0).get("period")!=null)){
//			if(null!=measures.get(0).get("value")) {
//			return String.valueOf(measures.get(0).get("value"));
//			}else {
//				return "0";
//			}
			Map innerMap=(Map) measures.get(0).get("period");
			return String.valueOf(innerMap.get("value"));

		}else{
			return "0";
		}
	}
	
	
    public Serverities getServerity(String projectKey,QualityType type, String branch) throws IOException, URISyntaxException{
    	String url="/api/issues/search?facets=severities&resolved=no&facetMode=count&ps=1&componentKeys="+projectKey+"&types="+type.name()+"&branch="+branch;
    	Map result=client.get(url, Map.class);
    	List facets=(List) result.get("facets");
    	Map serverity=(Map) facets.get(0);
    	List<Map> values=(List<Map>)serverity.get("values");
    	
    	final Serverities serv=new Serverities();
    	serv.setType(type);
    	for(Map value:values){
    		switch((String)value.get("val")){
    		case "MAJOR":
    			serv.setMajor(((Double)value.get("count")).intValue());
    			break;
    		case "INFO":
    			serv.setInfo(((Double)value.get("count")).intValue());
    			break;
    		case "MINOR":
    			serv.setMinor(((Double)value.get("count")).intValue());
    			break;
    		case "CRITICAL":
    			serv.setCritical(((Double)value.get("count")).intValue());
    			break;
    		case "BLOCKER":
    			serv.setBlocker(((Double)value.get("count")).intValue());
    			break;
    		}
    	}
    	return serv;
    }

}
