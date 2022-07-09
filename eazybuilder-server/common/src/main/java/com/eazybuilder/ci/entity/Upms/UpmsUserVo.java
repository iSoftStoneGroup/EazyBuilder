package com.eazybuilder.ci.entity.Upms;

import java.io.Serializable;
import java.util.Objects;


public class UpmsUserVo implements Serializable{

    //判断用户是否在项目中。
    private boolean inTeam;
    private String phoneNumber;
    private String userId;
    private String userName;
    private String nickName;
    private String email;
    private String deptName;
    private Integer employeeId;


    public boolean isInTeam() {
        return inTeam;
    }

    public void setInTeam(boolean inTeam) {
        this.inTeam = inTeam;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpmsUserVo that = (UpmsUserVo) o;
        return inTeam == that.inTeam &&
                com.google.common.base.Objects.equal(userId, that.userId) &&
                com.google.common.base.Objects.equal(userName, that.userName) &&
                com.google.common.base.Objects.equal(nickName, that.nickName) &&
                com.google.common.base.Objects.equal(email, that.email) &&
                com.google.common.base.Objects.equal(deptName, that.deptName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inTeam, userId, userName, nickName, email);
    }
}
