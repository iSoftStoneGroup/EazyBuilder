package com.eazybuilder.ci.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.eazybuilder.ci.entity.Guard;
import com.eazybuilder.ci.entity.Team;


@Component
public class PipelineExecuteService {

    @Autowired
    private GuardService guardService;
    
    @Value("${ci.dingtalk.secret}")
    private  String dingtalkSecret;
    
    @Value("${ci.dingtalk.accessToken}")
    private String accessToken;
    





	public String getAccessToken() {
		return accessToken;
	}


	public String getDingtalkSecret() {
		return dingtalkSecret;
	}


    public Map<String, Guard> findTeamGuard(Team team) {
       return  guardService.findTeamGuard(team);
    }
}
