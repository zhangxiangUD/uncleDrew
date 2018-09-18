package com.dakun.jianzhong.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "account_sms")
public class AccountSms {
    @Id
    private String id;

    private Integer uid;

    private String mobile;

    private Integer state;

    @Column(name = "sms_content")
    private String smsContent;

    @Column(name = "sms_type")
    private Integer smsType;

    @Column(name = "sent_time")
    private Date sentTime;

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

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * @return sms_content
     */
    public String getSmsContent() {
        return smsContent;
    }

    /**
     * @param smsContent
     */
    public void setSmsContent(String smsContent) {
        this.smsContent = smsContent;
    }

    /**
     * @return sms_type
     */
    public Integer getSmsType() {
        return smsType;
    }

    /**
     * @param smsType
     */
    public void setSmsType(Integer smsType) {
        this.smsType = smsType;
    }

    /**
     * @return sent_time
     */
    public Date getSentTime() {
        return sentTime;
    }

    /**
     * @param sentTime
     */
    public void setSentTime(Date sentTime) {
        this.sentTime = sentTime;
    }
}