package com.dakun.jianzhong.model;

import javax.persistence.*;
import java.util.List;

@Table(name = "account_dealer")
public class AccountDealer {
    @Id
    private Integer id;

    @Column(name = "id_number")
    private String idNumber;

    @Column(name = "dealer_name")
    private String dealerName;

    @Column(name = "ic_front")
    private String icFront;

    @Column(name = "ic_back")
    private String icBack;

    @Column(name = "business_lisence")
    private String businessLisence;

    private String address;

    @Transient
    private List<AccountPictures> shopFrontPictures;//经销商证书，多图

    private String description;

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
     * @return id_number
     */
    public String getIdNumber() {
        return idNumber;
    }

    /**
     * @param idNumber
     */
    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    /**
     * @return dealer_name
     */
    public String getDealerName() {
        return dealerName;
    }

    /**
     * @param dealerName
     */
    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    /**
     * @return ic_front
     */
    public String getIcFront() {
        return icFront;
    }

    /**
     * @param icFront
     */
    public void setIcFront(String icFront) {
        this.icFront = icFront;
    }

    /**
     * @return ic_back
     */
    public String getIcBack() {
        return icBack;
    }

    /**
     * @param icBack
     */
    public void setIcBack(String icBack) {
        this.icBack = icBack;
    }

    /**
     * @return business_lisence
     */
    public String getBusinessLisence() {
        return businessLisence;
    }

    /**
     * @param businessLisence
     */
    public void setBusinessLisence(String businessLisence) {
        this.businessLisence = businessLisence;
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

    public List<AccountPictures> getShopFrontPictures() {
        return shopFrontPictures;
    }

    public void setShopFrontPictures(List<AccountPictures> shopFrontPictures) {
        this.shopFrontPictures = shopFrontPictures;
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
}