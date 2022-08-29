package com.bonc.turing.cms.exercise.domain;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * @Author lky
 * @Description 学生老师的信息汇总表
 * @Date 16:24 2020/3/23
 * @Param
 * @return
 **/
@Data
@Entity
public class CosUserMsg {

  @Id
  @GeneratedValue(generator="system-uuid")
  @GenericGenerator(name="system-uuid",strategy="uuid")
  private String id;
  private String guid;
  private String schoolName;
  private String schoolId;
  private String collegeName;
  private String phone;
  private String mail;
  private Integer role;
  private String peopleId;
  private String realName;
  private Date createdTime;
  private Date modifyTime;
  private String photo;
}
