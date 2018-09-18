package com.dakun.jianzhong.model.vo;

/**
 * 粉丝列表vo
 * Created by hexingfu on 2017/8/11.
 */
public class AccountFollowUserVo {
    /*粉丝用户ID*/
    private Integer id;
    /*粉丝角色 1=普通用户 2=专家 3=经销商*/
    private Integer userType;
    /*粉丝姓名*/
    private String name;
    /*粉丝头像*/
    private String portrait;
    /*简介，普通用户无简介*/
    private String description;
    /*当前用户是否已关注该用户 0：未关注 1：已关注*/
    private int isFollowed;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIsFollowed() {
        return isFollowed;
    }

    public void setIsFollowed(int isFollowed) {
        this.isFollowed = isFollowed;
    }
}
