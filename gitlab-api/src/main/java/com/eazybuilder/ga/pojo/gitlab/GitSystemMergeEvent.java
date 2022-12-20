package com.eazybuilder.ga.pojo.gitlab;

public class GitSystemMergeEvent {

	private String object_kind = "";
	private User user;
	public static class User {
		private String id = "";
		private String name = "";
		private String username = "";
		private String avatar_url = "";
		private String email = "";

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getAvatar_url() {
			return avatar_url;
		}

		public void setAvatar_url(String avatar_url) {
			this.avatar_url = avatar_url;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

	}

	public String getObject_kind() {
		return object_kind;
	}

	public void setObject_kind(String object_kind) {
		this.object_kind = object_kind;
	}

	public String getMerge_when_pipeline_succeeds() {
		return merge_when_pipeline_succeeds;
	}

	public void setMerge_when_pipeline_succeeds(String merge_when_pipeline_succeeds) {
		this.merge_when_pipeline_succeeds = merge_when_pipeline_succeeds;
	}

	public String getMerge_user_id() {
		return merge_user_id;
	}

	public void setMerge_user_id(String merge_user_id) {
		this.merge_user_id = merge_user_id;
	}

	public String getMerge_commit_sha() {
		return merge_commit_sha;
	}

	public void setMerge_commit_sha(String merge_commit_sha) {
		this.merge_commit_sha = merge_commit_sha;
	}

	public String getDeleted_at() {
		return deleted_at;
	}

	public void setDeleted_at(String deleted_at) {
		this.deleted_at = deleted_at;
	}

	public String getIn_progress_merge_commit_sha() {
		return in_progress_merge_commit_sha;
	}

	public void setIn_progress_merge_commit_sha(String in_progress_merge_commit_sha) {
		this.in_progress_merge_commit_sha = in_progress_merge_commit_sha;
	}

	public String getLock_version() {
		return lock_version;
	}

	public void setLock_version(String lock_version) {
		this.lock_version = lock_version;
	}

	public String getTime_estimate() {
		return time_estimate;
	}

	public void setTime_estimate(String time_estimate) {
		this.time_estimate = time_estimate;
	}

	public String getLast_edited_at() {
		return last_edited_at;
	}

	public void setLast_edited_at(String last_edited_at) {
		this.last_edited_at = last_edited_at;
	}

	public String getLast_edited_by_id() {
		return last_edited_by_id;
	}

	public void setLast_edited_by_id(String last_edited_by_id) {
		this.last_edited_by_id = last_edited_by_id;
	}

	public String getHead_pipeline_id() {
		return head_pipeline_id;
	}

	public void setHead_pipeline_id(String head_pipeline_id) {
		this.head_pipeline_id = head_pipeline_id;
	}

	public String getRef_fetched() {
		return ref_fetched;
	}

	public void setRef_fetched(String ref_fetched) {
		this.ref_fetched = ref_fetched;
	}

	public String getMerge_jid() {
		return merge_jid;
	}

	public void setMerge_jid(String merge_jid) {
		this.merge_jid = merge_jid;
	}

	public String getLabels() {
		return labels;
	}

	public void setLabels(String labels) {
		this.labels = labels;
	}
	private  Project project;
	public static class Project {
		private String name = "";
		private String description = "";
		private String web_url = "";
		private String avatar_url = "";
		private String git_ssh_url = "";
		private String git_http_url = "";
		private String namespace = "";
		private String visibility_level = "";
		private String path_with_namespace = "";
		private String default_branch = "";
		private String ci_config_path = "";
		private String homepage = "";
		private String url = "";
		private String ssh_url = "";
		private String http_url = "";

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getWeb_url() {
			return web_url;
		}

		public void setWeb_url(String web_url) {
			this.web_url = web_url;
		}

		public String getAvatar_url() {
			return avatar_url;
		}

		public void setAvatar_url(String avatar_url) {
			this.avatar_url = avatar_url;
		}

		public String getGit_ssh_url() {
			return git_ssh_url;
		}

