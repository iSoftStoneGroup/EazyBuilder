package com.eazybuilder.ci.scm;

import java.io.File;

public interface ResourceDownloader {

	/**
	 * CHECKOUT RESOURCE FROM SOURCE CODE MANAGER
	 * 
	 * @param url
	 * @param user
	 * @param password
	 * @param toPath
	 * @throws Exception 
	 */
	public void checkout(String url,String user,String password,File toPath) throws Exception;
}
