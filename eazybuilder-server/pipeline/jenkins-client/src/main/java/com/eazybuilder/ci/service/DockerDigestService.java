package com.eazybuilder.ci.service;

import com.eazybuilder.ci.base.AbstractCommonServiceImpl;
import com.eazybuilder.ci.base.CommonService;
import com.eazybuilder.ci.entity.docker.DockerDigest;
import com.eazybuilder.ci.entity.docker.QDockerDigest;
import com.eazybuilder.ci.repository.DockerDigestDao;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class DockerDigestService extends AbstractCommonServiceImpl<DockerDigestDao, DockerDigest> implements CommonService<DockerDigest> {

    public Iterable<DockerDigest> findDockerByProjectId(Collection<String> projectIds){
        return dao.findAll(QDockerDigest.dockerDigest.projectId.in(projectIds));
    }

    public Iterable<DockerDigest> findDockerByProjectIdAndNamespaceAndTag(Collection<String> projectIds,Collection<String> namespace,String imageTag){
        return dao.findAll(QDockerDigest.dockerDigest.projectId.in(projectIds).and(QDockerDigest.dockerDigest.namespace.in(namespace)).and(QDockerDigest.dockerDigest.tag.eq(imageTag)));
    }

    public Iterable<DockerDigest> findDockerByProjectIdAndNamespaceAndTag(String projectId,Collection<String> namespace,String imageTag){
        return dao.findAll(QDockerDigest.dockerDigest.projectId.eq(projectId).and(QDockerDigest.dockerDigest.namespace.in(namespace)).and(QDockerDigest.dockerDigest.tag.eq(imageTag)));
    }
}
