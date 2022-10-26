package com.eazybuilder.ci.controller;

import com.eazybuilder.ci.local.QueryLocalData;
import com.eazybuilder.ci.service.TeamServiceImpl;
import com.wordnik.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/local")
public class LocalController {
    @Resource
    QueryLocalData queryLocalData;

    @Autowired
    TeamServiceImpl teamService;

    /**
     * @return
     */
    @RequestMapping(value = "/getLocalUsers", method = RequestMethod.GET)
    @ApiOperation("查询群组用户数据")
    public String list(@RequestParam(value = "groupId") String groupId) {
        String groupData = queryLocalData.getGroup(groupId);
        JSONObject groupJson = JSONObject.fromObject(groupData);
        return StringEscapeUtils.unescapeJava(groupJson.getJSONArray("users").toString());
    }
}
