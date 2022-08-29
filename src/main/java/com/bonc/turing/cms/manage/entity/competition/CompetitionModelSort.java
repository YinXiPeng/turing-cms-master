package com.bonc.turing.cms.manage.entity.competition;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @ProjectName: cms
 * @Package: com.bonc.turing.cms.manage.entity.competition
 * @ClassName: CompetitionModelSort
 * @Author: bxt
 * @Description: 竞赛-模型-排序
 * @Date: 2019/7/19 11:03
 * @Version: 1.0
 */
@Entity
@JsonIgnoreProperties(allowSetters = true, value = {"handler", "hibernateLazyInitializer"})
@Table(name = "notebook_sort")
@Data
public class CompetitionModelSort {

    @Id
    private String notebookId;
    private Integer isTop;
    private Integer isRefinement;
    private Integer sort;
    private Date modifyTime;
    private String remark;
    private String competitionId;
}
