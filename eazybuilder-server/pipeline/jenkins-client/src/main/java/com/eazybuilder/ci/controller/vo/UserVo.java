package com.eazybuilder.ci.controller.vo;

import com.eazybuilder.ci.entity.Role;
import com.eazybuilder.ci.entity.Team;
import com.eazybuilder.ci.entity.User;
import org.springframework.beans.BeanUtils;

import java.util.List;

public class UserVo extends User {
    public List<Team> teamList;


    public UserVo(){

    }

    public static UserVo Instance(User usere){
        UserVo vo = new UserVo();
        BeanUtils.copyProperties(usere,vo);
        return vo;
    }


    public List<Team> getTeamList() {
        return teamList;
    }

    public void setTeamList(List<Team> teamList) {
        this.teamList = teamList;
    }


    public boolean isAuditReader(){
        return this.getRoles().stream().filter(Role::isAuditReader).count() > 0;
    }

    public boolean isAdmin(){
        return this.getRoles().stream().filter(Role::isAdmin).count() > 0;
    }
}
