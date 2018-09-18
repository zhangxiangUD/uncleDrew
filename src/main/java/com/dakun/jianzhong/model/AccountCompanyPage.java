package com.dakun.jianzhong.model;

import javax.persistence.*;

@Table(name = "account_company_page")
public class AccountCompanyPage {

    public static final int DEFAULT_COMPANY_ID = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "company_id")
    private Integer companyId;

    @Column(name = "page_id")
    private Integer pageId;

    @Column(name = "role_ids")
    private String roleIds;

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
     * 获取公司id
     *
     * @return company_id - 公司id
     */
    public Integer getCompanyId() {
        return companyId;
    }

    /**
     * 设置公司id
     *
     * @param companyId 公司id
     */
    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    /**
     * 获取页面id
     *
     * @return page_id - 页面id
     */
    public Integer getPageId() {
        return pageId;
    }

    /**
     * 设置页面id
     *
     * @param pageId 页面id
     */
    public void setPageId(Integer pageId) {
        this.pageId = pageId;
    }

    /**
     * 获取角色ids
     *
     * @return role_ids - 角色ids
     */
    public String getRoleIds() {
        return roleIds;
    }

    /**
     * 设置角色ids
     *
     * @param roleIds 角色ids
     */
    public void setRoleIds(String roleIds) {
        this.roleIds = roleIds;
    }
}