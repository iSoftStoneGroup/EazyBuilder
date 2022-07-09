package com.eazybuilder.ci.controller;

import com.eazybuilder.ci.base.CRUDRestController;
import com.eazybuilder.ci.entity.docker.DockerDigest;
import com.eazybuilder.ci.service.DockerDigestService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dockerDigest")
public class DockerDigestController extends CRUDRestController<DockerDigestService, DockerDigest> {
}
