package com.bonc.turing.cms.exercise.domain;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 教师信息表
 * 将用户关联教材 自动成为教师
 * qt_teacher_info
 */
@Data
@ToString
@Entity
public class QtTeacherInfo {

    //主键 同sys_user_info
    @Id
    private String guid;
    // 名字
    private String name;
    //真实姓名
    private String realName;
    //简介
    private String introduction;
    // 当前教师评分 每次学生评分后重新计算
    private Double stars;
    //标签 字符串分隔
    private String tags;
    //证件照片地址
    private String photoUrls;
    //专业方向
    private String major;
    // 直播时间 eg:'2-1':2代表周几，1代表时间段 共五个时间段（1、上午(09:00-11:30)  2、中午(11:30-14:00)  3、下午(14:00-18:00) 4、晚上(18:00-24:00) 5、全天(9:00-24:00)）
    private String availableTime;
    //教师等级
    private String level;
    //  状态 0  未审核 默认状态 1  审核通过 2、审核不通过
    private String state;
    //删除标识位 也可以控制其他状态 -1 为删除
    private int flag;
    //身份证号
    private String idCard;
    //手机号
    private  String phone;
    //讲解课程范围id集合(平台复合型课程)
    private String textBookIds;
    //老师是否上线0否 1是（用于后台控制）
    private int isOnline;


}
