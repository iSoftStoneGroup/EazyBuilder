package com.eazybuilder.ci.util;

import java.util.UUID;

import cn.hutool.crypto.SecureUtil;

public class ApiTokenUtil {
	static final byte[] key=ApiTokenUtil.class.getName().getBytes();
	/**
	 * 生成随机密钥
	 * @return
	 */
	public static String generateRandomSecret() {
		return UUID.randomUUID().toString();
	}
	
	public static class ApiToken{
		String resource;
		String secret;
		long expired;
		
		public String getResource() {
			return resource;
		}
		public void setResource(String resource) {
			this.resource = resource;
		}
		public String getSecret() {
			return secret;
		}
		public void setSecret(String secret) {
			this.secret = secret;
		}
		public long getExpired() {
			return expired;
		}
		public void setExpired(long expired) {
			this.expired = expired;
		}
	}
	/**
	 * 生成API Token
	 * @param secret
	 * @param source
	 * @return
	 * @throws Exception
	 */
	public static String generateToken(ApiToken tokenSource) throws Exception {
		return SecureUtil.aes(key).encryptBase64(tokenSource.getResource()
				+"\n"+tokenSource.getSecret()+"\n"+tokenSource.getExpired());
	}
	
	/**
	 * 解析Token
	 * @param token
	 * @return
	 */
	public static ApiToken parseToken(String token) {
		String[] data=SecureUtil.aes(key).decryptStr(token).split("\n");
		ApiToken apiToken=new ApiToken();
		apiToken.setResource(data[0]);
		apiToken.setSecret(data[1]);
		apiToken.setExpired(Long.parseLong(data[2]));
		return apiToken;
	}
}
