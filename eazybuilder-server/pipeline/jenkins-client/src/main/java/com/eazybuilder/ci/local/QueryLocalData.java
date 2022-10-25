package com.eazybuilder.ci.local;

import com.alibaba.fastjson.JSON;
import com.eazybuilder.ci.entity.Team;
import com.eazybuilder.ci.entity.Upms.UpmsUserVo;
import com.eazybuilder.ci.entity.User;
import com.eazybuilder.ci.service.TeamServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class QueryLocalData {

    private static Logger logger = LoggerFactory.getLogger(QueryLocalData.class);
    @Autowired
    TeamServiceImpl teamService;

    public Team getGroupByName(String groupName) {
        return teamService.findByName(groupName);
    }
    public Team getGroupByGroupId(String groupId) {
        return teamService.findByGroupId(groupId);
    }
    /**
     * 创建项目组Id
     *
     * @param groupName
     * @return
     * @throws Exception
     */
    public long createGroup(String groupName) throws Exception {
        logger.info("查询项目组在Team是否存在：{}", this.getGroupByName(groupName));
        if(null != this.getGroupByName(groupName)){
            logger.info("Team已经存在这个项目组了，不需要创建，直接取出");
            return this.getGroupByName(groupName).getGroupId();
        }
        return System.currentTimeMillis();
    }


    /**
     * 查询一个项目组下的人员
     *
     * @return:{groupId=107533985382400, tenantId=101781516320768,
     *                                   groupName=DevOps项目组, users=[{userId=17760,
     *                                   tenantId=101781516320768, userName=薛孟琳,
     *                                   nickName=, email=mlxuef@eazybuilder.com}]}
     *
     */
    public String getGroup(String groupId) {
        Team team = this.getGroupByGroupId(groupId);
        LocalGroupReturn localGroupReturn = new LocalGroupReturn();
        localGroupReturn.setGroupId(groupId);
        localGroupReturn.setGroupName(team.getName());
        localGroupReturn.setTenantId("0000");
        List<User> teamMembers = team.getMembers();
        List<UpmsUserVo> upmsUserVos = new ArrayList<UpmsUserVo>();
        teamMembers.forEach((user) ->{
            UpmsUserVo upmsUserVo = new UpmsUserVo();
            upmsUserVo.setEmail(user.getEmail());
            upmsUserVo.setUserId(user.getId());
            upmsUserVo.setEmployeeId(user.getEmployeeId());
            upmsUserVo.setUserName(user.getName());
            upmsUserVo.setDeptName(user.getDepartment());
            upmsUserVo.setPhoneNumber(user.getPhone());
            upmsUserVos.add(upmsUserVo);
        } );
        localGroupReturn.setUsers(upmsUserVos);
        return JSON.toJSONString(localGroupReturn);
    }
}
