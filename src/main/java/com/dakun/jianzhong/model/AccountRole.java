package com.dakun.jianzhong.model;

import javax.persistence.*;

@Table(name = "account_role")
public class AccountRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "role_name")
    private String roleName;

    @Column(name = "page_id")
    private Integer pageId;

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
     * @return role_name
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * @param roleName
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    /**
     * @return page_id
     */
    public Integer getPageId() {
        return pageId;
    }

    /**
     * @param pageId
     */
    public void setPageId(Integer pageId) {
        this.pageId = pageId;
    }
}