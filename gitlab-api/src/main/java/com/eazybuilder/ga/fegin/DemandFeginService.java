package com.eazybuilder.ga.fegin;

import com.eazybuilder.ga.fegin.vo.CheckCodeVo;
import com.eazybuilder.ga.fegin.vo.HotfixVo;
import com.eazybuilder.ga.fegin.vo.UpdateNacosRequestVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;

@FeignClient(name = "demand-management", path = "/demand-management", contextId = "demand-management")
public interface DemandFeginService {

    @GetMapping(path = "/check/checkCode")
    public CheckCodeVo checkCode(@RequestParam("gitPathAddr") String gitPathAddr,@RequestParam("userName") String userName,@RequestParam("code") Integer code,@RequestParam("message") String message);

    @GetMapping(path = "/issue/getTopIssueId")
    public Integer getTopIssueId(@RequestParam("issueId") Integer issueId);

    @GetMapping(path = "/issue/getIssueCustomFieldById")
    public HashMap<String,String> getIssueCustomFieldById(@RequestParam("issueId") Integer issueId);

    @GetMapping(path = "/issue/addNote")
    public void addNote(@RequestParam("code") Integer code,@RequestParam("message") String message);

    @PostMapping(path = "/issue/updateNacos")
    public void updateNacos(@RequestBody UpdateNacosRequestVo updateNacosRequestVo);

    @PostMapping(path = "/hotfix/createHotfixIssue")
    public void createHotfixIssue(@RequestBody HotfixVo hotfix);
}
