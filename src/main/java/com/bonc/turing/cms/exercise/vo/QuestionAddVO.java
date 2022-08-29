package com.bonc.turing.cms.exercise.vo;

import com.bonc.turing.cms.exercise.type.QuestionType;
import lombok.Data;

import java.util.List;

/**
 * description:试题新增VO类
 *
 * @author lxh
 * @date 2020/3/23 15:16
 */
@Data
public class QuestionAddVO {

    /**
     * 试题名称
     */
    private String name;

    /**
     * 试题内容
     */
    private String content;

    /**
     * 选择题正确选项
     */
    private List<String> correctOptionItem;

    /**
     * 试题类型 选择题 编程题 视频问答题 简答题
     */
    private QuestionType type;

    /**
     * 选择题选项
     */
    private List<OptionAddVO> optionList;

}
