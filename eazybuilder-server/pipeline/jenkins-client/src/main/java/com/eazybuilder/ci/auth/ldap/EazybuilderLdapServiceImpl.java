package com.eazybuilder.ci.auth.ldap;

import javax.annotation.PostConstruct;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.support.LdapUtils;

public class EazybuilderLdapServiceImpl implements LdapService {

    @Autowired
    private LdapTemplate ldapTemplate;

    @Value("${ldap.domainName}")
    private String ldapDomainName;

    @Value("${ldap.base}")
    private String ldapBaseDn;
    
    @PostConstruct
    public void init(){
    	ldapTemplate.setIgnorePartialResultException(true);
    }    
    
	public LdapUser getUserInfo(String mail){
		System.out.println("try get "+mail+" detail from ldap");
		return ldapTemplate.searchForObject("", "(&(objectClass=person)(sAMAccountName="+mail.substring(0, mail.indexOf("@"))+"))",
				new ContextMapper<LdapUser>() {

					@Override
					public LdapUser mapFromContext(Object ctx) throws NamingException {
						DirContextAdapter dirContext=(DirContextAdapter) ctx;
						Attributes attributes=dirContext.getAttributes();
						LdapUser user = new LdapUser();
		            	user.setCn((String) attributes.get("cn").get());
		            	user.setDepartment((String) attributes.get("department").get());
		            	if(attributes.get("mobile")!=null) {
                            user.setMobile((String) attributes.get("mobile").get());
                        }
		            	user.setTitle((String) attributes.get("title").get());
		            	System.out.println(ToStringBuilder.reflectionToString(user,ToStringStyle.MULTI_LINE_STYLE));
		                return user;
					}
			
		});
    }
    
    /*
     * 身份认证
     */
	@Override
	public boolean authenticate(String userName, String password) {
        //String userDomainName = getDnForUser(userName);
		System.out.println("user:"+userName+",try to login");
        String userDomainName = String.format(ldapDomainName, userName);
        DirContext ctx = null;
        try {
            ctx = ldapTemplate.getContextSource().getContext(userDomainName,password);
            
            return true;
        } catch(Exception e) {
        	System.err.println(e.getMessage());
        } finally {
            LdapUtils.closeContext(ctx);
        }
        //验证未通过，返回null
        return false;
    }
}
