package com.eazybuilder.ci.ant.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.IOUtils;

public class HttpUtil {

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
	
	public static String get(String serverUrl) throws Exception{
		URL url=new URL(serverUrl);
	    HttpURLConnection conn=null;
	    try{
	        conn=(HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("GET");
	        conn.setConnectTimeout(5000);
	        conn.setDoInput(true);
	        conn.setRequestProperty("Accept", "application/json");
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
