package com.dakun.jianzhong.model.vo;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @author wangjie
 * @date 1/22/2018
 */
public class CompanyDefaultPassword {

    @NotNull(message = "公司id不能为空")
    private Integer companyId;

    @Length(min = 6, message = "密码不能小于6位数")
    private String password;

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
