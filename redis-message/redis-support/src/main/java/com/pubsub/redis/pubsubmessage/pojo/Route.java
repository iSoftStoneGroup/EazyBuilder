package com.pubsub.redis.pubsubmessage.pojo;

import lombok.Data;

 
@Data
public class Route {
    /**
     * 网关路由在redis中索引
     */
    private Integer index;
    /**
     * id 网关路由唯一标识
     */
    private String id;
    /**
     * checkUrl 健康安全检查
     */
    private String checkUrl;
    /**
     * weight 权重
     */
    private Integer weight;
    /**
     * group 分组用于负载均衡
     */
    private String group;
    /**
     * path 路由识别路径
     */
    private String path;
    /**
     * uri 网关路由转发地址
     */
    private String uri;

}
