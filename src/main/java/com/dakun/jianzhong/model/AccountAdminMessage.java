package com.dakun.jianzhong.model;

import javax.persistence.*;
import java.util.Date;

@Table(name = "account_admin_message")
public class AccountAdminMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 发送者id
     */
    @Column(name = "sendId")
    private Integer sendid;

    /**
     * 接受者id
     */
    @Column(name = "recId")
    private Integer recid;

    /**
     * 1=系统消息
     */
    private Integer type;

    /**
     * 消息内容
     */
    private String message;

    /**
     * -10=已删除，-1=发送失败，0=未读，1=已读
     */
    private Integer state;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "last_modify_time")
    private Date lastModifyTime;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取发送者id
     *
     * @return sendId - 发送者id
     */
    public Integer getSendid() {
        return sendid;
    }

    /**
     * 设置发送者id
     *
     * @param sendid 发送者id
     */
    public void setSendid(Integer sendid) {
        this.sendid = sendid;
    }

    /**
     * 获取接受者id
     *
     * @return recId - 接受者id
     */
    public Integer getRecid() {
        return recid;
    }

    /**
     * 设置接受者id
     *
     * @param recid 接受者id
     */
    public void setRecid(Integer recid) {
        this.recid = recid;
    }

    /**
     * 获取1=系统消息
     *
     * @return type - 1=系统消息
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置1=系统消息
     *
     * @param type 1=系统消息
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 获取消息内容
     *
     * @return message - 消息内容
     */
    public String getMessage() {
        return message;
    }

    /**
     * 设置消息内容
     *
     * @param message 消息内容
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 获取-10=已删除，-1=发送失败，0=未读，1=已读
     *
     * @return state - -10=已删除，-1=发送失败，0=未读，1=已读
     */
    public Integer getState() {
        return state;
    }

    /**
     * 设置-10=已删除，-1=发送失败，0=未读，1=已读
     *
     * @param state -10=已删除，-1=发送失败，0=未读，1=已读
     */
    public void setState(Integer state) {
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
}