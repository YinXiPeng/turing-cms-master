package com.bonc.turing.cms.exercise.dto;

import lombok.Data;

@Data
public class VideoCourseListDTO {
    private  String id; //课程id
    private  String bookName; //课程名
    private  String authorName;  //作者名
    private  double price; //价格
    private  int status; //课程状态（0下架 1上架）
    private  int studentNum; //学习人数
    private  int payType; //教材类型  1：免费 2：部分免费 3：全部收费
}
