package com.dakun.jianzhong.model;

import javax.persistence.*;
import java.util.Date;

@Table(name = "account_pictures")
public class AccountPictures {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "relate_type")
    private Integer relateType;

    private Integer fieldid;

    private String picurl;

    private String description;

    private String reserved;

    @Column(name = "last_modify_time")
    private Date lastModifyTime;

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
     * @return relate_type
     */
    public Integer getRelateType() {
        return relateType;
    }

    /**
     * @param relateType
     */
    public void setRelateType(Integer relateType) {
        this.relateType = relateType;
    }

    /**
     * @return fieldid
     */
    public Integer getFieldid() {
        return fieldid;
    }

    /**
     * @param fieldid
     */
    public void setFieldid(Integer fieldid) {
        this.fieldid = fieldid;
    }

    /**
     * @return picurl
     */
    public String getPicurl() {
        return picurl;
    }

    /**
     * @param picurl
     */
    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }

    /**
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return reserved
     */
    public String getReserved() {
        return reserved;
    }

    /**
     * @param reserved
     */
    public void setReserved(String reserved) {
        this.reserved = reserved;
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