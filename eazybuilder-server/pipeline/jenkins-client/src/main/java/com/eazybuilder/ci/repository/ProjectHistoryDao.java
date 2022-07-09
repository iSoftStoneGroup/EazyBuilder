package com.eazybuilder.ci.repository;

import com.eazybuilder.ci.entity.Project;
import com.eazybuilder.ci.entity.ProjectHistory;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectHistoryDao extends BaseDao<ProjectHistory, String>{

}
