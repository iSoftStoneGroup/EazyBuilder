package com.eazybuilder.ci.ant.util;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;

import com.fasterxml.jackson.databind.JavaType;
import com.google.common.collect.Maps;
import com.eazybuilder.ci.dto.Artifact;
import com.eazybuilder.ci.dto.Doc;
import com.eazybuilder.ci.dto.SearchResult;
/**
 * 通过jar checksum检索https://search.maven.org/ 得到pom
 * 

 *
 */
public class CentralMavenSearchService {
	static final String SEARCH_URL="https://search.maven.org/solrsearch/select?q=1:{0}&wt=json";
	static Map<String,Artifact> result_cache=Maps.newConcurrentMap();
	static{
		File file=new File(System.getProperty("java.io.tmpdir")+"/central-cache.data");
		if(file.exists()){
			JsonMapper mapper=JsonMapper.nonDefaultMapper();
			JavaType type=mapper.contructMapType(HashMap.class, String.class, Doc.class);
			try {
				result_cache.putAll(mapper.fromJson(FileUtils.readFileToString(file,"utf-8"),type));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static Artifact search(File jar) throws Exception{
		String checksum=DigestUtils.sha1Hex(FileUtils.readFileToByteArray(jar));
		if(result_cache.containsKey(checksum)){
			return result_cache.get(checksum);
		}
		String url=MessageFormat.format(SEARCH_URL, checksum);
		String json=HttpUtil.get(url);
		SearchResult result=JsonMapper.nonDefaultMapper().fromJson(json, SearchResult.class);
		Doc doc=result.getDoc();
		if(doc!=null){
			System.out.println("groupId:"+doc.getG());
			System.out.println("artifactId:"+doc.getA());
			System.out.println("version:"+doc.getV());
			Artifact artifact=doc.toArtifact();
			result_cache.put(checksum, artifact);
			return artifact;
		}
		return null;
	}
	
	public static void updateCache() throws IOException{
		FileUtils.write(new File(System.getProperty("java.io.tmpdir")+"/central-cache.data"), JsonMapper.nonDefaultMapper().toJson(result_cache),"utf-8");
	}
}
