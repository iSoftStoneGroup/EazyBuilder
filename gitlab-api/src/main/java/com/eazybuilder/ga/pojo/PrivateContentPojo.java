package com.eazybuilder.ga.pojo;

import java.io.Serializable;

public class PrivateContentPojo implements Serializable {

    private static final long serialVersionUID = -1L;

    private String title;

    private String text;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
