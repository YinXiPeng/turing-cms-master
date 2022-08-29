package com.bonc.turing.cms.manage.entity.user;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * 用户受欢迎，其他用户对当前用户的操作
 */
@Entity
public class UserVisit implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;//id;
    private String guid;//用户id;
    private long fork;//fork操作
    private long collect;//被收藏
    private long comment;//评论

    private long datause;//数据利用
    private long reading;//阅读总数
    private long follower;//

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public long getFork() {
        return fork;
    }

    public void setFork(long fork) {
        this.fork = fork;
    }

    public long getCollect() {
        return collect;
    }

    public void setCollect(long collect) {
        this.collect = collect;
    }

    public long getComment() {
        return comment;
    }

    public void setComment(long comment) {
        this.comment = comment;
    }

    public long getDatause() {
        return datause;
    }

    public void setDatause(long datause) {
        this.datause = datause;
    }

    public long getReading() {
        return reading;
    }

    public void setReading(long reading) {
        this.reading = reading;
    }

    public long getFollower() {
        return follower;
    }

    public void setFollower(long follower) {
        this.follower = follower;
    }


    public UserVisit() {
    }

    public UserVisit(String guid) {
        this.guid = guid;
    }

    @Override
    public String toString() {
        return "UserVisit{" +
                ", guid='" + guid + '\'' +
                ", fork=" + fork +
                ", collect=" + collect +
                ", comment=" + comment +
                ", datause=" + datause +
                ", reading=" + reading +
                ", follower=" + follower +
                '}';
    }
}
