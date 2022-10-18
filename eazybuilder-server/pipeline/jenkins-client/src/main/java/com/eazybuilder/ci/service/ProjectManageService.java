package com.eazybuilder.ci.service;

import com.eazybuilder.ci.base.AbstractCommonServiceImpl;
import com.eazybuilder.ci.base.CommonService;
import com.eazybuilder.ci.entity.ProjectManage;
import com.eazybuilder.ci.entity.devops.DevopsInit;
import com.eazybuilder.ci.repository.ProjectManageDao;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ProjectManageService extends AbstractCommonServiceImpl<ProjectManageDao, ProjectManage> implements CommonService<ProjectManage>{
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String PROJECTMANAGE = "demands:";

    @Autowired
    private StringRedisTemplate redisTemplate;


    @Resource
    DevopsInitServiceImpl devopsInitService;
    @Override
    public void save(ProjectManage entity) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
        entity.setCreateDate(df.format(new Date()));
        super.save(entity);
        logger.info("判断当前需求管理平台是否被项目组引用了-{}",entity.getName());
        List<DevopsInit>  devopsInits= devopsInitService.findByProjectManageId(entity.getId());
        if(devopsInits!=null && devopsInits.size()>0&&!devopsInits.isEmpty()) {
            devopsInits.forEach(devopsInit -> {
                logger.info("当前需求管理平台已经被项目组引用-{}",entity.getName());
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("isUpdate","true");
                jsonObject.put("dbPassword",entity.getDbPassword());
                jsonObject.put("dbUserName",entity.getDbName());
                jsonObject.put("dbUrl",entity.getDbUrl());
                jsonObject.put("managePassword",entity.getPassword());
                jsonObject.put("manageType",entity.getType());
                jsonObject.put("manageUrl",entity.getUrl());
                jsonObject.put("manageUserName",entity.getUserName());
                jsonObject.put("createDate",entity.getCreateDate());
                logger.info("开始更新缓存--{}-{}",PROJECTMANAGE+devopsInit.getTeamCode(),jsonObject);
                redisTemplate.opsForHash().put("demands", devopsInit.getTeamCode().toLowerCase(), jsonObject.toString());
                redisTemplate.opsForValue().set(PROJECTMANAGE+devopsInit.getTeamCode().toLowerCase(),jsonObject.toString());
            });
        }else {
            logger.info("当前需求管理平台没有被项目组引用-{}",entity.getName());
        }
    }
}
