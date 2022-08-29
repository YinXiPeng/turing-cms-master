package com.bonc.turing.cms.home.service;

import com.alibaba.fastjson.JSONObject;
import com.bonc.turing.cms.home.domain.Activity;
import com.bonc.turing.cms.manage.entity.user.SysUserInfo;

public interface ManageHomeService {

    Object createAccount(SysUserInfo sysUserInfo);

    Object deleteAccount(JSONObject params);

    Object checkUserName(String username);

    Object accountList(int pageNum,int pageSize);

    Object createActivity(Activity activity);

    Object activityList(int pageNum,int pageSize,String title);

    Object deleteActivity(String activityId);

    Object changeActivityStatus(int status,String activityId);

    Object deleteHomeContent(JSONObject params);

    Object hideHomeContent(JSONObject params);

    Object likeHomeContent(JSONObject params);

    Object feedTypeList();

}
