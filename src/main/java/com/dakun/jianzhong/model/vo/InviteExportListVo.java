package com.dakun.jianzhong.model.vo;

/**
 * Created by hexingfu on 2017/9/1.
 */
public class InviteExportListVo {

    private Integer id;
    /*头像*/
    private String portrait;
    /*专家姓名*/
    private String expertName;
    /*简介描述*/
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

    public String getExpertName() {
        return expertName;
    }

    public void setExpertName(String expertName) {
        this.expertName = expertName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
