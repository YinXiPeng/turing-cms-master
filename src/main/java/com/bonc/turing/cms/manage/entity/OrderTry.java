package com.bonc.turing.cms.manage.entity;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
public class OrderTry implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private int id;

  private String guid;
  private String productId;
  private Date createdTime;

  @Transient
  private OrderInfoUser orderInfoUser;

  @Transient
  private String phoneNumber;

  @Transient
  private Date expireTime;

  @Transient
  private String productName;

  @Transient
  private String productVersion;

}
