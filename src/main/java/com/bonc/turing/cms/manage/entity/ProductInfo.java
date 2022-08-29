package com.bonc.turing.cms.manage.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @Auther: LY
 * @Data: 2019/4/13
 * @Description: 商品信息
 */
@Entity
@GenericGenerator(name = "sysUUID", strategy = "uuid")
public class ProductInfo implements Serializable {
    private static final long serialVersionUID = -7018714746648454577L;
    @Id
    @GeneratedValue(generator = "sysUUID")
    private String id;

    private String productName;

    private String  productVersion;

    private String productDesc;

    private String unitPrice;

    private String subscribeTime;

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

    public String getSubscribeTime() {
        return subscribeTime;
    }

    public void setSubscribeTime(String subscribeTime) {
        this.subscribeTime = subscribeTime;
    }
}
