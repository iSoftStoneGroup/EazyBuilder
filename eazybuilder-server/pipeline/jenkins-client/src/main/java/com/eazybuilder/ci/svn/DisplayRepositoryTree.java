package com.eazybuilder.ci.svn;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import com.google.common.collect.Lists;
import com.eazybuilder.ci.entity.ProjectType;

public class DisplayRepositoryTree {
    /*
     * args parameter is used to obtain a repository location URL, user's
     * account name & password to authenticate him to the server.
     */
    public static List<ProjectSourceInfo> list(String url,String name,String password,Queue<String> logQueue) throws SVNException {
        setupLibrary();
        SVNRepository repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(url));
        ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(name, password.toCharArray());
        repository.setAuthenticationManager(authManager);
        SVNNodeKind nodeKind = repository.checkPath("", -1);
        if (nodeKind == SVNNodeKind.NONE||nodeKind == SVNNodeKind.FILE) {
        	throw new IllegalArgumentException("无效的SVN地址，请提供SVN目录地址");
        }
        return listEntries(repository, "",logQueue);
    }

    /*
     * Initializes the library to work with a repository via 
     * different protocols.
     */
    private static void setupLibrary() {
        /*
         * For using over http:// and https://
         */
        DAVRepositoryFactory.setup();
        /*
         * For using over svn:// and svn+xxx://
         */
        SVNRepositoryFactoryImpl.setup();
        
        /*
         * For using over file:///
         */
        FSRepositoryFactory.setup();
    }

    /*
     * Called recursively to obtain all entries that make up the repository tree
     * repository - an SVNRepository which interface is used to carry out the
     * request, in this case it's a request to get all entries in the directory
     * located at the path parameter;
     * 
     * path is a directory path relative to the repository location path (that
     * is a part of the URL used to create an SVNRepository instance);
     *  
     */
    public static List<ProjectSourceInfo> listEntries(SVNRepository repository, String path, Queue<String> logQueue)
            throws SVNException {
    	logQueue.add("扫描:"+path);
    	List<ProjectSourceInfo> projects=Lists.newArrayList();
        Collection entries = repository.getDir(path, -1, null,
                (Collection) null);
        Iterator iterator = entries.iterator();
        while (iterator.hasNext()) {
            SVNDirEntry entry = (SVNDirEntry) iterator.next();
            /*
             * Checking up if the entry is a directory.
             */
            if (entry.getKind() == SVNNodeKind.DIR) {
            	String thisPath=(path.equals("")) ? entry.getName(): path + "/" + entry.getName();
            	ProjectSourceInfo psi=checkProject(repository, thisPath,logQueue);
            	if(psi!=null){
            		projects.add(psi);
            	}else{
            		//search child
            		projects.addAll(listEntries(repository, thisPath,logQueue));
            	}
            }
        }
        return projects;
    }
    
    public static ProjectSourceInfo checkProject(SVNRepository repository, String path, Queue<String> logQueue)
            throws SVNException {
        Collection entries = repository.getDir(path, -1, null,(Collection) null);
        Iterator iterator = entries.iterator();
        while (iterator.hasNext()) {
            SVNDirEntry entry = (SVNDirEntry) iterator.next();
            if(entry.getKind()==SVNNodeKind.FILE){
            	if("pom.xml".equals(entry.getName())){
            		logQueue.add("检测到Maven项目:"+path);
            		return new ProjectSourceInfo(path,true, null, null,path,ProjectType.java);
            	}
            	if("package.json".equals(entry.getName())) {
            		logQueue.add("检测到NPM项目:"+path);
            		return new ProjectSourceInfo(path,false, null, null,path,ProjectType.npm);
            	}
            }
            if(entry.getKind()==SVNNodeKind.DIR ){
            	String srcPath=path.equals("")?
    					(entry.getName()+"src/main/java"):(path+"/"+entry.getName()+"/src/main/java");
    			String libPath=path.equals("")?
    					(entry.getName()+"src/main/webapp/WEB-INF/lib"):(path+"/"+entry.getName()+"/src/main/webapp/WEB-INF/lib");
    			SVNNodeKind srcKind=repository.checkPath(srcPath, -1);
    			SVNNodeKind libKind=repository.checkPath(libPath, -1);
            	if(srcKind==SVNNodeKind.DIR
            			&&libKind==SVNNodeKind.DIR){
            		logQueue.add("检测到普通项目:"+path+"/"+entry.getName());
            		return new ProjectSourceInfo(path,false, "src/main/java", "src/main/webapp/WEB-INF/lib",path+"/"+entry.getName(),ProjectType.java);
            	}
            }
        }
        return null;
    }
}