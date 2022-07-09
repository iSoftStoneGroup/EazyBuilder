package com.eazybuilder.ci.constant;

public enum RoleEnum {

    /**
     * 给角色设置优先级，upms可能会返回多个角色时，按照优先级取其一
     */
    developer(3),
    tester(4),
    teamleader(5),
    admin(1),
    testleader(6),
    audit(2);
    private  Integer  priority;

    RoleEnum() {
    }

    RoleEnum(Integer priority) {
        this.priority = priority;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }
}
