package com.eazybuilder.ci.auth.ldap;

public interface LdapService {
	/**
	 * 身份认证
	 */
	/* (non-Javadoc)
	 * @see com.eazybuilder.ldap.LdapService#authenticate(java.lang.String, java.lang.String)
	 */
	boolean authenticate(String userName, String password);
	
	LdapUser getUserInfo(String mail);

}