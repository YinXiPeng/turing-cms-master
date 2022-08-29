package com.bonc.turing.cms.exercise.domain;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author yh
 * @desc 课程表
 * @date 2019.12.25
 */
@Data
@Entity
public class QtTextBook {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id; //主键id
    private String bookName; //教材名
    private String authorName;  //作者名
    private String coverUrl;  //封面url
    private String briefTxt; //一句话简介
    private String description; //教材内容介绍
    private String direction; //教材方向
    private String label;//标签
    private String catalogue; //目录
    private double price; //价格
    private int status; //教材状态（0下架 1上架）
    private int isDeleted;//是否删除（0否，1是）
    private String createById; //创建人id
    private double discount; //折扣
    private Date createTime; //创建时间
    private int type; //课程类型（0：默认教材 1：视频）
    private int randomNum;  //初始随机数

}
