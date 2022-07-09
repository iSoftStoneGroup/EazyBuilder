package com.eazybuilder.ci.controller.vo;

import java.util.List;

public class GitSystemPushEvent {
	private String event_name;//": "push","tag_push"
	private String before;//": "95790bf891e76fee5e1747ab589903a6a1f80f22",
	private String after;//": "da1560886d4f094c3e6c9ef40349f7d38b5d27d7",
	private String ref;//": "refs/heads/master",
	private String user_name;//
	private String user_email;//": "john@example.com",
	private GitProject project;
	private List<GitCommit> commits;//
	public static class GitProject{
		private String name;
		private String http_url;
		private String ssh_url;
		private String path_with_namespace;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getHttp_url() {
			return http_url;
		}
		public void setHttp_url(String http_url) {
			this.http_url = http_url;
		}
		public String getSsh_url() {
			return ssh_url;
		}
		public void setSsh_url(String ssh_url) {
			this.ssh_url = ssh_url;
		}
		public String getPath_with_namespace() {
			return path_with_namespace;
		}
		public void setPath_with_namespace(String path_with_namespace) {
			this.path_with_namespace = path_with_namespace;
		}
	}
	
	public static class GitCommit{
		private String id;
		private String message;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
	}

	public String getEvent_name() {
		return event_name;
	}

	public void setEvent_name(String event_name) {
		this.event_name = event_name;
	}

	public String getBefore() {
		return before;
	}

	public void setBefore(String before) {
		this.before = before;
	}

	public String getAfter() {
		return after;
	}

	public void setAfter(String after) {
		this.after = after;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public String getUser_email() {
		return user_email;
	}

	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}

	public GitProject getProject() {
		return project;
	}

	public void setProject(GitProject project) {
		this.project = project;
	}

	public List<GitCommit> getCommits() {
		return commits;
	}

	public void setCommits(List<GitCommit> commits) {
		this.commits = commits;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

}
