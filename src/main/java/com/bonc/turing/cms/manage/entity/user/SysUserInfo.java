package com.bonc.turing.cms.manage.entity.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户信息.
 * @author liuyunkai
 * @date 2018-11-6 10:40
 */
@Entity
public class SysUserInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private String guid;//用户id;
    @NotEmpty(message = "用户名不能为空")
    @Size(min = 2, max = 20, message = "用户名长度必须为4-20个字符")
    @Column(nullable = false, length = 20, unique = true)
    private String username;//账号用户名
    @NotEmpty(message = "密码不能为空")
    @Size(max = 100, message = "密码长度最多100个字符")
    @Column(length = 100)
    private String password; //密码;
    private String salt;//加密密码的盐
    private byte state;//用户状态,0:普通用户(白名单用户) 1：高危用户 2：灰名单用户 3：黑名单用户
    private String phone; //手机号;
    private long registts; //注册时间
    private String openid;//微信Id
    private String nickname;//用户昵称
    private String headimgurl;//微信头像地址
    private String deviceid;//设备ID
    private String sdkid;//SDK ID
    private String manifesto;//宣言
    private String wechatname;//微信昵称
    private String email;//邮箱
    private String personScore;//个人等级
    private String direction;//行业方向
    private String education;//最高学历
    private String position;//当前职位
    private String professiontag;//职业标签
    private String persontag;//个人标签
    private long lastModify = new Date().getTime(); //最新修改时间
    private String realName = ""; //姓名
    private Boolean isStu = true; //是否为学生
    private String idCard = ""; //身份证
    private String qq = ""; //qq号
    private String isEnterprise = "0"; //0-不是企业账号, 1-是, 2-其他权限;
    private String noviceGuidanceSteps; //步骤（1，2，3，4，5，6，7，8，9，10（10代表已完成））
    /**
     * 是否管理员（角色：true 管理员；false 普通用户),注册时默认是普通用户
     */
    private Boolean isAdmin = false;
    private String userLevel = "1";// 默认用户等级
    private Integer source = 0;

    private int isDeleted;

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getPersontag() {
        return persontag;
    }

    public void setPersontag(String persontag) {
        this.persontag = persontag;
    }

    public String getProfessiontag() {
        return professiontag;
    }

    public void setProfessiontag(String professiontag) {
        this.professiontag = professiontag;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public byte getState() {
        return state;
    }

    public void setState(byte state) {
        this.state = state;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getRegistts() {
        return registts;
    }

    public void setRegistts(long registts) {
        this.registts = registts;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getSdkid() {
        return sdkid;
    }

    public void setSdkid(String sdkid) {
        this.sdkid = sdkid;
    }

    public String getManifesto() {
        return manifesto;
    }

    public void setManifesto(String manifesto) {
        this.manifesto = manifesto;
    }

    public String getWechatname() {
        return wechatname;
    }

    public void setWechatname(String wechatname) {
        this.wechatname = wechatname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPersonScore() {
        return personScore;
    }

    public void setPersonScore(String personScore) {
        this.personScore = personScore;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public long getLastModify() {
        return lastModify;
    }

    public void setLastModify(long lastModify) {
        this.lastModify = lastModify;
    }

    public String getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Boolean getIsStu() {
        return isStu;
    }

    public void setIsStu(Boolean stu) {
        this.isStu = isStu;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getIsEnterprise() {
        return isEnterprise;
    }

    public void setIsEnterprise(String isEnterprise) {
        this.isEnterprise = isEnterprise;
    }

    public String getNoviceGuidanceSteps() {
        return noviceGuidanceSteps;
    }

    public void setNoviceGuidanceSteps(String noviceGuidanceSteps) {
        this.noviceGuidanceSteps = noviceGuidanceSteps;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    /**
     * 密码盐.
     * @return
     */
    public String getCredentialsSalt(){
        return this.username+this.salt;
    }

    @Override
    public String toString() {
        return "SysUserInfo{" +
                "guid='" + guid + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                ", state=" + state +
                ", phone='" + phone + '\'' +
                ", registts=" + registts +
                ", openid='" + openid + '\'' +
                ", nickname='" + nickname + '\'' +
                ", headimgurl='" + headimgurl + '\'' +
                ", deviceid='" + deviceid + '\'' +
                ", sdkid='" + sdkid + '\'' +
                ", manifesto='" + manifesto + '\'' +
                ", wechatname='" + wechatname + '\'' +
                ", email='" + email + '\'' +
                ", personScore='" + personScore + '\'' +
                ", direction='" + direction + '\'' +
                ", education='" + education + '\'' +
                ", position='" + position + '\'' +
                ", professiontag='" + professiontag + '\'' +
                ", persontag='" + persontag + '\'' +
                ", lastModify=" + lastModify +
                ", isAdmin=" + isAdmin +
                ", userLevel='" + userLevel + '\'' +
                ", realName='" + realName + '\'' +
                ", isStu='" + isStu + '\'' +
                ", idCard='" + idCard + '\'' +
                ", qq=" + qq +
                ", isEnterprise=" + isEnterprise +
                '}';
    }

}
