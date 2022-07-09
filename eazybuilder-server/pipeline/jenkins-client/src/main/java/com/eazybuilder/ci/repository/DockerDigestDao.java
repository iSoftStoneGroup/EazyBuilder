package com.eazybuilder.ci.repository;

import com.eazybuilder.ci.entity.docker.DockerDigest;
import org.springframework.stereotype.Repository;

@Repository
public interface DockerDigestDao extends BaseDao<DockerDigest, String>{
}
