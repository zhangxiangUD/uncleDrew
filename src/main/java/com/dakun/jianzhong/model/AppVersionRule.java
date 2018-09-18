package com.dakun.jianzhong.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "app_version_rule")
public class AppVersionRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 版本id
     */
    @Column(name = "version_id")
    private String versionId;

    /**
     * 更新规则：0：所有用户 1：版本号 2：用户组 3：用户id
     */
    @Column(name = "rule_type")
    private Integer ruleType;

    /**
     * rule_type对应的对象值
     */
    @Column(name = "rule_val")
    private String ruleVal;

    /**
     * 是否强制更新 0：否 1：是
     */
    @Column(name = "is_force")
    private Integer isForce;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

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
     * 获取版本id
     *
     * @return version_id - 版本id
     */
    public String getVersionId() {
        return versionId;
    }

    /**
     * 设置版本id
     *
     * @param versionId 版本id
     */
    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    /**
     * 获取更新规则：0：所有用户 1：版本号 2：用户组 3：用户id
     *
     * @return rule_type - 更新规则：0：所有用户 1：版本号 2：用户组 3：用户id
     */
    public Integer getRuleType() {
        return ruleType;
    }

    /**
     * 设置更新规则：0：所有用户 1：版本号 2：用户组 3：用户id
     *
     * @param ruleType 更新规则：0：所有用户 1：版本号 2：用户组 3：用户id
     */
    public void setRuleType(Integer ruleType) {
        this.ruleType = ruleType;
    }

    /**
     * 获取rule_type对应的对象值
     *
     * @return rule_val - rule_type对应的对象值
     */
    public String getRuleVal() {
        return ruleVal;
    }

    /**
     * 设置rule_type对应的对象值
     *
     * @param ruleVal rule_type对应的对象值
     */
    public void setRuleVal(String ruleVal) {
        this.ruleVal = ruleVal;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getIsForce() {
        return isForce;
    }

    public void setIsForce(Integer isForce) {
        this.isForce = isForce;
    }
}