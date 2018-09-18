package com.dakun.jianzhong.model.vo;

/**
 * Created by hexingfu on 2017/8/15.
 * 我的库存列表vo
 */
public class AccountStockVo {
    /*产品名称*/
    private String productName;
    /*箱型*/
    private Integer boxType;
    /*库存*/
    private Integer stock;
    /*内包装单位*/
    private  String innerUnit;
    /*外包装单位*/
    private  String outerUnit;

    public Integer getBoxType() {
        return boxType;
    }

    public void setBoxType(Integer boxType) {
        this.boxType = boxType;
    }

    public String getInnerUnit() {
        return innerUnit;
    }

    public void setInnerUnit(String innerUnit) {
        this.innerUnit = innerUnit;
    }

    public String getOuterUnit() {
        return outerUnit;
    }

    public void setOuterUnit(String outerUnit) {
        this.outerUnit = outerUnit;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
