package com.bonc.turing.cms.manage.service.login.impl;

import com.bonc.turing.cms.common.utils.JedisUtil;
import com.bonc.turing.cms.manage.controller.login.ShiroController;
import com.bonc.turing.cms.manage.entity.LoginLog;

import com.bonc.turing.cms.manage.metrics.TaggedMetricRegistryProvider;
import com.bonc.turing.cms.manage.repository.LoginLogRepository;
import com.bonc.turing.cms.manage.service.login.LoginService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codahale.metrics.Timer;
import com.github.sps.metrics.TaggedMetricRegistry;


import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

/**
 * @author lky
 */
@Service
public class LoginServiceImpl implements LoginService {
    private static final Logger logger = LoggerFactory.getLogger(ShiroController.class);

    @Autowired
    private LoginLogRepository loginLogRepository;
    @Autowired
    TaggedMetricRegistryProvider taggedMetricRegisterProvider;
    /**
     * 退出/注销登录
     *
     * @param guid
     */
    public long logout(String guid) {
        long ret = -1;
        TaggedMetricRegistry taggedMetricRegistry = taggedMetricRegisterProvider.getTaggedMetricRegistry();
        Timer.Context time=null;
        if (null!=taggedMetricRegistry){
            taggedMetricRegistry.meter("cloud.logout.qps").mark();
            time = taggedMetricRegistry.timer("cloud.logout.latency").time();
        }
        logger.info("logout:guid={}", guid);
        JedisCluster jedis = null;
        String tokenInDb = "";
        try {
            jedis = JedisUtil.getJedisCluster();
            if (jedis != null) {
                String key = JedisUtil.getKey(JedisUtil.tokenPrefix + guid);
                tokenInDb = jedis.get(key);
                ret = jedis.del(key);
            }
            if (jedis != null) {
                String key = JedisUtil.getKey(JedisUtil.ShrioPrefix + tokenInDb);
                jedis.del(key);
            }
            if (jedis != null) {
                String key = JedisUtil.getKey(tokenInDb);
                jedis.del(key);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        if (null!=taggedMetricRegistry){
            time.stop();
        }
        return ret;
    }

    @Override
    public void updateUserToken(String guid, String token) {
        JedisCluster jedis = null;
        TaggedMetricRegistry taggedMetricRegistry = taggedMetricRegisterProvider.getTaggedMetricRegistry();
        Timer.Context time=null;
        if (null!=taggedMetricRegistry){
            taggedMetricRegistry.meter("cloud.updateUserToken.qps").mark();
            time = taggedMetricRegistry.timer("cloud.updateUserToken.latency").time();
        }
        try {
            jedis = JedisUtil.getJedisCluster();
            if (jedis != null) {
                String key = JedisUtil.getKey(JedisUtil.tokenPrefix + guid);
                jedis.setex(key,86400*30, token);
                logger.info("update user:{} token:{} done!", guid, token);
            }
            if (jedis != null) {
                String key = JedisUtil.getKey(JedisUtil.ShrioPrefix + token);
                jedis.expire(key, 86400*7);
                logger.info("update user:{} shrioToken:{} done!", guid, token);
            }
            if (jedis != null) {
                String key = JedisUtil.getKey(token);
                jedis.setex(key, 86400*7, guid);
                logger.info("update user:{} guid:{} done!", guid, token);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (null!=taggedMetricRegistry){
                time.stop();
            }
        }
    }

    @Override
    public void updateUserTokenByGuid(String guid) {
        JedisCluster jedis = null;
        String token ="";
        TaggedMetricRegistry taggedMetricRegistry = taggedMetricRegisterProvider.getTaggedMetricRegistry();
        Timer.Context time=null;
        if (null!=taggedMetricRegistry){
            taggedMetricRegistry.meter("cloud.updateUserTokenByGuid.qps").mark();
            time= taggedMetricRegistry.timer("cloud.updateUserTokenByGuid.latency").time();
        }

        try {
            jedis = JedisUtil.getJedisCluster();
            if (jedis != null) {
                String key = JedisUtil.getKey(JedisUtil.tokenPrefix + guid);
                token = jedis.get(key);
                if(StringUtils.isNotBlank(token)){
                    int expire = 86400*7;
                    jedis.expire(key, expire);
                    logger.info("update user:{} token:{} expire:{} done!", guid,token,expire);
                }else {
                    jedis.expire(key, 1);
                    logger.info("update user:{} token:{} expire:{} done!", guid,token,1);
                }
            }
            if (jedis != null) {
                if(StringUtils.isNotBlank(token)){
                    String key = JedisUtil.getKey(JedisUtil.ShrioPrefix + token);
                    jedis.expire(key, 86400*7);
                    logger.info("update user:{} shrioToken:{} done!", guid, token);
                }

            }

            if (jedis != null) {
                if (StringUtils.isNotBlank(token)) {
                    String key = JedisUtil.getKey(token);
                    jedis.expire(key, 86400*7);
                    logger.info("update user:{} guid:{} done!", guid, token);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (null!=taggedMetricRegistry){
                time.stop();
            }
        }
    }

    /**
     * 根据guid判断是否为有效登录用户
     * @param guid
     * @return
     */
    @Override
    public boolean isLoginUserByGuid(String guid) {
        boolean isLogin = false;
        String tokenInDb = "";
        if (StringUtils.isNoneBlank(guid)) {
            tokenInDb = queryUserToken(guid);
            if (StringUtils.isNotBlank(tokenInDb)) {
                isLogin = true;
            }
        }
        logger.info("isLoginUser:guid={},token={},isLogin={}", guid, tokenInDb, isLogin);
        return isLogin;
    }


    /**
     * 判断是否为有效登录用户
     * @param guid
     * @param token
     * @return
     */
    public boolean isLoginUser(String guid, String token) {
        boolean isLogin = false;
        String tokenInDb = null;
        if (StringUtils.isNoneBlank(guid, token)) {
            tokenInDb = queryUserToken(guid);
            if (token.equals(tokenInDb)) {
                isLogin = true;
            }
        }
        logger.info("isLoginUser:guid={},token={},tokenInDb={},isLogin={},", guid, token, tokenInDb, isLogin);
        return isLogin;
    }


    /**
     * @param guid
     * @return
     */
    public String queryUserToken(String guid) {
        String token = "";
        if (StringUtils.isNoneBlank(guid)) {
            JedisCluster jedis = null;
            try {
                jedis = JedisUtil.getJedisCluster();
                if (jedis != null) {
                    String key = JedisUtil.getKey(JedisUtil.tokenPrefix + guid);
                    token = jedis.get(key);
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        logger.info("queryUserToken:guid={},token={}", guid, token);
        return token;
    }

    @Override
    public String getGuid(String token) {
        String guid = null;
        if (StringUtils.isNoneBlank(token)) {
            JedisCluster jedis = null;
            try {
                jedis = JedisUtil.getJedisCluster();
                if (jedis != null) {
                    guid = jedis.get(JedisUtil.getKey(token));
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
            logger.info("getGuid:guid={},token={}", guid, token);
        }
        return guid;
    }

    @Override
    public void addLoginLog(LoginLog loginLog) {
        loginLogRepository.save(loginLog);
    }
}
