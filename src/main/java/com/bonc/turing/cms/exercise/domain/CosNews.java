package com.bonc.turing.cms.exercise.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * @desc 高校首页-资讯
 * @author yh
 * @date 2020.06.28
 */
@Data
@Entity
public class CosNews{
    /**
     * 主键
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容概述
     */
    private String overview;

    /**
     *图片
     */
    private String imgUrl;

    /**
     * markdown格式内容
     */
    private String markdownContent;

    /**
     * html格式内容
     */
    private String htmlContent;

    /**
     * 内容url地址
     */
    private String contentUrl;

    /**
     * 学校id
     */
    private String schoolId;

    /**
     * 是否公开(登录且为该校的可看私有，其余只能看公开)
     */
    private Integer isPublic;

    /**
     * 是否隐藏(隐藏将不在该校首页展示)
     */
    private Integer isHide;

    /**
     * 内容类型 1：文本 2：url地址
     */
    private Integer type;

    /**
     * 类型 1:咨询 2:公告
     */
    private Integer newsType;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;

}
