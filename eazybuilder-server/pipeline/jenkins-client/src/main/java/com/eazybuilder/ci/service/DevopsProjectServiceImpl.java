package com.eazybuilder.ci.service;

import com.eazybuilder.ci.base.AbstractCommonServiceImpl;
import com.eazybuilder.ci.base.CommonService;
import com.eazybuilder.ci.entity.devops.*;
import com.eazybuilder.ci.repository.DevopsProjectDao;
import org.springframework.stereotype.Service;


@Service
public class DevopsProjectServiceImpl extends AbstractCommonServiceImpl<DevopsProjectDao, DevopsProject> implements CommonService<DevopsProject>{

}
