package com.eazybuilder.ci.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.eazybuilder.ci.entity.Project;
@Repository
public interface ProjectDao extends BaseDao<Project, String>{

	@Modifying
	@Query(value = "delete from ci_job_projects where projects_id=?1",nativeQuery = true)
	public void deleteJobProject(String projectId);
	
	
	@Modifying
	@Query(value = "delete from ci_project_group_projects where projects_id=?1",nativeQuery = true)
	public void deleteGroupProject(String projectId);

	@Modifying
	@Query(value = "delete from ci_release_project_list where project_list_id=?1",nativeQuery = true)
	public void deleteReleaseProject(String projectId);

	@Modifying
	@Query(value = "delete from ci_release_pipeline_list where pipeline_list_id=?1",nativeQuery = true)
	public void deleteReleasePipeline(String PipelineId);
}
