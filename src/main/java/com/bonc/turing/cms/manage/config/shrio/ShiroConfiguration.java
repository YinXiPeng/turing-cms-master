package com.bonc.turing.cms.manage.config.shrio;


import com.bonc.turing.cms.manage.constant.Redis;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisClusterManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shiro 配置
 * 注入Shiro Factory和SecurityManager
 * @author liuyunkai
 * @date 2018-11-6 10:40
 */
@Configuration
public class ShiroConfiguration {
    protected static final int timeout = 600000;
    //protected static String host = LoadConfig.lookUpValueByKey("redis.host");
    //protected static int port = Integer.valueOf(LoadConfig.lookUpValueByKey("redis.port"));
    //protected static String auth = LoadConfig.lookUpValueByKey("redis.auth");

    @Autowired
    Redis redis;
    //身份认证部分
    /**
     * ShiroFilterFactoryBean 处理拦截资源文件问题。
     * 注意：单独一个ShiroFilterFactoryBean配置是或报错的，以为在
     * 初始化ShiroFilterFactoryBean的时候需要注入：SecurityManager
     *
     * Filter Chain定义说明
     * 1、一个URL可以配置多个Filter，使用逗号分隔
     * 2、当设置多个过滤器时，全部验证通过，才视为通过
     * 3、部分过滤器可指定参数，如perms，roles
     */
    @Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager){
        //System.out.println("ShiroConfiguration.shirFilter()");
        ShiroFilterFactoryBean shiroFilterFactoryBean  = new ShiroFilterFactoryBean();

        // 必须设置 SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);
//        Map<String, Filter> mapFilter = new LinkedHashMap<>();
//        mapFilter.put("authFilter",authFilter());
//        shiroFilterFactoryBean.setFilters(mapFilter);
        //拦截器.
        Map<String,String> filterChainDefinitionMap = new LinkedHashMap<>();

        //注意过滤器配置顺序 不能颠倒
        //配置退出 过滤器,其中的具体的退出代码Shiro已经替我们实现了，登出后跳转配置的loginUrl
        //filterChainDefinitionMap.put("/logout", "logout");
        //配置认证通过可以访问的地址
        // 配置不会被拦截的链接 顺序判断
        filterChainDefinitionMap.put("/login", "anon");
//        //用户相关
//        filterChainDefinitionMap.put("/plan/**", "perms[user:plan]");
//        filterChainDefinitionMap.put("/avatar/**","perms[user:avatar]");
//        filterChainDefinitionMap.put("/testing/**","perms[user:testing ]");
        //TODO
        filterChainDefinitionMap.put("/**", "anon");


        //配置shiro默认登录界面地址，前后端分离中登录界面跳转应由前端路由控制，后台仅返回json数据
        shiroFilterFactoryBean.setLoginUrl("/unauth");
        // 登录成功后要跳转的链接
        //shiroFilterFactoryBean.setSuccessUrl("/index");
        //未授权界面;
        //shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }
    @Bean
    public SecurityManager securityManager(){
        DefaultWebSecurityManager securityManager =  new DefaultWebSecurityManager();
        //设置realm，将myShiroRealm注入到securityManager中
        securityManager.setRealm(myShiroRealm());
        // 自定义session管理 使用redis
        securityManager.setSessionManager(sessionManager());
        // 自定义缓存实现 使用redis
        securityManager.setCacheManager(cacheManager());

        //注入缓存管理器;
        //securityManager.setCacheManager(ehCacheManager());//这个如果执行多次，也是同样的一个对象;
        //注入记住我管理器;
        securityManager.setRememberMeManager(rememberMeManager());
        return securityManager;
    }
    /**
     * 身份认证realm;
     * 将自定义的Realm注入到SecurityManager中
     * (这个需要自己写，账号密码校验；权限等)
     * @return
     */
    @Bean
    public MyShiroRealm myShiroRealm(){
        MyShiroRealm myShiroRealm = new MyShiroRealm();
        myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());

        return myShiroRealm;
    }
    //自定义sessionManager
    @Bean
    public SessionManager sessionManager() {
        MySessionManager mySessionManager = new MySessionManager();
        mySessionManager.setSessionDAO(redisSessionDAO());
        return mySessionManager;
    }
    /**
     * RedisSessionDAO shiro sessionDao层的实现 通过redis
     * 使用的是shiro-redis开源插件
     */
    @Bean
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setKeyPrefix("cms_shiro_redis_session:");
        redisSessionDAO.setRedisManager(redisManager());
        return redisSessionDAO;
    }
    /**
     * 配置shiro redisManager
     * 使用的是shiro-redis开源插件
     * @return
     */
    public RedisClusterManager redisManager() {
        RedisClusterManager redisManager = new RedisClusterManager();
        redisManager.setHost(redis.getHost());
        redisManager.setTimeout(timeout);
        redisManager.setPassword(redis.getAuth());
        return redisManager;
    }

    /**
     * 凭证匹配器
     * 加密算法
     * （密码校验交给Shiro的SimpleAuthenticationInfo进行处理了
     *  所以需要修改下doGetAuthenticationInfo中的代码;
     * ）
     * @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        MyHashedCredentialsMatcher hashedCredentialsMatcher = new MyHashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");//散列算法:这里使用MD5算法;
        hashedCredentialsMatcher.setHashIterations(2);//散列的次数，比如散列两次，相当于 md5(md5(""));
        return hashedCredentialsMatcher;
    }

    //权限控制部分
    /**
     *  开启shiro aop注解支持.
     *  使用代理方式;所以需要开启代码支持;
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    //缓存管理部分
    /**
     * shiro缓存管理器;
     * 需要注入对应的其它的实体类中：
     * 1、安全管理器：securityManager
     * securityManager是整个shiro的核心；
     * @return
     */
    @Bean
    public EhCacheManager ehCacheManager(){
        //System.out.println("ShiroConfiguration.getEhCacheManager()");
        EhCacheManager cacheManager = new EhCacheManager();
        cacheManager.setCacheManagerConfigFile("classpath:config/ehcache-shiro.xml");
        return cacheManager;
    }
    /**
     * cacheManager 缓存 redis实现
     * 使用的是shiro-redis开源插件
     * @return
     */
    @Bean
    public RedisCacheManager cacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        return redisCacheManager;
    }

    //记住密码部分
    /**
     * cookie对象;
     * @return
     */
    @Bean
    public SimpleCookie rememberMeCookie(){
        //System.out.println("ShiroConfiguration.rememberMeCookie()");
        //这个参数是cookie的名称，对应前端的checkbox 的name = rememberMe
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        //<!-- 记住我cookie生效时间30天 ,单位秒;-->
        simpleCookie.setMaxAge(259200);
        return simpleCookie;
    }
    /**
     * cookie管理对象;
     * @return
     */
    @Bean
    public CookieRememberMeManager rememberMeManager(){
        //System.out.println("ShiroConfiguration.rememberMeManager()");
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        return cookieRememberMeManager;
    }

    /**
     * 注册全局异常处理
     * @return
     */
    @Bean(name = "exceptionHandler")
    public HandlerExceptionResolver handlerExceptionResolver() {
        return new MyExceptionHandler();
    }

//    @Bean
//    public AuthFilter authFilter() {
//        return new AuthFilter();
//    }
}
