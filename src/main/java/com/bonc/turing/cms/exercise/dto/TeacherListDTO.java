package com.bonc.turing.cms.exercise.dto;

import lombok.Data;

/**
 * @desc 后台-导师列表
 * @author  yh
 * @date 2020.02.03
 */
@Data
public class TeacherListDTO {
    //导师id
    private String guid;
    // 名字
    private String name;
    //真实姓名
    private String realName;
    //身份证号
    private String idCard;
    //手机号
    private  String phone;
    //关联教材数
    private int bookNum;
    //直播次数
    private  int liveNum;
    // 当前教师评分
    private Double stars;
    //教师等级
    private String level;
    //老师是否上线0否 1是（用于后台控制）
    private int isOnline;

}
