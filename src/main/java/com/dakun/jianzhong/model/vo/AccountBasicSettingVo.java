package com.dakun.jianzhong.model.vo;

/**
 * Created by hexingfu on 2017/8/10.
 * 个人信息设置
 */
public class AccountBasicSettingVo {
    /*用户id*/
    private Integer id;
    /*用户类型 1：普通用户 2：专家用户 3：经销商用户*/
    private Integer userType;
    /*头像地址*/
    private String portrait;
    /*姓名*/
    private String name;
    /*简介*/
    private String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }
}