		public void setGit_ssh_url(String git_ssh_url) {
			this.git_ssh_url = git_ssh_url;
		}

		public String getGit_http_url() {
			return git_http_url;
		}

		public void setGit_http_url(String git_http_url) {
			this.git_http_url = git_http_url;
		}

		public String getNamespace() {
			return namespace;
		}

		public void setNamespace(String namespace) {
			this.namespace = namespace;
		}

		public String getVisibility_level() {
			return visibility_level;
		}

		public void setVisibility_level(String visibility_level) {
			this.visibility_level = visibility_level;
		}

		public String getPath_with_namespace() {
			return path_with_namespace;
		}

		public void setPath_with_namespace(String path_with_namespace) {
			this.path_with_namespace = path_with_namespace;
		}

		public String getDefault_branch() {
			return default_branch;
		}

		public void setDefault_branch(String default_branch) {
			this.default_branch = default_branch;
		}

		public String getCi_config_path() {
			return ci_config_path;
		}

		public void setCi_config_path(String ci_config_path) {
			this.ci_config_path = ci_config_path;
		}

		public String getHomepage() {
			return homepage;
		}

		public void setHomepage(String homepage) {
			this.homepage = homepage;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getSsh_url() {
			return ssh_url;
		}

		public void setSsh_url(String ssh_url) {
			this.ssh_url = ssh_url;
		}

		public String getHttp_url() {
			return http_url;
		}

		public void setHttp_url(String http_url) {
			this.http_url = http_url;
		}

	}
	private  Object_attributes object_attributes;
	public Merge_params getMerge_params() {
		return merge_params;
	}

	public void setMerge_params(Merge_params merge_params) {
		this.merge_params = merge_params;
	}

	public Source getSource() {
		return source;
	}

	public void setSource(Source source) {
		this.source = source;
	}

	public Target getTarget() {
		return target;
	}

	public void setTarget(Target target) {
		this.target = target;
	}

	public Last_commit getLast_commit() {
		return last_commit;
	}

	public void setLast_commit(Last_commit last_commit) {
		this.last_commit = last_commit;
	}

	public Repository getRepository() {
		return repository;
	}

	public void setRepository(Repository repository) {
		this.repository = repository;
	}

	public void setObject_attributes(Object_attributes object_attributes) {
		this.object_attributes = object_attributes;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}


	public static class Object_attributes {
		private String id = "";
		private String target_branch = "";
		private String source_branch = "";
		private String source_project_id = "";
		private String author_id = "";
		private String assignee_id = "";
		private String title = "";
		private String created_at = "";
		private String updated_at = "";
		private String milestone_id = "";
		private String state = "";
		private String merge_status = "";
		private String target_project_id = "";
		private String iid = "";
		private String description = "";
		private String updated_by_id = "";
		private String merge_error = "";

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getTarget_branch() {
			return target_branch;
		}

		public void setTarget_branch(String target_branch) {
			this.target_branch = target_branch;
		}

		public String getSource_branch() {
			return source_branch;
		}

		public void setSource_branch(String source_branch) {
			this.source_branch = source_branch;
		}

		public String getSource_project_id() {
			return source_project_id;
		}

		public void setSource_project_id(String source_project_id) {
			this.source_project_id = source_project_id;
		}

		public String getAuthor_id() {
			return author_id;
		}

		public void setAuthor_id(String author_id) {
			this.author_id = author_id;
		}

		public String getAssignee_id() {
			return assignee_id;
		}

