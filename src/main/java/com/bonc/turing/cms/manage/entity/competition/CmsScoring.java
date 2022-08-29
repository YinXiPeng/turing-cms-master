package com.bonc.turing.cms.manage.entity.competition;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * @ProjectName: cms
 * @Package: com.bonc.turing.cms.manage.entity.competition
 * @ClassName: CmsScoring
 * @Author: bxt
 * @Description:
 * @Date: 2019/7/12 18:38
 * @Version: 1.0
 */
@Entity
@Data
public class CmsScoring {
    @Id
    private String id;
    private String scoringItem;
    private Float weight;
    private String competitionInfoId;
    private int competitionInfoType;
    private Date caretaDate;

}
