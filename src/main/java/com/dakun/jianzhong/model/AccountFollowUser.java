package com.dakun.jianzhong.model;

import javax.persistence.*;

@Table(name = "account_follow_user")
public class AccountFollowUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "f_id")
    private Integer fId;

    @Column(name = "state")
    private Integer state;

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
     * @return user_id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * @return f_id
     */
    public Integer getfId() {
        return fId;
    }

    /**
     * @param fId
     */
    public void setfId(Integer fId) {
        this.fId = fId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}