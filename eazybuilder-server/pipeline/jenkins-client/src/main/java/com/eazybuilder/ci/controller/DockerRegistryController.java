package com.eazybuilder.ci.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eazybuilder.ci.base.CRUDRestController;
import com.eazybuilder.ci.entity.DockerRegistry;
import com.eazybuilder.ci.service.DockerRegistryService;

@RestController
@RequestMapping("/api/dockerRegistry")
public class DockerRegistryController extends CRUDRestController<DockerRegistryService, DockerRegistry>{

}
