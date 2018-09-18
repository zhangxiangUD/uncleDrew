package com.dakun.jianzhong.model;

import javax.persistence.*;
import java.util.Date;

@Table(name = "account_address")
public class AccountAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 省
     */
    private Integer province;

    /**
     * 市
     */
    private Integer city;

    /**
     * 区县
     */
    private Integer district;
    @Transient
    private String provinceName;
    @Transient
    private String cityName;
    @Transient
    private String districtName;
    /**
     * 作物名称
     */
    private String cropName;

    /**
     * 详细地址
     */
    private String detail;

    /**
     * 百度地址id
     */
    @Column(name = "location_id")
    private String locationId;

    /**
     * 作物id
     */
    private Integer crop;

    /**
     * -10=删除，1=有效
     */
    private Integer state;

    @Column(name = "last_modify_time")
    private Date lastModifyTime;

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
     * 获取用户id
     *
     * @return user_id - 用户id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置用户id
     *
     * @param userId 用户id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取省
     *
     * @return province - 省
     */
    public Integer getProvince() {
        return province;
    }

    /**
     * 设置省
     *
     * @param province 省
     */
    public void setProvince(Integer province) {
        this.province = province;
    }

    /**
     * 获取市
     *
     * @return city - 市
     */
    public Integer getCity() {
        return city;
    }

    /**
     * 设置市
     *
     * @param city 市
     */
    public void setCity(Integer city) {
        this.city = city;
    }

    /**
     * 获取区县
     *
     * @return district - 区县
     */
    public Integer getDistrict() {
        return district;
    }

    /**
     * 设置区县
     *
     * @param district 区县
     */
    public void setDistrict(Integer district) {
        this.district = district;
    }

    /**
     * 获取详细地址
     *
     * @return detail - 详细地址
     */
    public String getDetail() {
        return detail;
    }

    /**
     * 设置详细地址
     *
     * @param detail 详细地址
     */
    public void setDetail(String detail) {
        this.detail = detail;
    }

    /**
     * 获取百度地址id
     *
     * @return location_id - 百度地址id
     */
    public String getLocationId() {
        return locationId;
    }

    /**
     * 设置百度地址id
     *
     * @param locationId 百度地址id
     */
    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    /**
     * 获取作物id
     *
     * @return crop - 作物id
     */
    public Integer getCrop() {
        return crop;
    }

    /**
     * 设置作物id
     *
     * @param crop 作物id
     */
    public void setCrop(Integer crop) {
        this.crop = crop;
    }

    /**
     * 获取-10=删除，1=有效
     *
     * @return state - -10=删除，1=有效
     */
    public Integer getState() {
        return state;
    }

    /**
     * 设置-10=删除，1=有效
     *
     * @param state -10=删除，1=有效
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

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getCropName() {
        return cropName;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
    }
}