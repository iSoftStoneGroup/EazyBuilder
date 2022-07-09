package com.eazybuilder.ci.collector.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.commons.io.IOUtils;

public class HttpUtil {
	
	public static String getRootUrl(String baseUrl) {
		try {
			URL url=new URL(baseUrl);
			int port=url.getPort()<0?url.getDefaultPort():url.getPort();
			if(port>0) {
				return url.getProtocol()+"://"+url.getHost()+":"+port;
			}else {
				return url.getProtocol()+"://"+url.getHost();
			}
		}catch(Exception e) {
			return baseUrl;
		}
	}
	
	public static void downloadFileStream(String serverUrl,File toFile)throws Exception{
		URL url=new URL(serverUrl);
	    HttpURLConnection conn=null;
	    try{
	        conn=(HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("GET");
	        conn.setConnectTimeout(5000);
	        conn.setDoInput(true);
	        conn.connect();
	        
	        try(InputStream is=conn.getInputStream();FileOutputStream fos=new FileOutputStream(toFile)){
	        	IOUtils.copy(is, fos);
	        }
	    }finally{
	        if(conn!=null){
	            conn.disconnect();
	        }
	    }
	}

	public static String postJson(String serverUrl,String json) throws Exception{
		URL url=new URL(serverUrl);
	    HttpURLConnection conn=null;
	    try{
	        conn=(HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("POST");
	        conn.setConnectTimeout(5000);
	        conn.setDoInput(true);
	        conn.setDoOutput(true);
	        conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
	        conn.connect();
	        try(OutputStream os=conn.getOutputStream();){
	        	os.write(json.getBytes("utf-8"));
	        	os.flush();
	        	try {
	        		try(InputStream is=conn.getInputStream()){
	        			ByteArrayOutputStream baos=new ByteArrayOutputStream();
	        			IOUtils.copy(is, baos);
	        			return baos.toString("utf-8");
	        		}
				} catch (FileNotFoundException e) {
					return "";
				}
	        }
	    }finally{
	        if(conn!=null){
	            conn.disconnect();
	        }
	    }
	}
	
	public static String postFileStream(String serverUrl,File file) throws Exception{
		URL url=new URL(serverUrl);
	    HttpURLConnection conn=null;
	    try{
	        conn=(HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("POST");
	        conn.setConnectTimeout(5000);
	        conn.setDoOutput(true);
	        conn.setRequestProperty("X-FILE-NAME", URLEncoder.encode(file.getName(),"UTF-8"));
	        conn.setRequestProperty("Content-Type", "application/octet-stream;charset=UTF-8");
	        conn.connect();
	        try(OutputStream os=conn.getOutputStream();InputStream fis=new FileInputStream(file)){
	        	IOUtils.copy(fis, os);
	        	os.flush();
	        	try(InputStream is=conn.getInputStream()){
	        		ByteArrayOutputStream baos=new ByteArrayOutputStream();
	        		IOUtils.copy(is, baos);
	        		return baos.toString("utf-8");
	        	}
	        }
	    }finally{
	        if(conn!=null){
	            conn.disconnect();
	        }
	    }
	}
}
