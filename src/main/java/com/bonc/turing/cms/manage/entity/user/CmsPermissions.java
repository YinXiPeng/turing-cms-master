package com.bonc.turing.cms.manage.entity.user;



import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
public class CmsPermissions implements Serializable{

  private static final long serialVersionUID = 1L;

  @Id
  private int id;
  private String permissionsName;
  private int parentsPermissions;
//  @ManyToMany(mappedBy = "permissions",fetch = FetchType.EAGER)
//  private List<CmsUserInfo> cmsUserInfos;

//  public List<CmsUserInfo> getCmsUserInfos(){
//    return null;
//  }

}
