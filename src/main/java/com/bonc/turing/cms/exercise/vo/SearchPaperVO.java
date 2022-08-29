package com.bonc.turing.cms.exercise.vo;

import lombok.Data;

/**
 * description:试卷列表VO类
 *
 * @author lxh
 * @date 2020/3/23 20:46
 */
@Data
public class SearchPaperVO {

    /**
     * 创建时间开始
     */
    private Long createTimeStart;

    /**
     * 创建时间结束
     */
    private Long createTimeEnd;

    /**
     * 所属老师ID
     */
    private String createById;

    /**
     * 试卷名称
     */
    private String name;

}
