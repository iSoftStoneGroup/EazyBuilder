package com.eazybuilder.ci.maven;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;


@WebServlet(urlPatterns="/repo/maven")
@Component
public class RepoManageServlet extends HttpServlet {

	private static final Logger LOG = LoggerFactory.getLogger(RepoManageServlet.class);

	private static final long serialVersionUID = -8453502699403909016L;

	enum Mode {
		list, rename, copy, remove, savefile, editfile, addfolder, changepermissions, compress, extract,getContent
	}

	@Value("${maven.local_repo:./}")
	private String REPOSITORY_BASE_URL;
	
	private String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss"; // (2001-07-04 12:08:56)


	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Catch download requests
		String path = request.getParameter("path");
		String teamId=request.getParameter("teamId");
		getFile(teamId,response, path);

	}

	private void getFile(String teamId,HttpServletResponse response, String path) throws IOException {
		path=path.replaceAll("\\.\\.", "");
		File file = new File(REPOSITORY_BASE_URL+"/teams/"+teamId, path);
		LOG.info("download:"+file.getAbsolutePath());
		if (!file.isFile()) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "Resource Not Found");
			return;
		}

		// se imageName non ha estensione o non è immagine sballa! ;)
		// response.setHeader("Content-Type", getServletContext().getMimeType(imageName));
		response.setHeader("Content-Type", getServletContext().getMimeType(file.getName()));
		response.setHeader("Content-Length", String.valueOf(file.length()));
		response.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");

		FileInputStream input = null;
		BufferedOutputStream output = null;
		try {

			input = new FileInputStream(file);
			output = new BufferedOutputStream(response.getOutputStream());
			byte[] buffer = new byte[8192];
			for (int length = 0; (length = input.read(buffer)) > 0;) {
				output.write(buffer, 0, length);
			}
		} catch (Throwable t) {
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException logOrIgnore) {
				}
			}
			if (input != null) {
				try {
					input.close();
				} catch (IOException logOrIgnore) {
				}
			}
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			fileOperation(request, response);
		} catch (Throwable t) {
			setError(t, response);
		}

	}

	private void setError(Throwable t, HttpServletResponse response) throws IOException {
		try {
			// { "result": { "success": false, "error": "message" } }
			JSONObject responseJsonObject = error(t.getMessage());
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			out.print(responseJsonObject);
			out.flush();
		} catch (Throwable x) {
			response.sendError(HttpStatus.SC_INTERNAL_SERVER_ERROR, x.getMessage());
		}

	}


	private void fileOperation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject responseJsonObject = null;
		try {
			// legge il parametro json
			StringBuilder sb = new StringBuilder();
			BufferedReader br = request.getReader();
			String str;
			while ((str = br.readLine()) != null) {
				sb.append(str);
			}
			br.close();
			
			String teamId=request.getParameter("teamId");
			
			JSONObject jObj = JSONObject.parseObject(sb.toString());
			// legge mode e chiama il metodo aapropriato
			JSONObject params = jObj;
			Mode mode = Mode.valueOf(params.getString("action"));
			switch (mode) {
				case remove:
					responseJsonObject = delete(teamId,params);
					break;
				case list:
					responseJsonObject = list(teamId,params);
					break;
				default:
					throw new ServletException("not implemented");
			}
			if (responseJsonObject == null) {
				responseJsonObject = error("generic error : responseJsonObject is null");
			}
		} catch (Exception e) {
			responseJsonObject = error(e.getMessage());
		}
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		out.print(responseJsonObject);
		out.flush();
	}

	private JSONObject list(String teamId, JSONObject params) throws ServletException {
		try {
			String path = params.getString("path");
			path=path.replaceAll("\\.\\.", "");
			
			File dir = new File(REPOSITORY_BASE_URL+"/teams/"+teamId, path);
			LOG.info("LIST REPO:"+dir.getAbsolutePath());
			File[] fileList = dir.listFiles();

			List<JSONObject> resultList = new ArrayList<JSONObject>();
			SimpleDateFormat dt = new SimpleDateFormat(DATE_FORMAT);
			if (fileList != null) {
				// Calendar cal = Calendar.getInstance();
				for (File f : fileList) {
					if (!f.exists()) {
						continue;
					}
					BasicFileAttributes attrs = Files.readAttributes(f.toPath(), BasicFileAttributes.class);
					JSONObject el = new JSONObject();
					el.put("name", f.getName());
					el.put("rights", getPermissions(f));
					el.put("date", dt.format(new Date(attrs.lastModifiedTime().toMillis())));
					el.put("size", f.length());
					el.put("type", f.isFile() ? "file" : "dir");
					resultList.add(el);
				}
			}
			JSONObject wrap=new JSONObject();
			wrap.put("result", resultList);
			return wrap;
		} catch (Exception e) {
			LOG.error("list", e);
			return error(e.getMessage());
		}
	}


	private JSONObject delete(String teamId, JSONObject params) throws ServletException {
		try {
			JSONArray pathArray=params.getJSONArray("items");
			for(Object pathObject:pathArray){
				String path = pathObject.toString();
				path=path.replaceAll("\\.\\.", "");
				LOG.debug("delete {}", path);
				File srcFile = new File(REPOSITORY_BASE_URL+"/teams/"+teamId, path);
				LOG.info("DELETE REPO FILE:"+srcFile.getAbsolutePath());
				if (!FileUtils.deleteQuietly(srcFile)) {
					throw new Exception("Can't delete: " + srcFile.getAbsolutePath());
				}
			}
			return success(params);
		} catch (Exception e) {
			LOG.error("delete", e);
			return error(e.getMessage());
		}
	}

	
	private boolean isWindows(){
		return System.getProperty("os.name").toLowerCase().startsWith("win");
	}


	private String getPermissions(File f) throws IOException {
		// http://www.programcreek.com/java-api-examples/index.php?api=java.nio.file.attribute.PosixFileAttributes
		if(isWindows()){
			return "";
		}else{
			PosixFileAttributeView fileAttributeView = Files.getFileAttributeView(f.toPath(), PosixFileAttributeView.class);
			PosixFileAttributes readAttributes = fileAttributeView.readAttributes();
			Set<PosixFilePermission> permissions = readAttributes.permissions();
			return PosixFilePermissions.toString(permissions);
		}
	}


	private JSONObject error(String msg) throws ServletException {
		try {
			// { "result": { "success": false, "error": "msg" } }
			JSONObject result = new JSONObject();
			result.put("success", false);
			result.put("error", msg);
			JSONObject wrap=new JSONObject();
			wrap.put("result", result);
			return wrap;
		} catch (JSONException e) {
			throw new ServletException(e);
		}
	}

	private JSONObject success(JSONObject params) throws ServletException {
		try {
			// { "result": { "success": true, "error": null } }
			JSONObject result = new JSONObject();
			result.put("success", true);
			result.put("error", (Object) null);
			JSONObject wrap=new JSONObject();
			wrap.put("result", result);
			return wrap;
		} catch (JSONException e) {
			throw new ServletException(e);
		}
	}

}
