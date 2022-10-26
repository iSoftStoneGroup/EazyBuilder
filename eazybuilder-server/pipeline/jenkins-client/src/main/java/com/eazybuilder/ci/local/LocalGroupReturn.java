package com.eazybuilder.ci.local;

import com.eazybuilder.ci.entity.Upms.UpmsUserVo;
import java.util.List;


public class LocalGroupReturn {
    private String groupId;
    private String tenantId;
    private String groupName;
    private List<UpmsUserVo> users;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<UpmsUserVo> getUsers() {
        return users;
    }

    public void setUsers(List<UpmsUserVo> users) {
        this.users = users;
    }
}
