package com.dakun.jianzhong.model.constant;

/**
 * Created by hexingfu on 2017/12/29.
 */
public enum  AppVersionRuleType {
    //更新规则：0：所有用户 1：版本号 2：用户角色 3：用户id
    ALL_USER(0,"所有用户"),
    VERSION_CODE(1,"版本号"),
    USER_TYPE(2,"用户角色"),
    USER_ID(3,"用户ID");
    private int state;
    private String info;
    AppVersionRuleType(int state,String info){
        this.state = state;
        this.info = info;
    }

    public int value() {
        return state;
    }
}
