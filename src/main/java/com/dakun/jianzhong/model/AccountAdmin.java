package com.dakun.jianzhong.model;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

@Table(name = "account_admin")
public class AccountAdmin {

    /** 公司默认用户名称 */
    public static final String DEFAULT_COMPANY_USERNAME = "default";

    /** 超级管理员公司id: 0 */
    public static final int DEFAULT_COMPANY_ID = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "用户名称")
    @Pattern(regexp = "^[a-z0-9_-]{3,15}$", message = "用户名为3-15位字母和数字")
    private String username;

    @ApiModelProperty(value = "密码", hidden = true)
    private String password;

    @ApiModelProperty(value = "角色id, 0=超级用户，1=企业管理员，2=审核，3=申报")
    @Column(name = "role_id")
    private Integer roleId;

    private String description;

    @ApiModelProperty(value = "联系方式")
    private String mobile;

    @ApiModelProperty(value = "公司id")
    @Column(name = "company_id")
    private Integer companyId;

    @ApiModelProperty(value = "公司名称")
    @Transient
    private String companyName;

    @ApiModelProperty(value = "状态, 1=正常, -1=停用", allowableValues = "1, -1")
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
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
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
     * @return mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * @param mobile
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}