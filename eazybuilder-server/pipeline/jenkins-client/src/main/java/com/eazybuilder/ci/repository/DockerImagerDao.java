package com.eazybuilder.ci.repository;

import com.eazybuilder.ci.entity.devops.DockerImage;
import org.springframework.stereotype.Repository;

@Repository
public interface DockerImagerDao extends BaseDao<DockerImage, String>{

}
