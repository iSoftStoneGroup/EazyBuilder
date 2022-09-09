package com.eazybuilder.ci.service;

import com.eazybuilder.ci.controller.vo.UserVo;
import com.eazybuilder.ci.entity.BuildJob;
import com.eazybuilder.ci.entity.JobTrigger;
import com.eazybuilder.ci.entity.Team;
import com.eazybuilder.ci.repository.BuildJobDao;
import com.eazybuilder.ci.util.AuthUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.querydsl.core.types.Predicate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.redisson.Redisson;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * @author: mlxuef
 * @createTime: 2022/9/1
 * @description:
 **/
@PrepareForTest(AuthUtils.class)
@RunWith(PowerMockRunner.class)
public class BuildJobServiceTest {

    @InjectMocks
    BuildJobService service;

    @Mock
    BuildJobDao dao;

    @Mock
    Redisson redisson;

    @Mock
    TeamServiceImpl teamService;


    @Mock
    private JdbcTemplate template;


    RedissonClient redissonClient;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        Config config = new Config();
        config.useSingleServer()
                .setTimeout(1000000)
                .setAddress("redis://redis-plat.eazybuilder-devops.cn:16379");
        redissonClient = spy(Redisson.create(config));
    }

    @Test
    public void save() {
        BuildJob buildJob = new BuildJob();
        buildJob.setName("test");
        buildJob.setTriggerType(JobTrigger.manual);
        when(dao.save(buildJob)).thenReturn(buildJob);
        service.save(buildJob);

    }


    @Test
    public void save1() {
        BuildJob buildJob = new BuildJob();
        String buildJobId = "1";
        buildJob.setId(buildJobId);
        buildJob.setName("test");
        buildJob.setTriggerType(JobTrigger.watch_job_executed);
        String watchJobId = "2";
        buildJob.setWatchJobId(watchJobId);

        BuildJob watchJob = new BuildJob();
        watchJob.setId(watchJobId);
        watchJob.setTriggerType(JobTrigger.manual);
        Optional<BuildJob> optional = Optional.of(watchJob);
        when(dao.findById(buildJobId)).thenReturn(Optional.of(buildJob));
        when(dao.findOne(any(Predicate.class))).thenReturn(optional);
        when(dao.save(buildJob)).thenReturn(buildJob);

        Map<String,String> map = Maps.newHashMap();
        map.put(watchJobId,"watchJob");

        RMap<String,String> rMap =  redissonClient.getMap("nothing");
        doReturn(rMap).when(redisson).getMap(anyString());
        service.save(buildJob);

    }
    
    @Test
    public void updateNextTime(){
        BuildJob buildJob = new BuildJob();
        String buildJobId = "1";
        buildJob.setId(buildJobId);
        Long nextTime = 1000L;
        when(template.update(anyString(),any(Object[].class))).thenReturn(1);
        service.updateNextTime(buildJob,nextTime);
    }

    @Test
    public void findAllCronJob(){
        when(dao.findAll(any(Predicate.class))).thenReturn(Lists.newArrayList());
        service.findAllCronJob();
    }

    @Test
    public void pageSearchOnline(){
        PowerMockito.mockStatic(AuthUtils.class);
        UserVo user = new UserVo();
        when(AuthUtils.getCurrentUser()).thenReturn(user);
        Team team = new Team();
        team.setId("1");
        when(teamService.findByUser(user)).thenReturn(Arrays.asList(team));
        when(dao.findAll(any(Predicate.class), any(Pageable.class))).thenReturn(null);
        service.pageSearchOnline(PageRequest.of(1,10),"user");
    }


    @Test
    public void pageSearch(){
        PowerMockito.mockStatic(AuthUtils.class);
        UserVo user = new UserVo();
        when(AuthUtils.getCurrentUser()).thenReturn(user);
        Team team = new Team();
        team.setId("1");
        when(teamService.findByUser(user)).thenReturn(Arrays.asList(team));
        when(dao.findAll(any(Predicate.class), any(Pageable.class))).thenReturn(null);
        service.pageSearch(PageRequest.of(1,10),"user");
    }
}