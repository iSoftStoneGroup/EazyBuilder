package com.eazybuilder.ci.controller;

import com.eazybuilder.ci.base.CRUDRestController;
import com.eazybuilder.ci.entity.devops.DockerImage;
import com.eazybuilder.ci.service.DockerImageService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/dockerImage")
public class DockerImageController extends CRUDRestController<DockerImageService, DockerImage> {

    @RequestMapping(method=RequestMethod.GET,value="/getDockerImageByProjectId")
    public Iterable<DockerImage> getDockerImageByProjectId(@RequestParam("projectId")String projectId){
        return service.findByProjectId(projectId);
    }
}
