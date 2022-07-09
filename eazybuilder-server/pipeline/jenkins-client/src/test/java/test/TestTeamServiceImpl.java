package test;

import com.eazybuilder.ci.Application;
import com.eazybuilder.ci.entity.Team;
import com.eazybuilder.ci.service.TeamServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestTeamServiceImpl {
    @Resource
    TeamServiceImpl teamService;
    @Test
    public  void testFindByName(){
        Team testqwe = teamService.findByName("test");
        System.out.println(testqwe.getCheckReleasePipeline());

    }
}
