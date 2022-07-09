package com.eazybuilder.ci.repository;

import com.eazybuilder.ci.entity.ProfileHistory;
import com.eazybuilder.ci.entity.ProjectHistory;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileHistoryDao extends BaseDao<ProfileHistory, String>{

}
