package com.bonc.turing.cms.manage.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Auther:lcd
 * @Data:2019/4/9
 * @Description
 */
@Entity
@GenericGenerator(name = "sysUUID", strategy = "uuid")
public class OrderInfo implements Serializable {

    private static final long serialVersionUID = 6390295570812923499L;
    @Id
    @GeneratedValue(generator = "sysUUID")
    private String orderId;

    private int authorizationNumber;

    private int subscribeTimeLimit;

    private BigDecimal price;

    private String userId;

    private int status;

    private Date createTime;

    private Date orderTime;

    private String productId;

    private String actualPayMoney;

    private int isInvoice;

    private String discount;

    private String remarks;

    public OrderInfoUser getOrderInfoUser() {
        return orderInfoUser;
    }

    public void setOrderInfoUser(OrderInfoUser orderInfoUser) {
        this.orderInfoUser = orderInfoUser;
    }

    @Transient
    private OrderInfoUser orderInfoUser;

    @Transient
    private String phoneNumber;

    @Transient
    private Date expireTime;

    @Transient
    private String productName;

    @Transient
    private String productVersion;
    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getActualPayMoney() {
        return actualPayMoney;
    }

    public void setActualPayMoney(String actualPayMoney) {
        this.actualPayMoney = actualPayMoney;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getAuthorizationNumber() {
        return authorizationNumber;
    }

    public void setAuthorizationNumber(int authorizationNumber) {
        this.authorizationNumber = authorizationNumber;
    }

    public int getSubscribeTimeLimit() {
        return subscribeTimeLimit;
    }

    public void setSubscribeTimeLimit(int subscribeTimeLimit) {
        this.subscribeTimeLimit = subscribeTimeLimit;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public int getIsInvoice() {
        return isInvoice;
    }

    public void setIsInvoice(int isInvoice) {
        this.isInvoice = isInvoice;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
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

    public String getRemarks(){return remarks;}

    public void setRemarks(String remarks){this.remarks = remarks;}
}
