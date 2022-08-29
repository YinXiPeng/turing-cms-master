package com.bonc.turing.cms.exercise.domain;

import com.bonc.turing.cms.exercise.type.PaperOrderType;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * description: 试卷持久化类
 *
 * @author lxh
 * @date 2020/3/23 11:26
 */
@Data
@Entity
@Table(name = "cos_paper")
public class CosPaper {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    /**
     * 试卷名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 预置完成时间单位/秒
     */
    @Column(name = "finish_time")
    private Integer finishTime;

    /**
     * 试题数量
     */
    @Column(name = "question_count")
    private Integer questionCount;

    /**
     * 选择题数量
     */
    @Column(name = "option_question_count")
    private Integer optionQuestionCount;

    /**
     * 简答题数量
     */
    @Column(name = "short_question_count")
    private Integer shortQuestionCount;

    /**
     * 视频问答题数量
     */
    @Column(name = "video_question_count")
    private Integer videoQuestionCount;

    /**
     * 编程题数量
     */
    @Column(name = "program_question_count")
    private Integer programQuestionCount;

    /**
     * 试题顺序
     */
    @Column(name = "paper_order")
    private PaperOrderType paperOrder;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 开始考试时间
     */
    @Column(name = "exam_time")
    private Date examTime;

    /**
     * 创建人ID/老师ID
     */
    @Column(name = "create_by_id")
    private String createById;

    /**
     * 创建人名称/老师名称
     */
    @Column(name = "create_by_name")
    private String createByName;

    /**
     * 备注信息
     */
    @Column(name = "remark")
    private String remark;

    /**
     * 所属课程ID
     */
    @Column(name = "course_id")
    private String courseId;

    /**
     * 所属学校ID
     */
    @Column(name = "school_id")
    private String schoolId;

    /**
     * 是否删除
     */
    @Column(name = "is_delete")
    private Integer isDelete;

}
