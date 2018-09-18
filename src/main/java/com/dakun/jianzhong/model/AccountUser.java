package com.dakun.jianzhong.model;

import com.dakun.jianzhong.model.validation.QueryGroup;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Table(name = "account_user")
public class AccountUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty(value = "极光推送registrationId")
    private String registrationId;

    @ApiModelProperty(notes = "手机号")
    private String mobile;

    @ApiModelProperty(notes = "姓名")
    private String name;

    @ApiModelProperty(notes = "头像地址")
    private String portrait;

    @ApiModelProperty(notes = "状态", allowableValues = "0=停用, 1=启用")
    private Integer state;

    @ApiModelProperty(notes = "用户类型", allowableValues = "1=普通用户, 2=专家, 3=经销商")
    @NotNull(groups = QueryGroup.class)
    private Integer usertype;

    @ApiModelProperty(notes = "注册时间")
    private Date regtime;

    @ApiModelProperty(notes = "默认地址")
    @Column(name = "default_add")
    private String defaultAdd;

    @ApiModelProperty(notes = "邀请码")
    @Column(name = "invite_code")
    private String inviteCode;

    @ApiModelProperty(notes = "临时密码")
    private String temppwd;

    @ApiModelProperty(notes = "关注作物IDS")
    @Column(name = "concern_crops")
    private String concernCrops;

    @ApiModelProperty(value = "上次登录时间")
    @Column(name = "last_login_time")
    private Date lastLoginTime;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @ApiModelProperty(notes = "设备唯一识别码")
    @Column(name = "device_id")
    private String deviceId;

    @ApiModelProperty(notes = "粉丝数")
    @Column(name = "follower_num")
    private Integer followerNum;

    @ApiModelProperty(notes = "关注数")
    @Column(name = "follow_num")
    private Integer followNum;

    @Transient
    private AccountFollowUser followUser;

    @Transient
    private String description;

    private Date lastModifyTime;

    public Date getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * @param mobile
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return portrait
     */
    public String getPortrait() {
        return portrait;
    }

    /**
     * @param portrait
     */
    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * @return usertype
     */
    public Integer getUsertype() {
        return usertype;
    }

    /**
     * @param usertype
     */
    public void setUsertype(Integer usertype) {
        this.usertype = usertype;
    }

    /**
     * @return regtime
     */
    public Date getRegtime() {
        return regtime;
    }

    /**
     * @param regtime
     */
    public void setRegtime(Date regtime) {
        this.regtime = regtime;
    }

    /**
     * @return default_add
     */
    public String getDefaultAdd() {
        return defaultAdd;
    }

    /**
     * @param defaultAdd
     */
    public void setDefaultAdd(String defaultAdd) {
        this.defaultAdd = defaultAdd;
    }

    /**
     * @return invite_code
     */
    public String getInviteCode() {
        return inviteCode;
    }

    /**
     * @param inviteCode
     */
    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    /**
     * @return temppwd
     */
    public String getTemppwd() {
        return temppwd;
    }

    /**
     * @param temppwd
     */
    public void setTemppwd(String temppwd) {
        this.temppwd = temppwd;
    }

    /**
     * @return concern_crops
     */
    public String getConcernCrops() {
        return concernCrops;
    }

    /**
     * @param concernCrops
     */
    public void setConcernCrops(String concernCrops) {
        this.concernCrops = concernCrops;
    }

    /**
     * @return follower_num
     */
    public Integer getFollowerNum() {
        return followerNum;
    }

    /**
     * @param followerNum
     */
    public void setFollowerNum(Integer followerNum) {
        this.followerNum = followerNum;
    }

    /**
     * @return follow_num
     */
    public Integer getFollowNum() {
        return followNum;
    }

    /**
     * @param followNum
     */
    public void setFollowNum(Integer followNum) {
        this.followNum = followNum;
    }

    public AccountFollowUser getFollowUser() {
        return followUser;
    }

    public void setFollowUser(AccountFollowUser followUser) {
        this.followUser = followUser;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }
}