		public void setAssignee_id(String assignee_id) {
			this.assignee_id = assignee_id;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getCreated_at() {
			return created_at;
		}

		public void setCreated_at(String created_at) {
			this.created_at = created_at;
		}

		public String getUpdated_at() {
			return updated_at;
		}

		public void setUpdated_at(String updated_at) {
			this.updated_at = updated_at;
		}

		public String getMilestone_id() {
			return milestone_id;
		}

		public void setMilestone_id(String milestone_id) {
			this.milestone_id = milestone_id;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		public String getMerge_status() {
			return merge_status;
		}

		public void setMerge_status(String merge_status) {
			this.merge_status = merge_status;
		}

		public String getTarget_project_id() {
			return target_project_id;
		}

		public void setTarget_project_id(String target_project_id) {
			this.target_project_id = target_project_id;
		}

		public String getIid() {
			return iid;
		}

		public void setIid(String iid) {
			this.iid = iid;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getUpdated_by_id() {
			return updated_by_id;
		}

		public void setUpdated_by_id(String updated_by_id) {
			this.updated_by_id = updated_by_id;
		}

		public String getMerge_error() {
			return merge_error;
		}

		public void setMerge_error(String merge_error) {
			this.merge_error = merge_error;
		}
	}
	private  Merge_params merge_params;
	public static class Merge_params {
		private String force_remove_source_branch = "";

		public String getForce_remove_source_branch() {
			return force_remove_source_branch;
		}

		public void setForce_remove_source_branch(String force_remove_source_branch) {
			this.force_remove_source_branch = force_remove_source_branch;
		}

	}

	private String merge_when_pipeline_succeeds = "";
	private String merge_user_id = "";
	private String merge_commit_sha = "";
	private String deleted_at = "";
	private String in_progress_merge_commit_sha = "";
	private String lock_version = "";
	private String time_estimate = "";
	private String last_edited_at = "";
	private String last_edited_by_id = "";
	private String head_pipeline_id = "";
	private String ref_fetched = "";
	private String merge_jid = "";
	private  Source source;
	public static class Source {
		private String name = "";
		private String description = "";
		private String web_url = "";
		private String avatar_url = "";
		private String git_ssh_url = "";
		private String git_http_url = "";
		private String namespace = "";
		private String visibility_level = "";
		private String path_with_namespace = "";
		private String default_branch = "";
		private String ci_config_path = "";
		private String homepage = "";
		private String url = "";
		private String ssh_url = "";
		private String http_url = "";

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getWeb_url() {
			return web_url;
		}

		public void setWeb_url(String web_url) {
			this.web_url = web_url;
		}

		public String getAvatar_url() {
			return avatar_url;
		}

		public void setAvatar_url(String avatar_url) {
			this.avatar_url = avatar_url;
		}

		public String getGit_ssh_url() {
			return git_ssh_url;
		}

		public void setGit_ssh_url(String git_ssh_url) {
			this.git_ssh_url = git_ssh_url;
		}

		public String getGit_http_url() {
			return git_http_url;
		}

		public void setGit_http_url(String git_http_url) {
			this.git_http_url = git_http_url;
		}

		public String getNamespace() {
			return namespace;
		}

		public void setNamespace(String namespace) {
			this.namespace = namespace;
		}

		public String getVisibility_level() {
			return visibility_level;
		}

		public void setVisibility_level(String visibility_level) {
			this.visibility_level = visibility_level;
		}

		public String getPath_with_namespace() {
			return path_with_namespace;
		}

		public void setPath_with_namespace(String path_with_namespace) {
			this.path_with_namespace = path_with_namespace;
		}

		public String getDefault_branch() {
			return default_branch;
		}

		public void setDefault_branch(String default_branch) {
			this.default_branch = default_branch;
		}

		public String getCi_config_path() {
			return ci_config_path;
		}

		public void setCi_config_path(String ci_config_path) {
			this.ci_config_path = ci_config_path;
		}

		public String getHomepage() {
			return homepage;
		}

		public void setHomepage(String homepage) {
			this.homepage = homepage;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getSsh_url() {
			return ssh_url;
		}

		public void setSsh_url(String ssh_url) {
			this.ssh_url = ssh_url;
		}

		public String getHttp_url() {
			return http_url;
		}

		public void setHttp_url(String http_url) {
			this.http_url = http_url;
		}
	}
	private  Target target;
	public static class Target {
		private String name = "";
		private String description = "";
		private String web_url = "";
		private String avatar_url = "";
		private String git_ssh_url = "";
		private String git_http_url = "";
		private String namespace = "";
		private String visibility_level = "";
		private String path_with_namespace = "";
		private String default_branch = "";
		private String ci_config_path = "";
		private String homepage = "";
		private String url = "";
		private String ssh_url = "";
		private String http_url = "";

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getWeb_url() {
			return web_url;
		}

		public void setWeb_url(String web_url) {
			this.web_url = web_url;
		}

		public String getAvatar_url() {
			return avatar_url;
		}

		public void setAvatar_url(String avatar_url) {
			this.avatar_url = avatar_url;
		}

		public String getGit_ssh_url() {
			return git_ssh_url;
		}

		public void setGit_ssh_url(String git_ssh_url) {
			this.git_ssh_url = git_ssh_url;
		}

		public String getGit_http_url() {
			return git_http_url;
		}

		public void setGit_http_url(String git_http_url) {
			this.git_http_url = git_http_url;
		}

		public String getNamespace() {
			return namespace;
		}

		public void setNamespace(String namespace) {
			this.namespace = namespace;
		}

		public String getVisibility_level() {
			return visibility_level;
		}

		public void setVisibility_level(String visibility_level) {
			this.visibility_level = visibility_level;
		}

		public String getPath_with_namespace() {
			return path_with_namespace;
		}

		public void setPath_with_namespace(String path_with_namespace) {
			this.path_with_namespace = path_with_namespace;
		}

		public String getDefault_branch() {
			return default_branch;
		}

		public void setDefault_branch(String default_branch) {
			this.default_branch = default_branch;
		}

		public String getCi_config_path() {
			return ci_config_path;
		}

		public void setCi_config_path(String ci_config_path) {
			this.ci_config_path = ci_config_path;
		}

		public String getHomepage() {
			return homepage;
		}

		public void setHomepage(String homepage) {
			this.homepage = homepage;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getSsh_url() {
			return ssh_url;
		}

		public void setSsh_url(String ssh_url) {
			this.ssh_url = ssh_url;
		}

		public String getHttp_url() {
			return http_url;
		}

		public void setHttp_url(String http_url) {
			this.http_url = http_url;
		}
	}
	private  Last_commit last_commit;
	public static class Last_commit {
		private String id = "";
		private String message = "";
		private String timestamp = "";
		private String url = "";
		private String work_in_progress = "";
		private String total_time_spent = "";
		private String human_total_time_spent = "";
		private String human_time_estimate = "";

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

		public String getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(String timestamp) {
			this.timestamp = timestamp;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getWork_in_progress() {
			return work_in_progress;
		}

		public void setWork_in_progress(String work_in_progress) {
			this.work_in_progress = work_in_progress;
		}

		public String getTotal_time_spent() {
			return total_time_spent;
		}

		public void setTotal_time_spent(String total_time_spent) {
			this.total_time_spent = total_time_spent;
		}

		public String getHuman_total_time_spent() {
			return human_total_time_spent;
		}

		public void setHuman_total_time_spent(String human_total_time_spent) {
			this.human_total_time_spent = human_total_time_spent;
		}

		public String getHuman_time_estimate() {
			return human_time_estimate;
		}

		public void setHuman_time_estimate(String human_time_estimate) {
			this.human_time_estimate = human_time_estimate;
		}
		private  Author author;
		public Author getAuthor() {
			return author;
		}

		public void setAuthor(Author author) {
			this.author = author;
		}
		public static class Author {
			private String name = "";
			private String email = "";

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}

			public String getEmail() {
				return email;
			}

			public void setEmail(String email) {
				this.email = email;
			}
		}

	}

	private String labels = "";
	private  Repository repository;
	public static class Repository {
		private String name = "";
		private String url = "";
		private String description = "";
		private String homepage = "";

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getHomepage() {
			return homepage;
		}

		public void setHomepage(String homepage) {
			this.homepage = homepage;
		}

	}

}
