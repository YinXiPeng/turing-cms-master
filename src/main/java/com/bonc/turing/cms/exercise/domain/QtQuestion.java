package com.bonc.turing.cms.exercise.domain;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author lky
 * @desc 题目
 * @date 2019/12/25 20:45
 */
@Data
@Entity
public class QtQuestion {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid",strategy="uuid")
    private String qid;
    private Integer questionType; //题目的类型(1:选择,2:算法)
    private String questionName;  //题目的名称
    private String questionDescription;   //题目描述
    private Integer chooseType;    //选择题类型(1:单选,2:多选,3:非选择题)
    private String chooseOptions;  //选择题选项
    private String chooseTrueAnswer;   //选择题答案
    private String questionDirection;        //题目方向
    private String questionLabel;             //题目标签
    private Integer questionDifficulty;       //题目难度(1:简单,2:一般,3:困难)
    private Date createdTime;                 //创建时间
    private Date lastModifyTime;              //最后修改时间
    private String createdGuid;               //创建人
    private Integer answerNum=0;              //答题数
    private Integer commentNum=0;             //评论数
    private Integer codeNum=0;                //题解数
    private Integer questionStatus=0;         //题目状态(0:下线,1:上线)

}
