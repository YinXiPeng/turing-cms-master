package com.bonc.turing.cms.manage.entity;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
public class Notebook implements Serializable {

  private static final long serialVersionUID = 6390295570812923499L;

  private String guid;
  @Id
  private String notebookId;
  private String notebookName;
  private String notebookDescription;
  private String notebookPath;
  private String notebookTools;
  private String parentId;
  private int version;
  private Date lastModified;
  private String kernelId;
  private String status;
  private int executeTime;
  private Date createTime;
 // private int isService;
  private int isPublic;
  private int isDelete;
  //private String typeId;
 // private String envId;
  // private String cephIdPort;
  //private String cephPath;
//  private String cpu;
//  private String gbMemory;
//  private String gpu;
//  private String imageId;
//  private String jupyterPath;


}
