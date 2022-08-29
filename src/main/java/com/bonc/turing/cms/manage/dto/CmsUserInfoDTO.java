package com.bonc.turing.cms.manage.dto;

import com.bonc.turing.cms.exercise.domain.CosSchool;
import com.bonc.turing.cms.manage.entity.user.CmsPermissions;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author lky
 * @date 2019/7/23 19:45
 */
@Data
public class CmsUserInfoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String guid;
    private String deviceid;
    private String email;
    private String headimgurl;
    private String userName;
    private String realName;
    private int role;
    private String salt;
    private String password;
    private Long createdTime;
    private String createdId;
    private Long lastModifyTime;
    private String lastModifyId;
    private int state;
    private String phone;
    private int isDeleted;
    private List<CmsPermissions> permissions;
    private String token;
    private CosSchool cosSchool;
}
