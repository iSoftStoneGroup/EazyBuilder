package com.eazybuilder.ci.storage.impl;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;

import com.eazybuilder.ci.entity.report.ResourceItem;
import com.eazybuilder.ci.storage.ResourceStorageService;

public class LocalFileStorageService implements ResourceStorageService{

	File baseDir=new File("resources");
	
	@PostConstruct
	public void init(){
		if(!baseDir.exists()){
			baseDir.mkdirs();
		}
	}
	
	public synchronized String save(ResourceItem item) throws IOException{
		long day=LocalDate.now().toEpochDay();
		String fid=UUID.randomUUID().toString();
		
		File dir=new File(baseDir,""+day+"/"+fid);
		if(!dir.exists()){
			dir.mkdirs();
		}
		File file=new File(dir,item.getName());
		file.createNewFile();
		FileUtils.writeByteArrayToFile(file, item.getData());
		
		return day+"/"+fid+"/"+item.getName();
		
	}
	
	public synchronized String save(File file) throws IOException{
		long day=LocalDate.now().toEpochDay();
		String fid=UUID.randomUUID().toString();
		
		File dir=new File(baseDir,""+day+"/"+fid);
		if(!dir.exists()){
			dir.mkdirs();
		}
		if(file.isDirectory()){
			FileUtils.copyDirectory(file, dir);
		}else{
			FileUtils.copyFileToDirectory(file, dir);
		}
		return day+"/"+fid+"/"+file.getName();
	}
	
	public ResourceItem get(String id) throws IOException{
		File file=new File(baseDir,id);
		if(file.isDirectory()) {
			file= ((List<File>)FileUtils.listFiles(file, FileFilterUtils.fileFileFilter(), null)).get(0);
		}
		ResourceItem item=new ResourceItem();
		item.setId(id);
		item.setName(file.getName());
		item.setData(FileUtils.readFileToByteArray(file));
		
		return item;
	}

}
