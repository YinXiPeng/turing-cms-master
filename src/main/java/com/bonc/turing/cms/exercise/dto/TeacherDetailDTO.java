package com.bonc.turing.cms.exercise.dto;

import com.bonc.turing.cms.exercise.domain.QtTeacherTryCourse;
import com.bonc.turing.cms.exercise.domain.QtTextBook;
import com.bonc.turing.cms.exercise.domain.QtTextTeacherVideo;
import lombok.Data;

import java.util.List;


/**
 * @desc 导师详情信息
 * @author yh
 * @date 2020.02.04
 */
@Data
public class TeacherDetailDTO {
    //主键
    private String guid;
    // 名字
    private String name;
    //真实姓名
    private String realName;
    //简介
    private String introduction;
    //标签 字符串分隔
    private String tags;
    //证件照片地址
    private String photoUrls;
    //专业方向
    private String major;
    //可直播时间
    private String availableTime;
    //教师等级
    private String level;
    //身份证号
    private String idCard;
    //手机号
    private String phone;
    //讲解范围教材集合（重点：教材id,教材名称）
//    private List<QtTextBook> textBooks;
    private List<QtTeacherTryCourse> textBooks;
    //讲解视频集合
    private List<QtTextTeacherVideo> videos;
}
