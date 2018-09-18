package com.dakun.jianzhong.model;

import javax.persistence.*;

@Table(name = "account_city")
public class AccountCity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 行政代码
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 父id
     */
    @Column(name = "parent_id")
    private Integer parentId;

    /**
     * 首字母
     */
    @Column(name = "first_letter")
    private String firstLetter;

    /**
     * 城市等级
     */
    private Integer level;

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
     * 获取行政代码
     *
     * @return code - 行政代码
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置行政代码
     *
     * @param code 行政代码
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取名称
     *
     * @return name - 名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置名称
     *
     * @param name 名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取父id
     *
     * @return parent_id - 父id
     */
    public Integer getParentId() {
        return parentId;
    }

    /**
     * 设置父id
     *
     * @param parentId 父id
     */
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    /**
     * 获取首字母
     *
     * @return first_letter - 首字母
     */
    public String getFirstLetter() {
        return firstLetter;
    }

    /**
     * 设置首字母
     *
     * @param firstLetter 首字母
     */
    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }

    /**
     * 获取城市等级
     *
     * @return level - 城市等级
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * 设置城市等级
     *
     * @param level 城市等级
     */
    public void setLevel(Integer level) {
        this.level = level;
    }
}