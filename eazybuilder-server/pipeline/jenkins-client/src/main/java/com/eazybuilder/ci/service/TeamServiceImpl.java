package com.eazybuilder.ci.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;
import com.eazybuilder.ci.base.AbstractCommonServiceImpl;
import com.eazybuilder.ci.base.CommonService;
import com.eazybuilder.ci.constant.ActionScope;
import com.eazybuilder.ci.constant.RoleEnum;
import com.eazybuilder.ci.entity.*;
import com.eazybuilder.ci.entity.devops.DevopsInit;
import com.eazybuilder.ci.rabbitMq.SendRabbitMq;
import com.eazybuilder.ci.repository.GuardDao;
import com.eazybuilder.ci.repository.TeamDao;
import com.eazybuilder.ci.util.AuthUtils;
import com.querydsl.core.types.dsl.BooleanExpression;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TeamServiceImpl extends AbstractCommonServiceImpl<TeamDao, Team> implements CommonService<Team>{

    private static Logger logger = LoggerFactory.getLogger(TeamServiceImpl.class);

	@Value("${bug_blocker_name:默认BUG阻断阈值}")
	private String bugBlockerName;
	@Value("${bug_blocker_name_thresholdMin:30}")
	private double bugBlockerNameThresholdMin;
	@Value("${bug_blocker_name_thresholdMax:100}")
	private double bugBlockerNameThresholdMax;

	@Value("${guard.vulner_blocker_name:默认安全漏洞阻断阈值}")
	private String vulnerBlockerName;
	@Value("${guard.vulner_blocker_name_thresholdMin:30}")
	private double vulnerBlockerNameThresholdMin;
	@Value("${guard.vulner_blocker_name_thresholdMax:100}")
	private double vulnerBlockerNameThresholdMax;

	@Value("${guard.code_smell_blocker_name:默认编码规范阻断阈值}")
	private String codeSmellBlockerName;
	@Value("${guard.code_smell_blocker_name_thresholdMin:30}")
	private double codeSmellBlockerNameThresholdMin;
	@Value("${guard.code_smell_blocker_name_thresholdMax:100}")
	private double codeSmellBlockerNameThresholdMax;

	@Value("${message.ciGuardExchange}")
	public String ciGuardExchange;

	@Resource
	GuardDao guardDao;
	@Resource
	SendRabbitMq sendRabbitMq;

	@Autowired
	private DevopsInitServiceImpl devopsInitService;

	//TODO 这里以后需要改为单个团队信息
	public void sendGuardRabbiqMq(){
	    logger.info("开始发送门禁数据");
        //每次保存项目组数据时，先将所有项目组的数据发送到mq中。
        Iterable<Team> all = dao.findAll();
        List<Team> teamList = new ArrayList<>();
        all.forEach(single ->teamList.add(single));
		List<Guard>  guardsList=(List<Guard>)guardDao.findAll(QGuard.guard.level.eq("default"));
        for(Team team:teamList){
            List<Guard> guards = new ArrayList<>();
			Set<TeamThreshold> teamThresholds =  team.getTeamThresholds().stream()
					.filter(teamThreshold-> teamThreshold.getActionScope() == ActionScope.CODE_PUSH || teamThreshold.getActionScope()== null).collect(Collectors.toSet());
			if (!CollectionUtils.isEmpty(teamThresholds)) {
				 List<String> blockerIds  =     teamThresholds.stream().map(TeamThreshold::getBlockerId).collect(Collectors.toList());
				team.setGuards((List<Guard>)guardDao.findAll(QGuard.guard.id.in(blockerIds)));
			} else {
                logger.info("项目组没有设置阈值，使用默认的");
				team.setGuards(guardsList);
			}
        }
        String teamJson = JSON.toJSONString(teamList, SerializerFeature.DisableCircularReferenceDetect);
		logger.info("发送项目阈值信息：{}",teamJson);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("teams",teamJson);
        sendRabbitMq.sendMsg(jsonObject.toString(),ciGuardExchange,"");
    }

	public DevopsInit getDevopsInit(Team team){
		return devopsInitService.findOne(team.getDevopsTeamId());
	}
	public Set<TeamNamespace> getTeamNameSpace(Team team){
		DevopsInit devopsInit = getDevopsInit(team);
		Assert.isTrue(devopsInit!=null,"项目组对应的项目组初始化信息被删除！");
		return devopsInit.getTeamNamespaces();
	}
	@Override
	public void save(Team entity) {
		super.save(entity);
        try {
            sendGuardRabbiqMq();
        } catch (Exception e) {
            logger.error("发送门禁数据失败，不做处理： {}-{}",e.getMessage(),e);
        }
	}

	public Collection<Team> findByUser(User user){
		return (Collection<Team>) dao.findAll(QTeam.team.members.contains(user));
	}
	public Team findByName(String  name){
		return   dao.findOne(QTeam.team.name.eq(name)).orElse(null);
	}
	public Collection<String> getMyTeamIds(){
		User user=AuthUtils.getCurrentUser();
		List<String> ids=Lists.newArrayList();
		if(Role.existRole(user.getRoles(), RoleEnum.admin)) {
			Iterable<Team> teams=findAll();
			teams.forEach(team->ids.add(team.getId()));
		}else {
			findByUser(user).forEach(team->ids.add(team.getId()));
		}
		return ids;
	}
	public Team findByGroupId(String  groupId){
		return   dao.findOne(QTeam.team.groupId.eq(Long.parseLong(groupId))).orElse(null);
	}
	@Override
	public Iterable<Team> findAll() {
		User currentUser=AuthUtils.getCurrentUser();
		if(currentUser!=null
				&&!Role.existRole(currentUser.getRoles(),RoleEnum.admin)){
			//filted by current user team
			return findByUser(currentUser);
		}
		return dao.findAll();
	}
	
	public boolean isTeamConfigManager(User user) {
		return dao.count(QTeam.team.ownerId.eq(user.getId()).or(QTeam.team.configers.contains(user)))>0;
	}

	@Override
	public Page<Team> pageSearch(Pageable pageable, String searchText) {
		BooleanExpression condition=null;
		User currentUser=AuthUtils.getCurrentUser();
		if(currentUser!=null
				&&!Role.existRole(currentUser.getRoles(),RoleEnum.admin)){
			//filted by current user 
			condition=QTeam.team.members.contains(currentUser).or(QTeam.team.ownerId.eq(currentUser.getId()));
		}
		
		if(StringUtils.isNotBlank(searchText)){
			if(condition!=null){
				condition=condition.and(QTeam.team.name.like("%"+searchText+"%"));
			}else{
				condition=QTeam.team.name.like("%"+searchText+"%");
			}
		}
		return condition==null?dao.findAll(pageable):dao.findAll(condition, pageable);
	}
	
}
