package com.eazybuilder.ci.service;

import com.eazybuilder.ci.repository.CIPackageDao;
import org.springframework.stereotype.Service;
import com.eazybuilder.ci.base.AbstractCommonServiceImpl;
import com.eazybuilder.ci.base.CommonService;

import com.eazybuilder.ci.entity.pipeline.CIPackage;

@Service
public class CIPackageService extends AbstractCommonServiceImpl<CIPackageDao, CIPackage> implements CommonService<CIPackage>{
}
