package com.eazybuilder.ci.controller;

import com.eazybuilder.ci.OperLog;
import com.eazybuilder.ci.base.CRUDRestController;
import com.eazybuilder.ci.base.PageResult;
import com.eazybuilder.ci.dto.DevopsInitDto;
import com.eazybuilder.ci.entity.Project;
import com.eazybuilder.ci.entity.Team;
import com.eazybuilder.ci.entity.Upms.UpmsUserVo;
import com.eazybuilder.ci.entity.User;
import com.eazybuilder.ci.entity.devops.*;
import com.eazybuilder.ci.local.QueryLocalData;
import com.eazybuilder.ci.rabbitMq.SendRabbitMq;
import com.eazybuilder.ci.service.*;
import com.eazybuilder.ci.upms.QueryUpmsData;
import com.eazybuilder.ci.util.DtoEntityUtil;
import com.wordnik.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/deveops")
public class DevopsInitController extends CRUDRestController<DevopsInitServiceImpl, DevopsInit> {

    private static Logger logger = LoggerFactory.getLogger(DevopsInitController.class);


    @Resource
    DevopsInitServiceImpl devopsInitServiceImpl;
    @Resource
    DevopsProjectServiceImpl devopsProjectService;
    @Resource
    ProjectManageService projectManageService;

    @Resource
    SendRabbitMq sendRabbitMq;
    @Resource
    QueryUpmsData queryUpmsData;
    @Resource
    QueryLocalData queryLocalData;
    @Resource
    ProjectService projectService;


    @Value("${message.broadcastExchange}")
    public  String broadcastExchange;

    @Value("${portal.used:false}")
    private Boolean used;


    @RequestMapping(value = "/init",method={RequestMethod.POST,RequestMethod.PUT})
    public void init(@RequestBody DevopsInitDto deveopsInitDto) throws Exception {
        logger.info("初始化项目组开始{}：",JSONObject.fromObject(deveopsInitDto));

        if(used) {
            //1.同步数据到upms中
            //1.1判断当前项目组是否已经在upms中存在
            try {
                if (null == deveopsInitDto.getGroupId()) {
                    Long groupId = queryUpmsData.createGroup(deveopsInitDto.getTeamName());
                    if (null != groupId) {
                        deveopsInitDto.setGroupId(groupId);
                    }
                }
                //1.2绑定用户到群组下
                logger.info("初始化绑定用户到群组下");
                List<String> userId = deveopsInitDto.getDevopsUsers().stream().map(UpmsUserVo::getUserId).collect(Collectors.toList());
                queryUpmsData.bindUserToGroup(deveopsInitDto.getGroupId(), userId);
            } catch (Exception e) {
                logger.error("ci初始化时和upms接口交互出现异常:{}", e.getMessage(), e);
            }
        }
        else{
            //判断当前项目组是否在本地Team中存在
            if (null == deveopsInitDto.getGroupId()) {
                Long groupId = queryLocalData.createGroup(deveopsInitDto.getTeamName());
                if (null != groupId) {
                    deveopsInitDto.setGroupId(groupId);
                }
            }
        }

        //2.将数据保存到数据库中
        //将deveopsDto转换成 DeveopsInit
        DevopsInit devopsInit = DtoEntityUtil.trans(deveopsInitDto, DevopsInit.class);
        devopsInitServiceImpl.save(devopsInit);

        devopsProjectService.save(devopsInit.getDevopsProjects());
        //3.发送初始化数据到deveops 各个平台
        //3.1将upms的user字段改为deveops约定的字段
        deveopsInitDto.setId(devopsInit.getId());
        deveopsInitDto.setDevopsProjects(devopsInit.getDevopsProjects());
        deveopsInitDto.setProjectManage(projectManageService.findOne(devopsInit.getProjectManageId()));
        JSONObject sendInitData = devopsInitServiceImpl.getSendInitData(deveopsInitDto);
        //3.2发送数据到mq中
        sendRabbitMq.sendMsg(sendInitData.toString(),broadcastExchange,"");
        //3.3
        projectManageService.save(projectManageService.findOne(devopsInit.getProjectManageId()));
        //4.ci进行初始化
        //把用户保存起来
        List<User> ciUser = devopsInitServiceImpl.getCiUser(deveopsInitDto);
        Team team = devopsInitServiceImpl.getCiTeam(devopsInit,ciUser);
        List<Project> projects = devopsInitServiceImpl.getCiProject(devopsInit,team);
        if(!projects.isEmpty()&&projects.size()>0) {
            projectService.saveWithoutMsg(projects);
        }
        //5.将红灯修复时间间隔存到redis中。供度量平台使用
        service.saveRedisFocusRedlightRepairConfig(devopsInit);
    }




    @RequestMapping(method={RequestMethod.POST,RequestMethod.PUT})
    @ApiOperation("保存")
    @OperLog(module = "persist",opType = "save",opDesc = "保存")
    @Override
    public DevopsInit save(@RequestBody DevopsInit entity){
        devopsInitServiceImpl.save(entity);
        return entity;
    }

    @RequestMapping(value="/getDeveopsPage",method=RequestMethod.GET)
    @ApiOperation("按页查询")
    public PageResult<DevopsInit> getDeveopsPage(
            @RequestParam(value="limit",defaultValue="10")int limit,
            @RequestParam(value="offset")int offset,
            @RequestParam(value="search",required=false)String searchText,HttpServletRequest request){

        Pageable pageable=PageRequest.of(Math.floorDiv(offset, limit), limit, Sort.Direction.DESC,"id");
        Page<DevopsInit> page = devopsInitServiceImpl.pageSearch(pageable,searchText);
        PageResult<DevopsInit> result=PageResult.create(page.getTotalElements(), page.getContent());
        return result;
    }
    @RequestMapping(value = "/delete",method=RequestMethod.POST)
    @ApiOperation("删除")
    public void delete(@RequestBody DevopsInitIds devopsInitIds) {
        DevopsInit devopsInit = service.findOne(devopsInitIds.getId());
        for(String id : devopsInitIds.getIds()){
            for(int i = 0; i< devopsInit.getDevopsProjects().size(); i++){
                DevopsProject devopsProject = devopsInit.getDevopsProjects().get(i);
                if(devopsProject.getId().equals(id)){
                    devopsInit.getDevopsProjects().remove(devopsProject);
                    //不减减不行，list元素在减少
                    i--;
                }
            }
        }
        service.save(devopsInit);
    }
}
