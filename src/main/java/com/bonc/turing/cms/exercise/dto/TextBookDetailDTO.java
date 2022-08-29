package com.bonc.turing.cms.exercise.dto;

import lombok.Data;

import java.util.List;

@Data
public class TextBookDetailDTO {
    private  String id; //教材id
    private  String bookName;//教材名
    private  String authorName; //作者名
    private  String coverUrl; //封面url
    private  String description; //教材内容介绍
    private  String label; //教材标签
    private  String catalogue; //目录
    private  double price; //价格
    private  int userType; //用户类型 1：普通用户 2：学生 3：导师
    private  int isStudy; //是否学习过
    private  int isPay; //是否购买 1：是 0：否
    private  int payType; //费用类型  1：免费 2：部分免费 3：全部收费
    private  int studyPeopleNum; //学习人数
    private List<String> excellentRank; //大牛榜
}
