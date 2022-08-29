package com.bonc.turing.cms.exercise.domain;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class QtTextBookPaging {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  private String textBookId;
  private String chapterId;
  private Integer beginPage;
  private Integer endPage;
  private Integer sort;

}
