package com.eazybuilder.ci.service;

import com.eazybuilder.ci.base.AbstractCommonServiceImpl;
import com.eazybuilder.ci.entity.PipelineProfile;
import com.eazybuilder.ci.entity.QTeam;
import com.eazybuilder.ci.entity.TeamNamespace;
import com.eazybuilder.ci.entity.devops.DevopsInit;
import com.eazybuilder.ci.entity.devops.QDevopsInit;
import com.eazybuilder.ci.repository.TeamNamespaceDao;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamNamespaceService extends AbstractCommonServiceImpl<TeamNamespaceDao, TeamNamespace> {

    @Autowired
    private EntityManager entityManager;

    //查询工厂实体
    private JPAQueryFactory queryFactory;


    @PostConstruct
    public void initFactory(){
        queryFactory = new JPAQueryFactory(entityManager);
    }

    public TeamNamespace findByNameSpaceName(PipelineProfile pipelineProfile){
        String teamId = pipelineProfile.getTeamId();
        String code = pipelineProfile.getNameSpace();
        QDevopsInit devopsInit = QDevopsInit.devopsInit;
        QTeam qTeam = QTeam.team;
        List<DevopsInit> devopsInits = queryFactory.select(devopsInit).from(devopsInit,qTeam)
                .where(devopsInit.id.eq(qTeam.devopsTeamId).and(qTeam.id.eq(teamId))).fetch();
        Assert.isTrue(!CollectionUtils.isEmpty(devopsInits),String.format("没有通过teamId：【%s】找到对应的项目初始化信息",teamId));
        List<TeamNamespace> teamNamespaces = devopsInits.get(0).getTeamNamespaces().stream().filter(teamNamespace -> StringUtils.equals(teamNamespace.getCode(),code)).collect(Collectors.toList());
        Assert.isTrue(!CollectionUtils.isEmpty(teamNamespaces),String.format("流水线设定：【%s】对应的TeamNamespance没有找到!",pipelineProfile.getName()));
        return teamNamespaces.get(0);
    }
}
