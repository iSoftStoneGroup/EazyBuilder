package com.eazybuilder.ci.storage.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;

import com.eazybuilder.ci.entity.report.ResourceItem;
import com.eazybuilder.ci.storage.ResourceStorageService;
import com.eazybuilder.ci.util.ZipUtil;
import com.obs.services.ObsClient;
import com.obs.services.model.ObjectMetadata;
import com.obs.services.model.ObsObject;

public class HWCloudOBSStorageService implements ResourceStorageService{


	@Value("${ci.storage.hw-obs.endpointUrl}")
	private String endpointUrl;
	
	@Value("${ci.storage.hw-obs.ak}")
	private String accessKey;
	
	@Value("${ci.storage.hw-obs.sk}")
	private String secretKey;
	
	@Value("${ci.storage.hw-obs.bucketName}")
	private String bucketName;
	
	public ObsClient  getClient() {
		return new ObsClient(accessKey, secretKey, endpointUrl);
	}
	
	
	@Override
	public String save(ResourceItem item) throws IOException {
		try(ObsClient client=getClient()){
			String fid=UUID.randomUUID().toString();
			ObjectMetadata metaData=new ObjectMetadata();
			metaData.addUserMetadata("name", item.getName());
			if(item.getData()!=null) {
				client.putObject(bucketName, fid, new ByteArrayInputStream(item.getData()),metaData);
			}else if(item.getDataStream()!=null) {
				client.putObject(bucketName, fid, item.getDataStream(), metaData);
			}
			return fid;
		}
	}

	@Override
	public String save(File file) throws IOException {
		try(ObsClient client=getClient()){
			String fid=UUID.randomUUID().toString();
			ObjectMetadata metaData=new ObjectMetadata();
			metaData.addUserMetadata("name", file.getName());
			if(file.isDirectory()) {
				File zipFile=new File(FileUtils.getTempDirectory(),"file-"+System.currentTimeMillis()+".zip");
				ZipUtil.pack(file.getAbsolutePath(), zipFile.getAbsolutePath());
				client.putObject(bucketName, fid, zipFile,metaData);
				zipFile.delete();
			}else {
				client.putObject(bucketName, fid, file,metaData);
			}
			return fid;
		}
	}

	@Override
	public ResourceItem get(String id) throws IOException {
		try(ObsClient client=getClient()){
			ObsObject object=client.getObject(bucketName, id);
			ByteArrayOutputStream baos=new ByteArrayOutputStream();
			IOUtils.copy(object.getObjectContent(), baos);
			
			String name=(String)object.getMetadata().getUserMetadata("name");
			ResourceItem item=new ResourceItem();
			item.setId(id);
			item.setName(name);
			item.setData(baos.toByteArray());
			return item;
		}
	}

}
