package com.eazybuilder.ci.service;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.stereotype.Service;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntryPath;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.BasicAuthenticationManager;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNInfo;
import org.tmatesoft.svn.core.wc.SVNLogClient;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCClient;

import com.google.common.collect.Lists;
import com.eazybuilder.ci.base.AbstractCommonServiceImpl;
import com.eazybuilder.ci.base.CommonService;
import com.eazybuilder.ci.controller.vo.ScmVersionInfo;
import com.eazybuilder.ci.entity.Scm;
import com.eazybuilder.ci.entity.ScmType;
import com.eazybuilder.ci.repository.ScmDao;
@Service
public class ScmService extends AbstractCommonServiceImpl<ScmDao, Scm> 
				implements CommonService<Scm>{
	
	public String getHeadVersion(Scm scm) throws Exception{
		if(scm.getType()==ScmType.git){
			return getGitCommitNo(scm);
		}
		return getSvnRevision(scm);
	}
	
	/**
	 * 获取两个版本号间的提交备注记录
	 * @param scm
	 * @param startRevision
	 * @param endRevision
	 * @param includeChangeList 
	 * @return
	 * @throws SVNException 
	 * @throws NumberFormatException 
	 */
	public List<ScmVersionInfo> getCommitLog(Scm scm,String startRevision,String endRevision,long limit, boolean includeChangeList) throws Exception{
		if(scm.getType()!=ScmType.svn){
			throw new IllegalArgumentException("目前只支持查看SVN修订记录");
		}
		SVNClientManager clientManager=SVNClientManager.newInstance();
		ISVNAuthenticationManager authManager = BasicAuthenticationManager.newInstance(scm.getUser(), scm.getPassword().toCharArray());
		clientManager.setAuthenticationManager(authManager);
		SVNLogClient client=clientManager.getLogClient();
		String url=scm.getUrl();
		if(url.endsWith("@HEAD")){
			url=url.substring(0, url.length()-"@HEAD".length());
		}
		SVNURL svnUrl=SVNURL.parseURIEncoded(url);
		
		List<ScmVersionInfo> changeList=Lists.newArrayList();
		client.doLog(svnUrl, new String[]{"."}, SVNRevision.HEAD,
				SVNRevision.create(Long.parseLong(startRevision)), SVNRevision.create(Long.parseLong(endRevision)), 
					true, includeChangeList, limit, (entry)->{
					List<String> changes=Lists.newArrayList();
					if (entry.getChangedPaths()!= null && !entry.getChangedPaths().isEmpty()) {
						for (Iterator<SVNLogEntryPath> paths = entry.getChangedPaths().values().iterator(); paths.hasNext();) {
							SVNLogEntryPath path = paths.next();
							changes.add(path.toString());
						}
					}
					changeList.add(new ScmVersionInfo(""+entry.getRevision(),entry.getDate(), entry.getAuthor(), entry.getMessage(),changes));
				});
		return changeList;
	}
	
	private String getGitCommitNo(Scm scm) throws Exception{
		Collection<Ref>	refs=Git
				.lsRemoteRepository()
				.setRemote(scm.getUrl())
				.setCredentialsProvider(new UsernamePasswordCredentialsProvider(scm.getUser(),scm.getPassword()))
				.setHeads(true)
				.call();
		if(refs!=null&&refs.size()>0){
			return refs.iterator().next().getObjectId().getName();
		}else{
			return null;
		}
	}
	
	private String getSvnRevision(Scm scm) throws Exception{
		SVNClientManager clientManager=SVNClientManager.newInstance();
		ISVNAuthenticationManager authManager = BasicAuthenticationManager.newInstance(scm.getUser(), scm.getPassword().toCharArray());
		clientManager.setAuthenticationManager(authManager);
		SVNWCClient client=clientManager.getWCClient();
		SVNURL svnUrl=SVNURL.parseURIEncoded(scm.getUrl());
		SVNInfo info = client.doInfo(svnUrl, SVNRevision.HEAD, SVNRevision.HEAD);
		long revision = info.getRevision().getNumber();
		return ""+revision;
	}
}
