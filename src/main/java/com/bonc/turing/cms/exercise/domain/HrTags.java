package com.bonc.turing.cms.exercise.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class HrTags {

  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private int id;
  /**
   * 标签类型
   */
  private String tagType;
  /**
   * 标签名
   */
  private String tagName;
  /**
   * 被使用的次数
   */
  private Integer usedNum;

}
