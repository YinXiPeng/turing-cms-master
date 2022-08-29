package com.bonc.turing.cms.manage.entity.user;


import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class CmsUserInfo implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  private String guid;
  private String deviceid;
  private String email;
  private String headimgurl;
  private String userName;
  private String nickname;
  private String realName;
  private int role;
  private String salt;
  private String password;
  private Date createdTime;
  private String createdId;
  private Date lastModifyTime;
  private String lastModifyId;
  private int state;
  private String phone;
  private int isDeleted;
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "cms_user_permissions",joinColumns = @JoinColumn(name = "guid"),
          inverseJoinColumns = @JoinColumn(name = "permissions_id"))
  private List<CmsPermissions> permissions;
  @Transient
  private String token;


    /**
     * 密码盐.
     * @return
     */
    public String getCredentialsSalt(){
        return this.userName+this.salt;
    }

}
