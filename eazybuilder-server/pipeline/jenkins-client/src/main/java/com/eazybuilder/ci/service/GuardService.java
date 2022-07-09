package com.eazybuilder.ci.service;

import com.google.common.collect.Maps;
import com.eazybuilder.ci.base.AbstractCommonServiceImpl;
import com.eazybuilder.ci.base.CommonService;
import com.eazybuilder.ci.constant.ActionScope;
import com.eazybuilder.ci.entity.*;
import com.eazybuilder.ci.repository.GuardDao;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GuardService extends AbstractCommonServiceImpl<GuardDao, Guard> implements CommonService<Guard>{

    @Resource
    TeamServiceImpl teamService;

    @Override
    public void save(Guard entity) {
        teamService.sendGuardRabbiqMq();
        super.save(entity);
    }


    public List<Guard> findDefaultGuard(){
        return (List<Guard>)dao.findAll(QGuard.guard.level.eq("default"));
    }

    public Map<String, Guard> findTeamGuard(Team team) {
        List<TeamThreshold> thresholds =  team.getTeamThresholds().stream().filter(teamThreshold -> teamThreshold.getActionScope() == ActionScope.CODE_PUSH).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(thresholds)){
            List<Guard> guards = findDefaultGuard();
            return convertToMap(guards);
        }
        List<String> guardId = thresholds.stream().map(TeamThreshold::getBlockerId).filter(blockId-> StringUtils.isNotEmpty(blockId)).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(guardId)){
            List<Guard> guards =  (List<Guard>)dao.findAll(QGuard.guard.id.in(guardId));
            return convertToMap(guards);
        }
        return null;
    }

    private Map<String, Guard> convertToMap(List<Guard> guards) {
        Map<ThresholdType, List<Guard>>   guardMap =  guards.stream().collect(Collectors.groupingBy(Guard::getGuardType));
        Map<String, Guard> guardResulMap = Maps.newHashMap();
        guardMap.entrySet().stream().forEach( entity ->{
            guardResulMap.put(entity.getKey().name(),entity.getValue().get(0));
        });
        return guardResulMap;
    }
}
