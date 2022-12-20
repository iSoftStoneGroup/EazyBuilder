package com.eazybuilder.ga.pojo.gitlab;

import java.util.List;

public class GitEvent {
	/**
	 * 事件类型:
	 */
	private String object_kind;
	/**
	 * ref
	 */
	private String ref;
	/**
	 * 触发的用户
	 */
	private String user_name;
	
	private List<GitCommit> commits;
	
	public String getObject_kind() {
		return object_kind;
	}
	public void setObject_kind(String object_kind) {
		this.object_kind = object_kind;
	}
	public String getRef() {
		return ref;
	}
	public void setRef(String ref) {
		this.ref = ref;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	
	public List<GitCommit> getCommits() {
		return commits;
	}
	public void setCommits(List<GitCommit> commits) {
		this.commits = commits;
	}


	public static class GitCommit{
		private String id;
		private String message;

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}
	}
}
