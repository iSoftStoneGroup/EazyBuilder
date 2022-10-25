package com.eazybuilder.ci.ant.util;

import java.io.File;
import java.io.FileNotFoundException;
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
import com.eazybuilder.ci.dto.NexusAnswer;

public class NexusSearchService {

	private static final String SEARCH_URL=System.getProperty("nexus.url", "http://10.9.80.149:8082/nexus")+"/service/local/identify/sha1/{0}";
	
	static Map<String,Artifact> result_cache=Maps.newConcurrentMap();
	static{
		File file=new File(System.getProperty("java.io.tmpdir")+"/nexus-cache.data");
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
		try {
			String json=HttpUtil.get(url);
			NexusAnswer result=JsonMapper.nonDefaultMapper().fromJson(json, NexusAnswer.class);
			System.out.println("groupId:"+result.getGroupId());
			System.out.println("artifactId:"+result.getArtifactId());
			System.out.println("version:"+result.getVersion());
			Artifact artifact=result.toArtifact();
			result_cache.put(checksum,artifact );
			return artifact;
		} catch (FileNotFoundException e) {
			return null;
		}
	}
	
	public static void updateCache() throws IOException{
		FileUtils.write(new File(System.getProperty("java.io.tmpdir")+"/nexus-cache.data"), JsonMapper.nonDefaultMapper().toJson(result_cache),"utf-8");
	}
}
