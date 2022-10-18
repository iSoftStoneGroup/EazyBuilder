package com.eazybuilder.ci.dto;

/**
 * @date 2021年12月6日
 * 开发（集成分支）-->处理中 不发送mq
 * 申请提测-->待审核
 * 申请提测通过->测试中，
 * 申请上线->待上线
 * 申请上线通过->待部署 不能提交代码
 * 上线部署->已上线 不能提交代码
 *
 */
public class IssuesStatusDto {

    /**
     * 需求状态
     */
    private String issuesStatus;
    /**
     * 修改人
     */
    private String userName;
    /**
     * 需求号
     */
    private String code;

    public String getIssuesStatus() {
        return issuesStatus;
    }

    public void setIssuesStatus(String issuesStatus) {
        this.issuesStatus = issuesStatus;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
