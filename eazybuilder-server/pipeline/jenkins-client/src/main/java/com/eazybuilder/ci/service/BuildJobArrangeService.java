package com.eazybuilder.ci.service;

import com.eazybuilder.ci.base.AbstractCommonServiceImpl;
import com.eazybuilder.ci.base.CommonService;
import com.eazybuilder.ci.base.PageSearch;
import com.eazybuilder.ci.entity.BuildJobArrange;
import com.eazybuilder.ci.entity.devops.QRelease;
import com.eazybuilder.ci.repository.BuildJobArrangeDao;
import org.springframework.stereotype.Service;

@Service
@PageSearch(value = QRelease.class, fields = {"name"})
public class BuildJobArrangeService extends AbstractCommonServiceImpl<BuildJobArrangeDao, BuildJobArrange>
				implements CommonService<BuildJobArrange>{

}
