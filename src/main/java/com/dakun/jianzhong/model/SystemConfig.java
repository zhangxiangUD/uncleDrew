package com.dakun.jianzhong.model;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Date;

@Table(name = "system_config")
public class SystemConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty(value = "类型，1=农药剂型")
    private Integer catalog;

    @ApiModelProperty(value = "catalog=0时，代表数据项的id；当catalog!=0时，表示数据项的顺序")
    private Integer value;

    @ApiModelProperty(value = "名称")
    @Column(name = "config_name")
    private String configName;

    private Integer state;

    @Column(name = "last_modify_time")
    private Date lastModifyTime;

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
     * @return catalog
     */
    public Integer getCatalog() {
        return catalog;
    }

    /**
     * @param catalog
     */
    public void setCatalog(Integer catalog) {
        this.catalog = catalog;
    }

    /**
     * @return config_name
     */
    public String getConfigName() {
        return configName;
    }

    /**
     * @param configName
     */
    public void setConfigName(String configName) {
        this.configName = configName;
    }

    /**
     * @return state
     */
    public Integer getState() {
        return state;
    }

    /**
     * @param state
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * @return last_modify_time
     */
    public Date getLastModifyTime() {
        return lastModifyTime;
    }

    /**
     * @param lastModifyTime
     */
    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}