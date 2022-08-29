package com.bonc.turing.cms.exercise.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author lky
 * @desc 答题记录
 * @date 2019/12/26 15:44
 */
@Data
@Entity
public class QtAnswerRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String guid;                      //用户id
    private String questionId;               //题目id
    private Integer isTrue;                   //是否正确(1:正确,2:错误)
    private Integer recordSource;             //记录来源(0:题库,1:教材)
    private String referId;                   //指向的id(来源为题库:0,来源教材:教材id)
    private Date createdTime;                 //创建时间
    private String chapterId;                 //章节id
    private Integer beginPage;                //起始页
    private Integer endPage;                  //结束页
    private String userAnswer;                //用户的答案
}
