package com.eazybuilder.ci.entity.devops;

import java.util.List;

public class DevopsInitIds {
    public String type;
    private List<String> ids;
    private String id;

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
