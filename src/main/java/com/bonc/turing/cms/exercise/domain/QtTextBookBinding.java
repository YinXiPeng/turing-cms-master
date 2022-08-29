package com.bonc.turing.cms.exercise.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author lky
 * @desc 教材绑定(绑定题目,绑定代码块)
 * @date 2019/12/25 21:23
 */
@Data
@Entity
public class QtTextBookBinding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer type;           //绑定类型(1:题库,2:代码块)
    private String title;           //标题
    private String titleNumber;    //题目编号
    private Integer language;       //语言(0:无,1:c++,2:java,3:python,4:python3,5:c)
    private String code;            //代码
    private Integer pagingId;         //组id
}
