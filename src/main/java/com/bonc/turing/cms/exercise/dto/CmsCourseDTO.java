package com.bonc.turing.cms.exercise.dto;

import lombok.Data;


@Data
public class CmsCourseDTO {

    private String id; //课程id
    private String name; //课程名称
    private String authorName;//课程老师
    private Integer status; //课程状态 0：未上线 1：上线
    private Integer isFree; //判断是否免费
    private Long createTime; //创建时间
    private Integer form=1; //课程形式（1：复合型课程 2：直播 5:实训课
    private String schoolCourseId;//实训课程id
    private int state;//实训课状态1显示2隐藏
}
