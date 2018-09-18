package com.dakun.jianzhong.model.query;

/**
 * @author wangjie
 * @date 1/10/2018
 */
public class AccountAdminQuery extends PageQuery {

    private Integer companyId;

    private String username;

    private Integer roleId;

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
