package com.eazybuilder.ci.controller;

import com.alibaba.fastjson.JSONArray;
import com.eazybuilder.ci.entity.Upms.UpmsUserVo;
import com.eazybuilder.ci.upms.QueryUpmsData;
import com.wordnik.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/upms")
public class UpmsController {

    @Resource
    QueryUpmsData queryUpmsData;

    /**
     * @return
     */
    @RequestMapping(value = "/getUpmsUsers", method = RequestMethod.GET)
    @ApiOperation("查询群组用户数据")
    public String list(@RequestParam(value = "groupId") String groupId) {
        String groupData = queryUpmsData.getGroup(groupId);
        JSONObject groupJson = JSONObject.fromObject(groupData);
        if(groupJson.containsKey("data")){
            JSONObject data = groupJson.getJSONObject("data");
            if(data.containsKey("users")){
                return StringEscapeUtils.unescapeJava(data.getJSONArray("users").toString());
            }
        }
        return null;
    }

    @RequestMapping(value = "/getUpmsAllUsers", method = RequestMethod.GET)
    @ApiOperation("查询租户用户数据")
    public List<UpmsUserVo> list() {
        String userData = queryUpmsData.getUserList();
        JSONObject jsonObject = JSONObject.fromObject(userData);
        List<UpmsUserVo> dataArr = JSONArray.parseArray(jsonObject.getJSONArray("data").toString(), UpmsUserVo.class);
        return dataArr;
    }
}
