package com.eazybuilder.ci.controller;

import com.eazybuilder.ci.base.CRUDRestController;
import com.eazybuilder.ci.entity.BuildJobArrange;
import com.eazybuilder.ci.service.BuildJobArrangeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: mlxuef
 * @createTime: 2022/8/17
 * @description:
 **/
@RestController
@RequestMapping("/api/buildJobArrange")
public class BuildJobArrangeController extends CRUDRestController<BuildJobArrangeService, BuildJobArrange> {

}
