package com.eazybuilder.ga.pojo;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class DingTalkText {
    @ApiModelProperty(value = "文本类型")
    private String msgType = "text";

    @Setter
    @Getter
    @ApiModelProperty(value = "显示内容")
    private String content;

    @Setter
    @Getter
    @ApiModelProperty(value = "是否at所有人")
    private Boolean isAtAll;

    @Setter
    @Getter
    @ApiModelProperty(value = "被@人的手机号(在content里添加@人的手机号)")
    private List<String> atMobiles;


    public String getMsgType() {
        return "text";
    }


    public String getJSONObjectString() {
        // text类型
        JSONObject content = new JSONObject();
        content.put("content", this.getContent());

        // at some body
        JSONObject atMobile = new JSONObject();
        if(this.getAtMobiles().size() > 0){
            List<String> mobiles = new ArrayList<String>();
            for (int i=0;i<this.getAtMobiles().size();i++){
                mobiles.add(this.getAtMobiles().get(i));
            }
            if(mobiles.size()>0){
                atMobile.put("atMobiles", mobiles);
            }
            atMobile.put("isAtAll", this.getIsAtAll());
        }

        JSONObject json = new JSONObject();
        json.put("msgtype", this.getMsgType());
        json.put("text", content);
        json.put("at", atMobile);
        return json.toJSONString();
    }

}
