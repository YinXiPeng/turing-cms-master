package com.bonc.turing.cms.manage.controller.login;


import com.alibaba.fastjson.JSONObject;
import com.bonc.turing.cms.common.utils.HttpUtil;
import com.bonc.turing.cms.exercise.dao.repository.CosSchoolRepository;
import com.bonc.turing.cms.exercise.dao.repository.CosUserMsgRepository;
import com.bonc.turing.cms.exercise.domain.CosSchool;
import com.bonc.turing.cms.exercise.domain.CosUserMsg;
import com.bonc.turing.cms.manage.config.shrio.MyUsernamePasswordToken;
import com.bonc.turing.cms.manage.constant.SystemConstants;
import com.bonc.turing.cms.manage.controller.BaseController;
import com.bonc.turing.cms.manage.dto.CmsUserInfoDTO;
import com.bonc.turing.cms.manage.entity.LoginLog;
import com.bonc.turing.cms.manage.entity.user.CmsUserInfo;
import com.bonc.turing.cms.manage.metrics.TaggedMetricRegistryProvider;
import com.bonc.turing.cms.manage.service.login.CaptchaService;
import com.bonc.turing.cms.manage.service.login.LoginService;
import com.bonc.turing.cms.manage.service.user.CmsUserService;
import com.codahale.metrics.Timer;
import com.github.sps.metrics.TaggedMetricRegistry;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;


/**
 * @desc 登陆,验证登陆部分
 * @author lky
 * @date 2019-7-12 20:22
 */
