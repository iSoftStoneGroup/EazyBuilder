package com.eazybuilder.ci.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.io.FileUtils;

import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.impl.FileVolumeManager;
import com.github.junrar.rarfile.FileHeader;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;

public class ZipUtils {
	
	public static void zipAll(ArrayList<File> files,File target) throws ZipException{
		ZipFile zf=new ZipFile(target);
		zf.setFileNameCharset("GBK");
		zf.createZipFile(files, new ZipParameters());
	}

	public static void unzipAny(File path)throws Exception{
		Collection<File> files=FileUtils.listFiles(path, new String[]{"zip"}, true);
		for(File file:files){
			extractZip(file);
		}
	}
	
	public static void unCompressAny(File path)throws Exception{
		Collection<File> files=FileUtils.listFiles(path, new String[]{"zip"}, true);
		for(File file:files){
			if(file.getName().endsWith(".zip")){
				extractZip(file);
			}else if(file.getName().endsWith(".rar")){
				extractRar(file);
			}
		}
	}

	public static String extract(File file) throws ZipException {
		ZipFile zf=new ZipFile(file);
		String fileName=file.getName();
		String path=new File(file.getParentFile(),fileName.substring(0, fileName.length()-4)).getAbsolutePath();
		zf.extractAll(path);
		return path;
	}
	
	public static void extractZip(File file) throws ZipException {
		ZipFile zf=new ZipFile(file);
//		zf.setFileNameCharset("GBK");
		String fileName=file.getName();
		zf.extractAll(new File(file.getParentFile(),fileName.substring(0, fileName.length()-4)).getAbsolutePath());
		
		ZipFile zfGbk=new ZipFile(file);
		zfGbk.setFileNameCharset("GBK");
		zfGbk.extractAll(new File(file.getParentFile(),fileName.substring(0, fileName.length()-4)+"_GBK").getAbsolutePath());
		
	}
	
	public static void unrarAny(File path)throws Exception{
		Collection<File> files=FileUtils.listFiles(path, new String[]{"rar"}, true);
		for(File file:files){
			extractRar(file);
		}
	}

	private static void extractRar(File file) throws RarException, IOException, FileNotFoundException {
		try(Archive archive=new Archive(new FileVolumeManager(file))){
			String fileName=file.getName();
			if (archive != null) {
				File base=new File(file.getParentFile(),
						fileName.substring(0, fileName.length()-4));
				base.mkdir();
				FileHeader fh = archive.nextFileHeader();
				while(fh!=null){
					File extractFile=new File(base,fh.getFileNameW());
					if(extractFile.exists()){
						break;
					}
					extractFile.getParentFile().mkdirs();
					try(OutputStream os=new FileOutputStream(extractFile)){
						archive.extractFile(fh, os);
					}
					fh = archive.nextFileHeader();
				}
			}
		}
	}
}
