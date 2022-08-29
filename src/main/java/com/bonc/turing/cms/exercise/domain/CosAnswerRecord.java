package com.bonc.turing.cms.exercise.domain;


import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author lxm
 * 学生答题记录表
 */
@Data
@Entity
public class CosAnswerRecord {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid",strategy="uuid")
    private String id;
    /**
     * guid
     */
    private  String guid;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 答题人姓名
     */
    private  String name;
    /**
     * 开始答题时间
     */
    private Date startTime;
    /**
     * 结束答题时间
     */
    private Date endTime;
    /**
     *试卷id
     */
    private String paperId;
    /**
     * 试卷名称
     */
    private String paperName;
    /**
     * 正确率
     */
    private Float correctRate;
    /**
     * 成绩
     */
    private Float score;
    /**
     * 题目数量
     */
    private Integer questionNum;
    /**
     * 选择题数量
     */
    private Integer choiceNum;
    /**
     * 要求作答时间
     */
    private Integer totalTime;
    /**
     * 用户正确答案和题目
     */
    private String userAndCorrectAnswerJson;
    /**
     * 用户答案和题目
     */
    private String userAnswerJson;
    /**
     * 学生id
     */
    private String studentId;
    /**
     * 开考时间
     */
    private Date paperRequireStartTime;
    /**
     * 评语
     */
    private String comment;

}
