package com.eazybuilder.ci.entity;

import javax.persistence.*;

@Entity
@Table(name="CI_DEPLOY_CONFIG_Env")
public class DeployConfigDetailEnv {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id",columnDefinition="int(5)")
    private String id;


    /**
     * 名称
     */
    private String name;

    /**
     * 数据
     */
    private String data;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
