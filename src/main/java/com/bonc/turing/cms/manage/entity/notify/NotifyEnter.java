package com.bonc.turing.cms.manage.entity.notify;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
@GenericGenerator(name = "sysUUID", strategy = "uuid")
public class NotifyEnter {

  @Id
  @GeneratedValue(generator = "sysUUID")
  private String notifyId;
  private String adminGuid;
  private String notifyTitle;
  private int notifyType;
  private String notifyMsg;
  private int notifyPhoneFlag;
  private int notifyEmailFlag;
  private int notifyWebFlag;
  private long notifyTime;
  private String jumpUrl1;
  private String competitionId;
  private int competitionState;
  private String tipsMsg1;
  private String competitionName;
  private int notifyUserType;
  private Date createdTime;
  private Date modifyTime;

}
