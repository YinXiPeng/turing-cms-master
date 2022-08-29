package com.bonc.turing.cms.manage.service.user;

import com.alibaba.fastjson.JSONObject;
import com.bonc.turing.cms.common.utils.PageInfoEntity;
import com.bonc.turing.cms.manage.entity.user.SysUserInfo;
import com.bonc.turing.cms.manage.entity.user.CmsPermissions;
import com.bonc.turing.cms.manage.entity.user.CmsUserInfo;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CmsUserService {
    CmsUserInfo getByUsername(String username);

    Optional<CmsUserInfo> getByGuid(String guid);

    void addUserInfo(CmsUserInfo cmsUserInfo, SysUserInfo sysUserInfo);

    void updateUserInfo(CmsUserInfo cmsUserInfo,SysUserInfo sysUserInfo);

    Optional<CmsUserInfo> findByGuid(String guid);

    Map findAll(int type, String keyWord, int pageNum, int pageSize);

    void deleteByGuid(String userId,String guid);

    List<CmsPermissions> getAllPermissions();

    Optional<SysUserInfo> findSysByGuid(String id);

    SysUserInfo getSysByUsername(String userName);

    SysUserInfo findByPhone(String phone);

    CmsUserInfo getByNickname(String username);

    String getTocUsername();

    String getTobUsername();

    boolean updateUserCredit(JSONObject jsonObject);

    PageInfoEntity getUserListOfB(String keyWord, Integer pageNum, Integer pageSize);

    void updateUserPermissions(JSONObject jsonObject);

    List getUserPermissions(String guid);
}
