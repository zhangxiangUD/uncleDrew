package com.dakun.jianzhong.model;

import javax.persistence.*;

@Table(name = "social_attitude")
public class SocialAttitude {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer uid;

    private Integer type;

    @Column(name = "followed_id")
    private Integer followedId;

    private Integer attitude;
    /*数据状态*/
    private Integer state;

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
     * @return type
     */
    public Integer getType() {
        return type;
    }

    /**
     * @param type
     */
    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getFollowedId() {
        return followedId;
    }

    public void setFollowedId(Integer followedId) {
        this.followedId = followedId;
    }

    /**
     * @return attitude
     */
    public Integer getAttitude() {
        return attitude;
    }

    /**
     * @param attitude
     */
    public void setAttitude(Integer attitude) {
        this.attitude = attitude;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}