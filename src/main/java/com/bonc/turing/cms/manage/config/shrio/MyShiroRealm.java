package com.bonc.turing.cms.manage.config.shrio;

import com.bonc.turing.cms.manage.entity.user.CmsUserInfo;
import com.bonc.turing.cms.manage.service.user.CmsUserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *  身份校验核心类;
 *  shiro的认证最终是交给了Realm进行执行的
 * @author liuyunkai
 * @date 2018-11-26 10:40
 */
public class MyShiroRealm extends AuthorizingRealm {

    @Autowired
    private CmsUserService cmsUserService;

    /**
     * 认证信息.(身份验证)
     * :验证用户输入的账号和密码是否正确
     * Authentication 是用来验证用户身份
     * @author liuyunkai
     * @date 2018-11-26 10:40
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("身份验证-->MyShiroRealm.doGetAuthenticationInfo()");
        MyUsernamePasswordToken myToken = (MyUsernamePasswordToken) token;
        // 1. 获取用户输入的账号
        String username = (String)myToken.getPrincipal();
        // 2. 通过username从数据库中查找，获取userInfo对象
        CmsUserInfo byUsername = cmsUserService.getByUsername(username);//这里取到以后，自动放进principals里，下面认证直接取。
        // 判断是否有userInfo
        if(byUsername == null){
            return null;
        }
        // 3. 加密， 使用SimpleAuthenticationInfo 进行身份处理
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(
                byUsername, //用户名
                byUsername.getPassword(), //密码
                ByteSource.Util.bytes(byUsername.getCredentialsSalt()),//salt=username+salt
                getName()  //realm name
        );
        return simpleAuthenticationInfo;
    }

    /**
     *权限控制
     *此方法调用  hasRole,hasPermission 的时候才会进行回调.
     *此方法中主要是使用类：SimpleAuthorizationInfo进行角色的添加和权限的添加。
     *  authorizationInfo.addRole(role.getRole());
     *  authorizationInfo.addStringPermission(p.getPermission());
     *当然也可以添加集合：
     *  authorizationInfo.setRoles(roles);
     *  authorizationInfo.setStringPermissions(stringPermissions);
     *
     * 权限信息.(授权):
     * 1、如果用户正常退出，缓存自动清空；
     * 2、如果用户非正常退出，缓存自动清空；
     * 3、如果我们修改了用户的权限，而用户不退出系统，修改的权限无法立即生效。
     * （需要手动编程进行实现；放在service进行调用）
     * 在权限修改后调用realm中的方法，realm已经由spring管理，所以从spring中获取realm实例，
     * 调用clearCached方法；
     * :Authorization 是授权访问控制，用于对用户进行的操作授权，证明该用户是否允许进行当前操作，如访问某个链接，某个资源文件等。
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        /*
         * 当没有使用缓存的时候，不断刷新页面的话，这个代码会不断执行，
         * 其实没有必要每次都重新设置权限信息，所以我们需要放到缓存中进行管理；
         * 当放到缓存中时，这样的话，doGetAuthorizationInfo就只会执行一次了，
         * 缓存过期之后会再次执行。
         */
        System.out.println("权限配置-->MyShiroRealm.doGetAuthorizationInfo()");
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        System.out.println("权限配置-->principals.getPrimaryPrincipal()："+ principals.getPrimaryPrincipal());
        CmsUserInfo primaryPrincipal = (CmsUserInfo) principals.getPrimaryPrincipal();
//        try {
//            for(SysRole role: sysUserInfo.getRoleList()){
//                authorizationInfo.addRole(role.getRole());
//                for(SysPermission p:role.getPermissions()){
//                    authorizationInfo.addStringPermission(p.getPermission());
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return authorizationInfo;
    }
}
