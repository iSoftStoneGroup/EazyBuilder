package com.eazybuilder.sql.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import org.apache.commons.io.IOUtils;

public class HttpUtil {
	public static final Random r=new Random();

	public static String postJson(String serverUrl,String json) throws Exception{
		String requestUrl=serverUrl;
		if(serverUrl.contains(",")){
			String[] servers=serverUrl.split(",");
			requestUrl=servers[servers.length>1?r.nextInt(servers.length):0];
		}
		URL url=new URL(requestUrl);
	    HttpURLConnection conn=null;
	    try{
	        conn=(HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("POST");
	        conn.setConnectTimeout(5000);
	        conn.setDoInput(true);
	        conn.setDoOutput(true);
	        conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
	        conn.connect();
	        try(OutputStream os=conn.getOutputStream()){
	        	os.write(json.getBytes("utf-8"));
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
	
	public static String getJson(String serverUrl,String param) throws Exception{
		String requestUrl=serverUrl;
		if(serverUrl.contains(",")){
			String[] servers=serverUrl.split(",");
			requestUrl=servers[servers.length>1?r.nextInt(servers.length):0];
		}
		URL url=new URL(requestUrl+"?"+param);
	    HttpURLConnection conn=null;
	    try{
	        conn=(HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("GET");
	        conn.setConnectTimeout(5000);
	        conn.setDoInput(true);
	        conn.setDoOutput(false);
	        conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
	        conn.connect();
	        try(InputStream is=conn.getInputStream()){
	        	ByteArrayOutputStream baos=new ByteArrayOutputStream();
	        	IOUtils.copy(is, baos);
	        	return baos.toString("utf-8");
	        }
	    }finally{
	        if(conn!=null){
	            conn.disconnect();
	        }
	    }
	}
}
