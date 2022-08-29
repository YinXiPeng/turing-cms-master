package com.bonc.turing.cms.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.*;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author liuyunkai
 * @date 2018-11-26 19:40
 */
@Component
public class JedisUtil {

    protected static final Logger logger = LoggerFactory.getLogger(JedisUtil.class);

    protected static final int timeout = 600000;




    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";




    /* protected static int portProd;
     protected static int portTest;*/


    protected static ReentrantLock lockPool = new ReentrantLock();

    private static JedisCluster jedisCluster = null;

    public static final int configDbIndex = 0;
    public static final String configKey = "userCenterConfig";//保存配置信息
    public static final String smsSignKey = "smsSign";
    //当前主通道使用的短信服务对应的key;主通道有两个可选项
    public static final String smsChannelKey = "smsChannel";//yunpian
    public static int smsCodeDbIndex = 1;
    public static String smsCodePrefix = "smscode:";//登录
    public static String smsCodeSecPrefix = "smscodeSec:";//登录
    public static final int tokenDbIndex = 2;
    public static final String tokenPrefix = "token:";//token信息前缀
    public static final int phoneDbIndex = 4;
    public static final String phonePrefix = "phone:";//token信息前缀
    public static final int ShrioDbIndex = 0;
    public static final String ShrioPrefix = "shiro_redis_session:";
    public static final int guidDbIndex = 3;
    public static final String GuidPrefix = "guid_redis_session:";
    public static String mailCodePrefix = "mailcode:";
    public static int mailCodeDbIndex = 4;
    public static final int headDbIndex = 5;
    public static final String headPrefix = "headMessage";

    protected static String host;
    protected static String auth = "123456";

    @Value("${spring.redis.nodes}")
    public void setHost(String host) {
        JedisUtil.host = host;
    }


    @Value("${spring.redis.password}")
    public void setAuth(String auth) {
        JedisUtil.auth = auth;
    }

    private static String projectBranch;

    @Value("${project.branch}")
    public void setProjectBranch(String projectBranch){
        this.projectBranch=projectBranch;
    }

    @PostConstruct
    public void init() {
        jedisClusterInit();
    }


    /*static {
        try {
           *//* host = LoadConfig.lookUpValueByKey("redis.host");
            auth = LoadConfig.lookUpValueByKey("redis.auth");*//*
            try {
               *//* portProd = Integer.valueOf(LoadConfig.lookUpValueByKey("redis.port-prod"));
                portTest = Integer.valueOf(LoadConfig.lookUpValueByKey("redis.port-test"));*//*
            } catch (Exception e) {
                logger.error("read port error", e);
                port = 6379;
            }

            redisPoolInit();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }*/

    /**
     * @desc 把项目的前缀给拼上
     * @auth lky
     * @date 2019/11/29 13:14
     * @param
     * @return
     */
    public static String getKey(String key){
        return projectBranch+":"+key;
    }

    /**
     * @throws
     * @Title: redisPoolInit
     * @Description: 初始化jedisPool
     * @author liu_yi
     */
    private static void jedisClusterInit() {
        // 当前锁是否已经锁住?if锁住了，do nothing; else continue
        assert !lockPool.isHeldByCurrentThread();

        // 配置发生变化的时候自动载入
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            // 是否启用后进先出, 默认true
            config.setLifo(true);
            // 最大空闲连接数
            config.setMaxIdle(10);
            // 获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted), 如果超时就抛异常,小于零:阻塞不确定的时间
            config.setMaxWaitMillis(2000);
            // 逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
            config.setMinEvictableIdleTimeMillis(1800000);
            // 最小空闲连接数, 默认0
            config.setMinIdle(0);
            // 每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3
            config.setNumTestsPerEvictionRun(3);
            // 对象空闲多久后逐出, 当空闲时间>该值 且 空闲连接>最大空闲数 时直接逐出,不再根据MinEvictableIdleTimeMillis判断  (默认逐出策略)
            config.setSoftMinEvictableIdleTimeMillis(1800000);
            // 在获取连接的时候检查有效性, 默认false
            config.setTestOnBorrow(true);
            config.setMaxTotal(50000);
            // 在空闲时检查有效性, 默认false
            config.setTestWhileIdle(false);

            logger.info("build jedisPool...");
            HashSet<HostAndPort> hostAndPorts = new HashSet<>();
            String[] split = host.split(",");
            for (String s:split){
                String[] split1 = s.split(":");
                HostAndPort hostAndPort = new HostAndPort(split1[0], Integer.valueOf(split1[1]));
                hostAndPorts.add(hostAndPort);
            }
            jedisCluster = new JedisCluster(hostAndPorts,timeout,timeout,5,auth,config);

        } catch (Exception ex) {
            logger.error("redisPoolInit failed#", ex);
        }
    }

    /**
     * @return
     */
    public static JedisCluster getJedisCluster() {
        if (jedisCluster == null) {
            jedisClusterInit();
        }
        return jedisCluster;
    }



    /**
     * 尝试获取分布式锁
     *
     * @param jedis      Redis客户端
     * @param lockKey    锁
     * @param requestId  请求标识
     * @param expireTime 超期时间
     * @return 是否获取成功
     */
    public static boolean tryGetDistributedLock(JedisCluster jedis, String lockKey, String requestId, int expireTime) {

        long startTime = System.currentTimeMillis();
        String result = jedis.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);
        long endTime = System.currentTimeMillis();
        logger.info("get user redis lock use {} ms", endTime - startTime);
        return LOCK_SUCCESS.equals(result);

    }

}
