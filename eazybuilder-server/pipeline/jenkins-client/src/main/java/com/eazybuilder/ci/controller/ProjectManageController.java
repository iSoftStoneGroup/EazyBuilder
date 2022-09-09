package com.eazybuilder.ci.controller;

import com.eazybuilder.ci.base.CRUDRestController;
import com.eazybuilder.ci.entity.ProjectManage;
import com.eazybuilder.ci.service.ProjectManageService;
import com.eazybuilder.ci.util.JDBCUtil;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;

@RestController
@RequestMapping("/api/projectManage")
public class ProjectManageController extends CRUDRestController<ProjectManageService, ProjectManage>{

    @PostMapping(value = "/testConnection")
    @ApiOperation("测试连接数据库")
    public Boolean testConnection(@RequestBody ProjectManage entity){
        Connection conn = null;
        String url = entity.getDbUrl();
        String userName = entity.getDbName();
        String password = entity.getDbPassword();
        try{
            conn = JDBCUtil.getConnection(url, userName, password);
            if(conn != null){
                 return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
