package com.bonc.turing.cms.manage.dto;

/**
 * @Auther: LY
 * @Data: 2019/4/13
 * @Description
 */
public class ProductInfoDTO {

    private String id;

    private String productName;

    private String  productVersion;

    private String productDesc;

    private String unitPrice;

    private String[] subscribeTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductVersion() {
        return productVersion;
    }

    public void setProductVersion(String productVersion) {
        this.productVersion = productVersion;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String[] getSubscribeTime() {
        return subscribeTime;
    }

    public void setSubscribeTime(String[] subscribeTime) {
        this.subscribeTime = subscribeTime;
    }
}
