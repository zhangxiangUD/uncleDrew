package com.dakun.jianzhong.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "app_version")
public class AppVersion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 版本号
     */
    @Column(name = "version_code")
    private Integer versionCode;

    /**
     * 版本名称
     */
    @Column(name = "version_name")
    private String versionName;

    /**
     * 客户端系统:1:android 2:ios
     */
    @Column(name = "os_type")
    private Integer osType;

    /**
     * 数据状态:-10:无效 1:生效
     */
    @Column(name = "state")
    private Integer state;
    /**
     * app下载地址
     */
    @Column(name = "url")
    private String url;

    /**
     * 上线时间
     */
    private Date uptime;

    /**
     * 版本备注
     */
    @Column(name = "version_note")
    private String versionNote;

    /**
     * 渠道：0：自有服务器 
     */
    private Integer channel;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 最后修改时间
     */
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
     * 获取版本号
     *
     * @return version_code - 版本号
     */
    public Integer getVersionCode() {
        return versionCode;
    }

    /**
     * 设置版本号
     *
     * @param versionCode 版本号
     */
    public void setVersionCode(Integer versionCode) {
        this.versionCode = versionCode;
    }

    /**
     * 获取版本名称
     *
     * @return version_name - 版本名称
     */
    public String getVersionName() {
        return versionName;
    }

    /**
     * 设置版本名称
     *
     * @param versionName 版本名称
     */
    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    /**
     * 获取客户端系统:1:android 2:ios
     *
     * @return os_type - 客户端系统:1:android 2:ios
     */
    public Integer getOsType() {
        return osType;
    }

    /**
     * 设置客户端系统:1:android 2:ios
     *
     * @param osType 客户端系统:1:android 2:ios
     */
    public void setOsType(Integer osType) {
        this.osType = osType;
    }

    /**
     * 获取上线时间
     *
     * @return uptime - 上线时间
     */
    public Date getUptime() {
        return uptime;
    }

    /**
     * 设置上线时间
     *
     * @param uptime 上线时间
     */
    public void setUptime(Date uptime) {
        this.uptime = uptime;
    }

    /**
     * 获取版本备注
     *
     * @return version_note - 版本备注
     */
    public String getVersionNote() {
        return versionNote;
    }

    /**
     * 设置版本备注
     *
     * @param versionNote 版本备注
     */
    public void setVersionNote(String versionNote) {
        this.versionNote = versionNote;
    }

    /**
     * 获取渠道：0：自有服务器 
     *
     * @return channel - 渠道：0：自有服务器 
     */
    public Integer getChannel() {
        return channel;
    }

    /**
     * 设置渠道：0：自有服务器 
     *
     * @param channel 渠道：0：自有服务器 
     */
    public void setChannel(Integer channel) {
        this.channel = channel;
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

    /**
     * 获取最后修改时间
     *
     * @return last_modify_time - 最后修改时间
     */
    public Date getLastModifyTime() {
        return lastModifyTime;
    }

    /**
     * 设置最后修改时间
     *
     * @param lastModifyTime 最后修改时间
     */
    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}