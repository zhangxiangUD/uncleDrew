package com.dakun.jianzhong.model.vo;

import com.dakun.jianzhong.model.AccountExpert;

public class AccountExpertVo extends AccountExpert {
    private String mobile;

    private String portrait;

    private String concernCrops;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getConcernCrops() {
        return concernCrops;
    }

    public void setConcernCrops(String concernCrops) {
        this.concernCrops = concernCrops;
    }
}
