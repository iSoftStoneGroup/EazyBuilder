package com.eazybuilder.ga.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class FlywayQueryPojo {

    private String url;

    private String userName;

    private String password;

    private Date date;

}
