package com.bonc.turing.cms.manage.service.login.impl;
import com.bonc.turing.cms.common.utils.CaptchaUtil;
import com.bonc.turing.cms.common.utils.JedisUtil;
import com.bonc.turing.cms.manage.constant.UUIDUtil;
import com.bonc.turing.cms.manage.service.login.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
@Service
public class CaptchaServiceImpl implements CaptchaService {
//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private UUIDUtil uuidUtil;
    @Autowired
    private CaptchaUtil captchaUtil;
    //从SpringBoot的配置文件中取出过期时间
    @Value("${server.servlet.session.timeout}")
    private Integer timeout;
    //UUID为key, 验证码为V1alue放在Redis中
    @Override
    public Map<String, Object> createToken(String captcha) {
        //生成一个token
        String key = uuidUtil.getUUID32();
        System.out.println(key+"key");
        //生成验证码对应的token  以token为key  验证码为value存在redis中
        JedisUtil.getJedisCluster().set(key,captcha);
        JedisUtil.getJedisCluster().expire(key,timeout);

        //stringRedisTemplate.opsForValue().set(key,captcha,timeout,TimeUnit.MINUTES);
        Map<String, Object> map = new HashMap<>();
        map.put("token", key);
        map.put("expire", timeout);
        return map;
    }
    //生成captcha验证码
    @Override
    public Map<String, Object> captchaCreator() throws IOException {
        return captchaUtil.catchaImgCreator();
    }
    //验证输入的验证码是否正确
    @Override
    public String versifyCaptcha(String token, String inputCode) {
        //根据前端传回的token在redis中找对应的value

        if (JedisUtil.getJedisCluster().get(token)!=null) {
            //验证通过, 删除对应的key
            if (JedisUtil.getJedisCluster().get(token).equals(inputCode)) {
                JedisUtil.getJedisCluster().del(token);
                return "true";
            } else {
                return "false";
            }
        } else {
            return "false";
        }
    }
}