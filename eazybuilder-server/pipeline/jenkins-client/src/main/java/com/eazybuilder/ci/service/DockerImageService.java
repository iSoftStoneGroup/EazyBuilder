package com.eazybuilder.ci.service;

import com.eazybuilder.ci.base.AbstractCommonServiceImpl;
import com.eazybuilder.ci.base.CommonService;
import com.eazybuilder.ci.base.PageSearch;
import com.eazybuilder.ci.entity.devops.DockerImage;
import com.eazybuilder.ci.entity.devops.QDockerImage;
import com.eazybuilder.ci.repository.DockerImagerDao;
import org.springframework.stereotype.Service;

@PageSearch(value = QDockerImage.class, fields = {"imageTag"})
@Service
public class DockerImageService extends AbstractCommonServiceImpl<DockerImagerDao, DockerImage> implements CommonService<DockerImage>{

    public Iterable<DockerImage> findByProjectId(String projectId){
        return dao.findAll(QDockerImage.dockerImage.projectId.eq(projectId));
    }
	
}
