package com.eazybuilder.ga.pojo.merge;

import lombok.Data;

@Data
public class Last_commit {

    private String id;
    private String message;
    private String timestamp;
    private String url;
    private Author author;

}
