package com.bonc.turing.cms.exercise.domain;


import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

@Data
@Entity
public class HrCompany {

  @Id
  @GeneratedValue(generator="system-uuid")
  @GenericGenerator(name="system-uuid",strategy="uuid")
  private String companyId;
  /**
   * 公司名称
   */
  private String companyName;
  /**
   * 公司规模
   */
  private Integer companySize;
  /**
   * 公司类型
   */
  private Integer companyType;
  /**
   * 公司简介
   */
  private String companyIntroduction;
  /**
   * 公司联系人姓名
   */
  private String contactPerson;
  /**
   * 邮箱
   */
  private String email;
  /**
   * 手机号
   */
  private String phone;
  /**
   * 公司标签
   */
  private String companyTags;
  /**
   * logo
   */
  private String logo;

}
