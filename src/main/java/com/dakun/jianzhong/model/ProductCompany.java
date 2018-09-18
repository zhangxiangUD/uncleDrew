package com.dakun.jianzhong.model;

import javax.persistence.Column;

public class ProductCompany {

    private Integer id;

    private String companyName;

    private String telephone;

    private String contact;

    private String address;

    private String catalog;

    private Byte registerFalg;

    private Byte tradenoteFalg;

    @Column(name = "line_flag")
    private Byte lineFlag;

    private String introduction;

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
     * @return company_name
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * @param companyName
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * @return telephone
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * @param telephone
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /**
     * @return contact
     */
    public String getContact() {
        return contact;
    }

    /**
     * @param contact
     */
    public void setContact(String contact) {
        this.contact = contact;
    }

    /**
     * @return address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return register_falg
     */
    public Byte getRegisterFalg() {
        return registerFalg;
    }

    /**
     * @param registerFalg
     */
    public void setRegisterFalg(Byte registerFalg) {
        this.registerFalg = registerFalg;
    }

    /**
     * @return tradenote_falg
     */
    public Byte getTradenoteFalg() {
        return tradenoteFalg;
    }

    /**
     * @param tradenoteFalg
     */
    public void setTradenoteFalg(Byte tradenoteFalg) {
        this.tradenoteFalg = tradenoteFalg;
    }

    /**
     * @return line_flag
     */
    public Byte getLineFlag() {
        return lineFlag;
    }

    /**
     * @param lineFlag
     */
    public void setLineFlag(Byte lineFlag) {
        this.lineFlag = lineFlag;
    }


    /**
     * @return introduction
     */
    public String getIntroduction() {
        return introduction;
    }

    /**
     * @param introduction
     */
    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }
}