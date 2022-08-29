package com.bonc.turing.cms.exercise.vo;

import com.bonc.turing.cms.exercise.type.PaperOrderType;
import lombok.Data;

import java.util.List;

/**
 * description:试卷新增VO类
 *
 * @author lxh
 * @date 2020/3/23 15:13
 */
@Data
public class PaperAddUpdateVO {
    /**
     * 试卷ID,更新试卷时参数
     */
    private String id;

    /**
     * 试卷名称
     */
    private String name;

    /**
     * 预置完成时间/分钟
     */
    private Integer finishTime;

    /**
     * 开始考试时间
     */
    private Long examTime;

    /**
     * 试题顺序
     */
    private PaperOrderType paperOrder;

    /**
     * 所属老师ID
     */
    private String createById;

    /**
     * 所属课程ID
     */
    private String courseId;

    /**
     * 所属老师ID
     */
    private String schoolId;

    /**
     * 所属老师姓名
     */
    private String createByName;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 试卷试题集合
     */
    private List<QuestionAddVO> questionList;

}
