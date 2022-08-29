package com.bonc.turing.cms.exercise.domain;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "hr_dictionary")
public class HrDictionary {

    @Id
    private String id;
    /**
     * 字典父id
     */
    private String parentId;
    /**
     * 是否含有子集
     */
    private int isParent;
    /**
     * 字典名称
     */
    private String dictionaryName;
    /**
     * 字典类型
     */
    private int dictionaryType;
    /**
     * 字典备注
     */
    private String dictionaryRemark;

}
