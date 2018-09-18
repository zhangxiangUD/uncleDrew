package com.dakun.jianzhong.session;

/**
 * 登录用户信息
 * <p>User: wangjie
 * <p>Date: 1/8/2018
 * @author wangjie
 */
public class LoginUser {

    private Integer userId;

    private String username;

    /** 账号类型: 1=app账户, 2=管理系统账户 */
    private Integer accountType;

    private Integer userType;

    private Integer companyId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
}
