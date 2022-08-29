package com.bonc.turing.cms.manage.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
public class CmsSpecial implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private int id;
  private String specialId;
  private Integer type;
  private Integer isTop;
  private Date topTime;
  private Integer sortMethod;
  private Integer isElite;
  private Integer isHead;
  private Integer state;
  private String modifyId;
  private Date headTime;
  private Integer isDeleted;
  private String remark;
  private Integer isHomeHide;
  @Transient
  private String userId;

}
