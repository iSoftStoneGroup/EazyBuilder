package com.eazybuilder.ci.scm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CIFileResourceDownloader implements ResourceDownloader{

	private static Logger logger=LoggerFactory.getLogger(CIFileResourceDownloader.class);
	
	private String downloadApiUrl;
	
	@Override
	public void checkout(String url, String user, String password, File toPath) throws Exception {
		URL fullUrl=new URL(downloadApiUrl+"?fileId="+url);
		logger.info("resource url:"+fullUrl);
		HttpURLConnection conn=null;
		try{
			if(!toPath.exists()){
				toPath.mkdir();
			}
			
			conn=(HttpURLConnection)fullUrl.openConnection();
			conn.setDoInput(true);
			conn.connect();
			String fileName="testScript.zip";
			List<String> disposition=conn.getHeaderFields().get("Content-Disposition");
			if(disposition!=null&&disposition.size()>0){
				String text=disposition.get(0);
				logger.info("Content-Disposition: "+text);
				fileName=URLDecoder.decode(text.substring(text.lastIndexOf("=")+2,text.length()-1),"utf-8");
				logger.info("Original fileName: "+fileName);
			}
			File file=new File(toPath,fileName);
			try(InputStream is=conn.getInputStream();OutputStream os=new FileOutputStream(file);){
				IOUtils.copyLarge(is, os);
			}
		}finally{
			conn.disconnect();
		}
	}

	public String getDownloadApiUrl() {
		return downloadApiUrl;
	}
	public void setDownloadApiUrl(String downloadApiUrl) {
		this.downloadApiUrl = downloadApiUrl;
	}

}