@RestController
@CrossOrigin(origins = "*")
public class ShiroController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(ShiroController.class);

    @Autowired
    private CmsUserService cmsUserService;
    @Autowired
    private LoginService loginService;
    @Autowired
    private CosSchoolRepository cosSchoolRepository;
    @Autowired
    private CosUserMsgRepository cosUserMsgRepository;
    @Autowired
    TaggedMetricRegistryProvider taggedMetricRegisterProvider;
    @Value("${tllb.url}")
    private String tllbUrl;

    @Autowired
    CaptchaService captchaService;

    @GetMapping("/captcha")
    public Map<String, Object> captcha() throws IOException {
        return captchaService.captchaCreator();
    }
        /*
    高级版本
    @RequestMapping(value = "/test3",method=RequestMethod.POST)
    public void test3(@RequestBody Map<String, String> map) throws Exception{
        System.out.println("username:"+map.get("username"));
    }
         */
    @RequestMapping("/login1/{token}/{inputCode}")
    @ResponseBody
    public String login(@PathVariable("token") String token,
                        @PathVariable("inputCode") String inputCode) {
        System.out.println(token+":"+inputCode);
        return captchaService.versifyCaptcha(token, inputCode);
    }

    /**
     * 登录方法
     *
     * @param cmsUserInfo
     * @return
     * @author lky
     * @date
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public JSONObject ajaxLogin(@RequestBody CmsUserInfo cmsUserInfo) {
        JSONObject jsonObject = new JSONObject();
        Subject subject = SecurityUtils.getSubject();
        String sessionId = (String) subject.getSession().getId();
        JSONObject object = new JSONObject();
        TaggedMetricRegistry taggedMetricRegistry = taggedMetricRegisterProvider.getTaggedMetricRegistry();
        Timer.Context time = null;
        if (null != taggedMetricRegistry) {
            taggedMetricRegistry.meter("cloud.Login.qps").mark();
            time = taggedMetricRegistry.timer("cloud.Login.latency").time();
        }
        try {
            MyUsernamePasswordToken token = new MyUsernamePasswordToken(cmsUserInfo.getUserName(), cmsUserInfo.getPassword());
            subject.login(token);
            this.getSession().setAttribute("username", token.getUsername());
            //每次登录更新token
            //获取用户信息
            CmsUserInfo byUsername = cmsUserService.getByUsername(cmsUserInfo.getUserName());
            String guid = byUsername.getGuid();
            byUsername.setToken(sessionId);

            Date createdTime = byUsername.getCreatedTime();
            Date lastModifyTime = byUsername.getLastModifyTime();
            CmsUserInfoDTO cmsUserInfoDTO = new CmsUserInfoDTO();
            if (null!=createdTime){
                cmsUserInfoDTO.setCreatedTime(createdTime.getTime());
            }
            if (null!=lastModifyTime){
                cmsUserInfoDTO.setLastModifyTime(createdTime.getTime());
            }
            cmsUserInfoDTO.setGuid(byUsername.getGuid());
            cmsUserInfoDTO.setCreatedId(byUsername.getCreatedId());
            cmsUserInfoDTO.setDeviceid(byUsername.getDeviceid());
            cmsUserInfoDTO.setEmail(byUsername.getEmail());
            cmsUserInfoDTO.setHeadimgurl(byUsername.getHeadimgurl());
            cmsUserInfoDTO.setIsDeleted(byUsername.getIsDeleted());
            cmsUserInfoDTO.setLastModifyId(byUsername.getLastModifyId());
            cmsUserInfoDTO.setPassword(null);
            cmsUserInfoDTO.setPermissions(byUsername.getPermissions());
            cmsUserInfoDTO.setPhone(byUsername.getPhone());
            cmsUserInfoDTO.setRealName(byUsername.getRealName());
            cmsUserInfoDTO.setRole(byUsername.getRole());
            cmsUserInfoDTO.setSalt(null);
            cmsUserInfoDTO.setState(byUsername.getState());
            cmsUserInfoDTO.setToken(byUsername.getToken());
            cmsUserInfoDTO.setUserName(byUsername.getUserName());
            Optional<CosUserMsg> byGuid = cosUserMsgRepository.findByGuid(guid);
            if (byGuid.isPresent()){
                CosUserMsg cosUserMsg = byGuid.get();
                Optional<CosSchool> byId = cosSchoolRepository.findById(cosUserMsg.getSchoolId());
                if (byId.isPresent()){
                    CosSchool cosSchool = byId.get();
                    cmsUserInfoDTO.setCosSchool(cosSchool);
                }
            }

            loginService.updateUserToken(guid, sessionId);
            jsonObject.put("code", SystemConstants.CODE_SUCCESS);
            jsonObject.put("msg", "登录成功");
            jsonObject.put("data", cmsUserInfoDTO);

            //每次登录记录log
            LoginLog loginLog = new LoginLog();
            loginLog.setGuid(guid);
            loginLog.setLogints(System.currentTimeMillis());
            loginLog.setPhone(object.getString("phone"));
            loginLog.setUsername(token.getUsername());
            loginService.addLoginLog(loginLog);
            logger.info("login==>guid={},userName={},loginTs={}", guid, token.getUsername(), System.currentTimeMillis());
        } catch (IncorrectCredentialsException e) {
            jsonObject.put("code", SystemConstants.CODE_FAILURE);
            jsonObject.put("msg", "密码错误");
            object.put("type", "password");
            jsonObject.put("data", object);
            logger.info(jsonObject.toString());
        } catch (AuthenticationException e) {
            jsonObject.put("code", SystemConstants.CODE_FAILURE);
            jsonObject.put("msg", "该用户不存在");
            object.put("type", "username");
            jsonObject.put("data", object);
            logger.info(jsonObject.toString());
        } catch (Exception e) {
            jsonObject.put("code", SystemConstants.CODE_FAILURE);
            jsonObject.put("msg", e.getMessage());
            object.put("type", "other");
            jsonObject.put("data", object);
            logger.info("", e);
        }
        if (null != taggedMetricRegistry) {
            time.stop();
        }
        logger.info(jsonObject.toString());
        return jsonObject;
    }

    /**
     * @desc 验证guid 是否登录
     * @auth lky
     * @date 2019/7/31 10:26
     * @param
     * @return
     */
    @RequestMapping(value = "/validateGuid", method = {RequestMethod.GET, RequestMethod.POST})
    public JSONObject validateGuid(String guid) {
        logger.info("validateGuid guid:{}", guid);
        JSONObject json = new JSONObject();
        JSONObject data = new JSONObject();
        int code = SystemConstants.CODE_SUCCESS;
        String msg = SystemConstants.MSG_200;
        boolean valid = false;
        if (!StringUtils.isEmpty(guid)) {
            Optional<CmsUserInfo> byGuid = cmsUserService.getByGuid(guid);
            if (!byGuid.isPresent()) {
                code = SystemConstants.CODE_500;
                msg = SystemConstants.MSG_500;
            } else {
                valid = loginService.isLoginUserByGuid(guid);
                logger.info("check guid({}) valid info result:{}", guid, valid);
                if (!valid) {
                    code = SystemConstants.CODE_408;
                    msg = SystemConstants.MSG_408;
                } else {
                    //重置token失效时间
                    loginService.updateUserTokenByGuid(guid);
                }
            }

        } else {
            code = SystemConstants.CODE_407;
            msg = "guid不能为空";
        }
        json.put("code", code);
        json.put("msg", msg);
        data.put("valid", valid);
        json.put("data", data);
        logger.info("RETURN:{}", json.toString());
        return json;
    }

    /**
     * @desc 验证guid,token是否有效
     * @auth lky
     * @date 2019/7/31 10:26
     * @param
     * @return
     */
    @RequestMapping(value = "/validateGuidAndToken", method = {RequestMethod.GET, RequestMethod.POST})
    public JSONObject validateGuidAndToken(String guid, String token) {
        logger.info("validateGuidAndToken guid:{},token:{}", guid, token);
        JSONObject json = new JSONObject();
        JSONObject data = new JSONObject();
        int code = SystemConstants.CODE_SUCCESS;
        String msg = SystemConstants.MSG_200;
        boolean valid = false;
        String username = "";
        if (StringUtils.isNoneBlank(guid, token)) {
            Optional<CmsUserInfo> byGuid = cmsUserService.getByGuid(guid);
            if (!byGuid.isPresent()) {
                code = SystemConstants.CODE_500;
                msg = SystemConstants.MSG_500;
            } else {
                CmsUserInfo cmsUserInfo = byGuid.get();
                username = cmsUserInfo.getUserName();
                valid = loginService.isLoginUser(guid, token);
                logger.info("check guid({}) token({}) valid info result:{}", guid, token, valid);
                if (!valid) {
                    code = SystemConstants.CODE_408;
                    msg = SystemConstants.MSG_408;
                } else {
                    //重置token失效时间
                    loginService.updateUserToken(guid, token);
                }
            }
        } else {
            code = SystemConstants.CODE_407;
            msg = "guid,token不能为空";
        }
        json.put("code", code);
        json.put("msg", msg);
        data.put("valid", valid);
        data.put("username", username);
        json.put("data", data);
        logger.info("RETURN:{}", json.toString());
        return json;
    }


    /**
     * 获取用户信息
     *
     * @param
     * @return
     * @author lky
     * @date
     */
    @RequestMapping(value = "/getUserMsg", method = RequestMethod.GET)
    public JSONObject getUserMsg(@RequestParam("guid")String guid,@RequestParam("token")String token) {
        JSONObject jsonObject = new JSONObject();
        try {
            Optional<CmsUserInfo> byGuid = cmsUserService.findByGuid(guid);
            CmsUserInfo cmsUserInfo = byGuid.get();
            cmsUserInfo.setToken(token);

            Date createdTime = cmsUserInfo.getCreatedTime();
            Date lastModifyTime = cmsUserInfo.getLastModifyTime();
            CmsUserInfoDTO cmsUserInfoDTO = new CmsUserInfoDTO();
            if (null!=createdTime){
                cmsUserInfoDTO.setCreatedTime(createdTime.getTime());
            }
            if (null!=lastModifyTime){
                cmsUserInfoDTO.setLastModifyTime(createdTime.getTime());
            }
            cmsUserInfoDTO.setGuid(cmsUserInfo.getGuid());
            cmsUserInfoDTO.setCreatedId(cmsUserInfo.getCreatedId());
            cmsUserInfoDTO.setDeviceid(cmsUserInfo.getDeviceid());
            cmsUserInfoDTO.setEmail(cmsUserInfo.getEmail());
            cmsUserInfoDTO.setHeadimgurl(cmsUserInfo.getHeadimgurl());
            cmsUserInfoDTO.setIsDeleted(cmsUserInfo.getIsDeleted());
            cmsUserInfoDTO.setLastModifyId(cmsUserInfo.getLastModifyId());
            cmsUserInfoDTO.setPassword(null);
            cmsUserInfoDTO.setPermissions(cmsUserInfo.getPermissions());
            cmsUserInfoDTO.setPhone(cmsUserInfo.getPhone());
            cmsUserInfoDTO.setRealName(cmsUserInfo.getRealName());
            cmsUserInfoDTO.setRole(cmsUserInfo.getRole());
            cmsUserInfoDTO.setSalt(null);
            cmsUserInfoDTO.setState(cmsUserInfo.getState());
            cmsUserInfoDTO.setToken(cmsUserInfo.getToken());
            cmsUserInfoDTO.setUserName(cmsUserInfo.getUserName());
            Optional<CosUserMsg> byGuid1 = cosUserMsgRepository.findByGuid(guid);
            if (byGuid1.isPresent()){
                CosUserMsg cosUserMsg = byGuid1.get();
                Optional<CosSchool> byId = cosSchoolRepository.findById(cosUserMsg.getSchoolId());
                if (byId.isPresent()){
                    CosSchool cosSchool = byId.get();
                    cmsUserInfoDTO.setCosSchool(cosSchool);
                }
            }
            jsonObject.put("code", SystemConstants.CODE_SUCCESS);
            jsonObject.put("msg", "查询成功");
            jsonObject.put("data", cmsUserInfoDTO);
        }  catch (Exception e) {
            jsonObject.put("code", SystemConstants.CODE_FAILURE);
            jsonObject.put("msg", "查询失败");
            jsonObject.put("data", "");
            logger.error("getUserMsg is failed,guid:{}",guid, e);
        }
        logger.info(jsonObject.toString());
        return jsonObject;
    }


    /**
     * @Author lky
     * @Description 从前台跳转后台
     * @Date 11:17 2020/7/24
     * @Param
     * @return
     **/
    @RequestMapping("loginToCms")
    public Object loginToCms(@RequestParam("guid")String tllbguid,@RequestParam("token")String tllbtoken){
        String judgeGuidAndToken = tllbUrl+"validateGuidAndToken"+"?guid="+tllbguid+"&token="+tllbtoken;
        JSONObject jsonObject = new JSONObject();
        JSONObject object = new JSONObject();
        JSONObject response = null;
        try {
            response = HttpUtil.sendGetRequest(judgeGuidAndToken);
        } catch (Exception e) {
            logger.error("loginToCms is failed",e);
            jsonObject.put("code", SystemConstants.CODE_FAILURE);
            jsonObject.put("msg", "用户验证失败,请重新登陆");
            jsonObject.put("data", object);
            return jsonObject;
        }
        if (null!=response && 1000-response.getInteger("code")==0){
            //验证通过,走登陆流程
            Subject subject = SecurityUtils.getSubject();
            String sessionId = (String) subject.getSession().getId();
            try {
                Optional<CmsUserInfo> byGuid1 = cmsUserService.findByGuid(tllbguid);
                if (!byGuid1.isPresent()){
                    jsonObject.put("code", SystemConstants.CODE_FAILURE);
                    jsonObject.put("msg", "用户不存在");
                    jsonObject.put("data", object);
                }
                CmsUserInfo cmsUserInfo = byGuid1.get();
                MyUsernamePasswordToken token = new MyUsernamePasswordToken(cmsUserInfo.getUserName());
                subject.login(token);
                this.getSession().setAttribute("username", token.getUsername());
                //每次登录更新token
                //获取用户信息
                CmsUserInfo byUsername = cmsUserService.getByUsername(cmsUserInfo.getUserName());
                String guid = byUsername.getGuid();
                byUsername.setToken(sessionId);

                Date createdTime = byUsername.getCreatedTime();
                Date lastModifyTime = byUsername.getLastModifyTime();
                CmsUserInfoDTO cmsUserInfoDTO = new CmsUserInfoDTO();
                if (null!=createdTime){
                    cmsUserInfoDTO.setCreatedTime(createdTime.getTime());
                }
                if (null!=lastModifyTime){
                    cmsUserInfoDTO.setLastModifyTime(createdTime.getTime());
                }
                cmsUserInfoDTO.setGuid(byUsername.getGuid());
                cmsUserInfoDTO.setCreatedId(byUsername.getCreatedId());
                cmsUserInfoDTO.setDeviceid(byUsername.getDeviceid());
                cmsUserInfoDTO.setEmail(byUsername.getEmail());
                cmsUserInfoDTO.setHeadimgurl(byUsername.getHeadimgurl());
                cmsUserInfoDTO.setIsDeleted(byUsername.getIsDeleted());
                cmsUserInfoDTO.setLastModifyId(byUsername.getLastModifyId());
                cmsUserInfoDTO.setPassword(null);
                cmsUserInfoDTO.setPermissions(byUsername.getPermissions());
                cmsUserInfoDTO.setPhone(byUsername.getPhone());
                cmsUserInfoDTO.setRealName(byUsername.getRealName());
                cmsUserInfoDTO.setRole(byUsername.getRole());
                cmsUserInfoDTO.setSalt(null);
                cmsUserInfoDTO.setState(byUsername.getState());
                cmsUserInfoDTO.setToken(byUsername.getToken());
                cmsUserInfoDTO.setUserName(byUsername.getUserName());
                Optional<CosUserMsg> byGuid = cosUserMsgRepository.findByGuid(guid);
                if (byGuid.isPresent()){
                    CosUserMsg cosUserMsg = byGuid.get();
                    Optional<CosSchool> byId = cosSchoolRepository.findById(cosUserMsg.getSchoolId());
                    if (byId.isPresent()){
                        CosSchool cosSchool = byId.get();
                        cmsUserInfoDTO.setCosSchool(cosSchool);
                    }
                }

                loginService.updateUserToken(guid, sessionId);
                jsonObject.put("code", SystemConstants.CODE_SUCCESS);
                jsonObject.put("msg", "登录成功");
                jsonObject.put("data", cmsUserInfoDTO);

                //每次登录记录log
                LoginLog loginLog = new LoginLog();
                loginLog.setGuid(guid);
                loginLog.setLogints(System.currentTimeMillis());
                loginLog.setPhone(object.getString("phone"));
                loginLog.setUsername(token.getUsername());
                loginService.addLoginLog(loginLog);
                logger.info("login==>guid={},userName={},loginTs={}", guid, token.getUsername(), System.currentTimeMillis());
            } catch (IncorrectCredentialsException e) {
                jsonObject.put("code", SystemConstants.CODE_FAILURE);
                jsonObject.put("msg", "密码错误");
                object.put("type", "password");
                jsonObject.put("data", object);
                logger.info(jsonObject.toString());
            } catch (AuthenticationException e) {
                jsonObject.put("code", SystemConstants.CODE_FAILURE);
                jsonObject.put("msg", "该用户不存在");
                object.put("type", "username");
                jsonObject.put("data", object);
                logger.info(jsonObject.toString());
            } catch (Exception e) {
                jsonObject.put("code", SystemConstants.CODE_FAILURE);
                jsonObject.put("msg", e.getMessage());
                object.put("type", "other");
                jsonObject.put("data", object);
                logger.info("", e);
            }
            logger.info(jsonObject.toString());
        }else {
            //用户验证失败,请重新登陆
            jsonObject.put("code", SystemConstants.CODE_FAILURE);
            jsonObject.put("msg", "用户验证失败,请重新登陆");
            jsonObject.put("data", object);
        }
        return jsonObject;
    }

}

