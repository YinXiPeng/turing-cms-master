package com.bonc.turing.cms.manage.service.login;


import com.bonc.turing.cms.manage.entity.LoginLog;

public interface LoginService {
    /**
     * 退出/注销登录
     */
    long logout(String guid);

    /**
     * 重置 token 失效时间
     * @param guid
     * @param token
     */
    void updateUserToken(String guid, String token);

    /**
     * 通过 guid 重置 token 失效时间
     * @param guid
     */
    void updateUserTokenByGuid(String guid);

    /**
     * 判断是否为有效的登录用户
     * @param guid
     * @param token
     * @return
     */
    boolean isLoginUser(String guid, String token);


    /**
     * 判断是否为有效的登录用户
     * @param guid
     * @return
     */
    boolean isLoginUserByGuid(String guid);
    
    /**
     * 根据token得到guid;
     * 
     * @param token
     * @return
     */
    String getGuid(String token);

    /**新增用户登录记录 */
    void addLoginLog(LoginLog loginLog);


}
