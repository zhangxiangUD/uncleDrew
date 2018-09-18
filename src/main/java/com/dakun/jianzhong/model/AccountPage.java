package com.dakun.jianzhong.model;

import javax.persistence.*;

@Table(name = "account_page")
public class AccountPage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String text;

    private String sref;

    private String icon;

    private String heading;

    private Integer level;

    @Column(name = "allow_roles")
    private String allowRoles;

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
     * @return text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return sref
     */
    public String getSref() {
        return sref;
    }

    /**
     * @param sref
     */
    public void setSref(String sref) {
        this.sref = sref;
    }

    /**
     * @return icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * @param icon
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * @return heading
     */
    public String getHeading() {
        return heading;
    }

    /**
     * @param heading
     */
    public void setHeading(String heading) {
        this.heading = heading;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getAllowRoles() {
        return allowRoles;
    }

    public void setAllowRoles(String allowRoles) {
        this.allowRoles = allowRoles;
    }
}