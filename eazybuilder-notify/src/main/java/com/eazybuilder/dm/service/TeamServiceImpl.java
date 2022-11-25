package com.eazybuilder.dm.service;


import com.eazybuilder.dm.base.AbstractCommonServiceImpl;
import com.eazybuilder.dm.base.CommonService;
import com.eazybuilder.dm.dao.TeamDao;
import com.eazybuilder.dm.entity.QTeam;
import com.eazybuilder.dm.entity.Team;
import org.springframework.stereotype.Service;


/**
 *  
 */
@Service
public class TeamServiceImpl extends AbstractCommonServiceImpl<TeamDao, Team> implements CommonService<Team> {
    public Team findByTeamCode(String  teamCode){
        return  dao.findOne(QTeam.team.teamCode.eq(teamCode)).orElse(null);
    }
}
