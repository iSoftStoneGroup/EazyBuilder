package com.eazybuilder.ci.repository;

import com.eazybuilder.ci.entity.devops.Release;
import org.springframework.stereotype.Repository;

@Repository
public interface ReleaseDao extends BaseDao<Release, String>{

}
