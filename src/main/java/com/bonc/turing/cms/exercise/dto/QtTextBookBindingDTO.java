package com.bonc.turing.cms.exercise.dto;

import com.bonc.turing.cms.exercise.domain.QtTextBookBinding;
import lombok.Data;

import java.util.List;

/**
 * @author lky
 * @desc
 * @date 2019/12/26 16:30
 */
@Data
public class QtTextBookBindingDTO implements Cloneable {

    private String textBookId;      //教材id
    private String chapterId;       //章节id
    private Integer beginPage;      //起始页
    private Integer endPage;        //结束页
    private List<String> titleNumbers;    //题目编号
    private List<QtTextBookBinding> codeList; //代码列表
    private Integer sort;                  //章节排序

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
