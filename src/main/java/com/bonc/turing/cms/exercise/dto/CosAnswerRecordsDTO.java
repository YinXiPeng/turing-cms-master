package com.bonc.turing.cms.exercise.dto;

import lombok.Data;
import lombok.ToString;

/**
 * 用户答题
 * @author lxm
 */
@Data
@ToString
public class CosAnswerRecordsDTO {
    /**
     * 试卷名
     */
    private String paperName;
    /**
     * 答题记录id
     */
    private String id;

    /**
     * 学生姓名
     */
    private String name;
    /**
     * 学生学号
     */
    private String studentId;
    /**
     * 学生成绩
     */
    private Float score;
    /**
     * 状态
     */
    private Integer status;
}
