package com.bonc.turing.cms.exercise.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * description: 选项持久化类
 *
 * @author lxh
 * @date 2020/3/23 11:48
 */
@Data
@Entity
@Table(name = "cos_option")
public class CosOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 选项
     */
    @Column(name = "option_item")
    private String optionItem;

    /**
     * 选项内容
     */
    @Column(name = "content")
    private String content;

    /**
     * 所属试题ID
     */
    @Column(name = "cos_question_id")
    private Long cosQuestionId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

}
