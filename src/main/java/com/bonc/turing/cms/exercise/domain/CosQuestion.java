package com.bonc.turing.cms.exercise.domain;

import com.bonc.turing.cms.exercise.type.OptionType;
import com.bonc.turing.cms.exercise.type.QuestionType;
import com.bonc.turing.cms.exercise.utils.CorrectOptionConverter;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * description: 试题持久化类
 *
 * @author lxh
 * @date 2020/3/23 11:44
 */
@Data
@Entity
@Table(name = "cos_question")
public class CosQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 试题名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 试题内容
     */
    @Column(name = "content")
    private String content;

    /**
     * 正确选项
     */
    @Convert(converter = CorrectOptionConverter.class)
    @Column(name = "correct_option_item")
    private List<String> correctOptionItem;

    /**
     * 所属试卷ID
     */
    @Column(name = "cos_paper_id")
    private String cosPaperId;

    /**
     * 试题类型
     */
    @Column(name = "type")
    private QuestionType type;

    /**
     * 选择题类型
     */
    @Column(name = "option_type")
    private OptionType optionType;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

}
