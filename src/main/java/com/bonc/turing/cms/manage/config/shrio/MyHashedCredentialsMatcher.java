package com.bonc.turing.cms.manage.config.shrio;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;

public class MyHashedCredentialsMatcher extends HashedCredentialsMatcher {
    //重写验证方法，实现无秘登录
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        MyUsernamePasswordToken mupt = (MyUsernamePasswordToken)token;
        if (mupt.getLoginType().equals(LoginType.NOPASSWD.getCode())) {
            return true;
        }
        if (mupt.getLoginType().equals(LoginType.PASSWORD.getCode())) {
            return super.doCredentialsMatch(token, info);
        } else {
            return false;
        }
    }
}
