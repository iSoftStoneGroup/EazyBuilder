package com.eazybuilder.ci.event.listener;

import com.eazybuilder.ci.entity.BuildJob;
import com.eazybuilder.ci.entity.JobTrigger;
import com.eazybuilder.ci.service.BuildJobService;
import com.eazybuilder.ci.service.PipelineServiceImpl;
import com.eazybuilder.ci.service.async.PipelineExecuteResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.redisson.Redisson;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.Arrays;
import java.util.List;

import static org.powermock.api.mockito.PowerMockito.*;

/**
 * @author: mlxuef
 * @createTime: 2022/9/5
 * @description:
 **/

@PrepareForTest(BuildJob.class)
@RunWith(PowerMockRunner.class)
public class BuildJobDoneListenerTest {
    @InjectMocks
    BuildJobDoneListener buildJobDoneListener;

    @Mock
    PipelineServiceImpl pipelineService;

    @Mock
    Redisson redisson;


    RedissonClient redissonClient;

    @Mock
    BuildJobService buildJobService;



    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        Config config = new Config();
        config.useSingleServer()
                .setTimeout(1000000)
                .setAddress("redis://redis-plat.eazybuild-devops..cn:16379");
        redissonClient = spy(Redisson.create(config));
    }


    @Test
    public void onFinished() throws Exception {
        PipelineExecuteResult pipelineExecute1 = PipelineExecuteResult.newSuccess();
        PipelineExecuteResult pipelineExecute2 = PipelineExecuteResult.newFailed();
        String jobId = "1";
        pipelineExecute1.setJobId(jobId);
        pipelineExecute2.setJobId(jobId);
        RMap<String,String> rMap =  redissonClient.getMap("96");
        rMap.put(jobId,"测试");
        List<PipelineExecuteResult> results = Arrays.asList(pipelineExecute1,pipelineExecute2);
        BuildJob buildJob = new BuildJob();
        buildJob.setTriggerType(JobTrigger.watch_job_executed);
        String key  =  BuildJob.getArrangementJobRedisKey(jobId);
        doReturn(rMap).when(redisson).getMap(key);
        when(buildJobService.findOne(jobId)).thenReturn(buildJob);
        doNothing().when(pipelineService).triggerBatchPipeline(buildJob);
        buildJobDoneListener.onFinished(results);
    }
}