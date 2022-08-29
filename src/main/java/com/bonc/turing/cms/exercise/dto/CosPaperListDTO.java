package com.bonc.turing.cms.exercise.dto;

import com.bonc.turing.cms.exercise.type.PaperOrderType;
import lombok.Data;

import java.util.Date;

/**
 * description: 试卷列表DTO
 *
 * @author lxh
 * @date 2020/3/23 20:50
 */
@Data
public class CosPaperListDTO {

    /**
     * 试卷ID
     */
    private String id;

    /**
     * 试卷名称
     */
    private String name;

    /**
     * 试卷预置完成时间
     */
    private Integer finishTime;

    /**
     * 试题数量
     */
    private Integer questionCount;

    /**
     * 选择题数量
     */
    private Integer optionQuestionCount;

    /**
     * 简答题数量
     */
    private Integer shortQuestionCount;

    /**
     * 视频问答题数量
     */
    private Integer videoQuestionCount;

    /**
     * 编程题数量
     */
    private Integer programQuestionCount;

    /**
     * 试题顺序
     */
    private PaperOrderType paperOrder;

    /**
     * 所属学校ID
     */
    private String schoolId;

    /**
     * 所属老师ID
     */
    private String createById;

    /**
     * 开始考试时间
     */
    private Long examTime;

    /**
     * 备注信息
     */
    private String remark;

}
