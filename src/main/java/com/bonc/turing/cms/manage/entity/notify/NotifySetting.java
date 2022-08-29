package com.bonc.turing.cms.manage.entity.notify;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@GenericGenerator(name = "sysUUID", strategy = "uuid")
public class NotifySetting {

  @Id
  @GeneratedValue(generator = "sysUUID")
  private String id;
  private String guid;
  private int webNotify=1;
  private int webRespond=1;
  private int webSystemNotice=1;
  private int webCompetition=1;
  private int webLike=1;
  private int webApplication=1;
  private int webCollection=1;
  private int state1=1;
  private int state2=1;
  private int state3=1;
  private int mailNotify=1;
  private int mailRespond=1;
  private int mailSystemNotice=1;
  private int mailCompetition=1;
  private int mailLike=1;
  private int mailApplication=1;
  private int mailCollection=1;
  private int remindMethod=0;
}
