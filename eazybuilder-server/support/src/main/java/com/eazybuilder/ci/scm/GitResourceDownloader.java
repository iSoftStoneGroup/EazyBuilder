package com.eazybuilder.ci.scm;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import com.eazybuilder.ci.util.HttpUtil;
/**
 * GIT resource download
 * 
 * url应该为资源文件压缩包的raw下载链接(http)
 * 或者整个repo的clone url
 *
 */
public class GitResourceDownloader implements ResourceDownloader{

	@Override
	public void checkout(String url, String user, String password, File toPath) throws Exception {
		SslUtils.ignoreSsl();
		if(toPath.exists()){
			FileUtils.deleteDirectory(toPath);
		}
		toPath.mkdirs();
		String fileName=url.substring(url.lastIndexOf("/")+1);
		if(fileName.endsWith(".zip")||fileName.endsWith(".rar")||fileName.endsWith(".gzip")){
			File downloadFile=new File(toPath,fileName);
			HttpUtil.downloadFileStream(url, downloadFile);
		}else{
			CloneCommand cc = Git.cloneRepository().setCloneSubmodules(true).setURI(url);
			if(user!=null&&password!=null){
				cc.setCredentialsProvider(new UsernamePasswordCredentialsProvider(user, password));
			}
			cc.setDirectory(toPath).call();
		}
	}

}
