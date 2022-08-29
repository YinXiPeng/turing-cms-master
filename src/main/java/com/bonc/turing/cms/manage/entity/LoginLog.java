package com.bonc.turing.cms.manage.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * 用户登录信息.
 * @author liuyunkai
 * @date 2018-11-27 20:40
 */
@Entity
public class LoginLog implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;//id;
    private String guid;//用户id;
    private String username;//账号.
    private String phone; //手机号;
    private long logints; //登录时间

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getLogints() {
        return logints;
    }

    public void setLogints(long logints) {
        this.logints = logints;
    }

    @Override
    public String toString() {
        return "LoginLog{" +
                "guid='" + guid + '\'' +
                ", username='" + username + '\'' +
                ", phone='" + phone + '\'' +
                ", logints=" + logints +
                '}';
    }
}
