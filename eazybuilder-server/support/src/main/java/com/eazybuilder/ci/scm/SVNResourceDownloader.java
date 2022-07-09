package com.eazybuilder.ci.scm;

import java.io.File;

import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.BasicAuthenticationManager;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;

public class SVNResourceDownloader implements ResourceDownloader{
	
	@Override
	public void checkout(String url, String user, String password, File toPath) throws Exception {
		SVNClientManager clientManager=SVNClientManager.newInstance();
		ISVNAuthenticationManager authManager = BasicAuthenticationManager.newInstance(user, password.toCharArray());
		clientManager.setAuthenticationManager(authManager);
		SVNUpdateClient client=clientManager.getUpdateClient();
		SVNURL svnUrl=SVNURL.parseURIEncoded(url);
		String path=svnUrl.getPath();
		String fileName=path.substring(path.lastIndexOf("/")+1);
		if(fileName.endsWith(".zip")||fileName.endsWith(".rar")||fileName.endsWith(".gzip")||fileName.endsWith(".scan")){
			File downloadFile=new File(toPath,fileName);
			client.doExport(svnUrl, downloadFile, SVNRevision.HEAD,SVNRevision.HEAD,null,true,SVNDepth.INFINITY);
		}else{
			//checkout directory
			client.doExport(svnUrl, toPath, SVNRevision.HEAD,SVNRevision.HEAD,null,true,SVNDepth.INFINITY);
		}
	}

}
