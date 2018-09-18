package com.dakun.jianzhong.model;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Table(name = "account_message")
public class AccountMessage {

    public static final int DELETED = -10;
    /**
     * 发送失败
     */
    public static final int FAILED = -1;
    /**
     * 未读
     */
    public static final int UNREAD = 0;
    /**
     * 已读
     */
    public static final int READ = 1;

    @Id
    private String id;

    private Integer uid;

    @ApiModelProperty(value = "接收人姓名")
    @Transient
    private String username;

    @ApiModelProperty(value = "接收人手机号")
    @Transient
    private String userMobile;

    /*消息产生（触发）用户*/
    private Integer puid;

    /*消息对应跳转参数，用于客户端用户点击通知或消息跳转到对应模块页面*/
    private String jumpParams;

    /*极光推送注册设备id*/
    @Transient
    private String registrationId;

    /**
     * 1-系统消息  2-新关注用户 3-新关注问题 4-新关注文章 5-新回答（问题） 6-新评论（文章） 7-新对话（问题） 8-新对话（文章） 9-新点赞（问题回答） 10-新点赞（文章评论） 11-奖品发货通知
     */
    @Column(name = "message_type")
    private Integer messageType;

    private String briefinfo;


    /**
     * =10已删除 0:未读 1：已读
     */
    private int state;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "last_modify_time")
    private Date lastModifyTime;

    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return uid
     */
    public Integer getUid() {
        return uid;
    }

    /**
     * @param uid
     */
    public void setUid(Integer uid) {
        this.uid = uid;
    }

    /**
     * 获取1-系统消息 2-新关注 3-新回答 4-新对话 5-新点赞
     *
     * @return message_type - 1-系统消息  2-新关注用户 3-新关注问题 4-新关注文章 5-新回答（问题） 6-新评论（文章） 7-新对话（问题） 8-新对话（文章） 9-新点赞（问题回答） 10-新点赞（文章评论） 11-奖品发货通知
     */
    public Integer getMessageType() {
        return messageType;
    }

    /**
     * 设置1-系统消息  2-新关注用户 3-新关注问题 4-新关注文章 5-新回答（问题） 6-新评论（文章） 7-新对话（问题） 8-新对话（文章） 9-新点赞（问题回答） 10-新点赞（文章评论） 11-奖品发货通知
     *
     * @param messageType 1-系统消息  2-新关注用户 3-新关注问题 4-新关注文章 5-新回答（问题） 6-新评论（文章） 7-新对话（问题） 8-新对话（文章） 9-新点赞（问题回答） 10-新点赞（文章评论） 11-奖品发货通知
     */
    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }

    /**
     * @return briefinfo
     */
    public String getBriefinfo() {
        return briefinfo;
    }

    /**
     * @param briefinfo
     */
    public void setBriefinfo(String briefinfo) {
        this.briefinfo = briefinfo;
    }


    /**
     * 获取=10已删除 0:未读 1：已读
     *
     * @return state - =10已删除 0:未读 1：已读
     */
    public int getState() {
        return state;
    }

    /**
     * 设置=10已删除 0:未读 1：已读
     *
     * @param state =10已删除 0:未读 1：已读
     */
    public void setState(int state) {
        this.state = state;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return last_modify_time
     */
    public Date getLastModifyTime() {
        return lastModifyTime;
    }

    /**
     * @param lastModifyTime
     */
    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public Integer getPuid() {
        return puid;
    }

    public void setPuid(Integer puid) {
        this.puid = puid;
    }

    public String getJumpParams() {
        return jumpParams;
    }

    public void setJumpParams(String jumpParams) {
        this.jumpParams = jumpParams;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }
}