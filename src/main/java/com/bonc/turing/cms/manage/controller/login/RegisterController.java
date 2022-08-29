package com.bonc.turing.cms.manage.controller.login;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bonc.turing.cms.common.utils.Encryptor;
import com.bonc.turing.cms.common.utils.HttpUtil;
import com.bonc.turing.cms.exercise.dao.repository.CosSchoolRepository;
import com.bonc.turing.cms.exercise.dao.repository.CosUserMsgRepository;
import com.bonc.turing.cms.exercise.domain.CosSchool;
import com.bonc.turing.cms.exercise.domain.CosUserMsg;
import com.bonc.turing.cms.manage.constant.ResponseResult;
import com.bonc.turing.cms.manage.constant.SystemConstants;
import com.bonc.turing.cms.manage.controller.BaseController;
import com.bonc.turing.cms.manage.entity.user.CmsPermissions;
import com.bonc.turing.cms.manage.entity.user.CmsUserInfo;
import com.bonc.turing.cms.manage.entity.user.SysUserInfo;
import com.bonc.turing.cms.manage.service.login.LoginService;
import com.bonc.turing.cms.manage.service.user.CmsUserService;
import com.bonc.turing.cms.manage.service.user.UserInfoService;
import com.bonc.turing.cms.manage.service.user.UserVisitService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @desc 注册,注销,修改信息
 * @author lky
 * @date 2019-1-2 19:40
 */
@CrossOrigin(origins = "*")
@RestController
public class RegisterController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(RegisterController.class);

    //随机数生成器
    private static RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
    //指定散列算法为md5
    private String algorithmName = "MD5";
    //散列迭代次数
    private final int hashIterations = 2;
    @Autowired
    private CmsUserService cmsUserService;
    @Autowired
    private LoginService loginService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserVisitService userVisitService;
    @Autowired
    private CosSchoolRepository cosSchoolRepository;
    @Autowired
    private CosUserMsgRepository cosUserMsgRepository;
    @Value(value = "${head.image.url}")
    private String headImageUrl;
    @Value("${tob.pre}")
    private String tobNamePre;
    @Value("${datasience.register.url}")
    private String datasienceRegisterUrl;
    @Value("${datasience.purchase}")
    private String datasiencePurchase;


    /**
     * @desc 注册
     * @auth lky
     * @date 2019/7/31 10:22
     * @param
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public JSONObject register(@RequestBody CmsUserInfo cmsUserInfo,@RequestParam(value = "guid")String guid) {
        JSONObject result = ResponseResult.getResult();
        SysUserInfo sysUserInfo = new SysUserInfo();
        try {
            //后台的用户名是前台的昵称,昵称不能重复
            SysUserInfo sysByUsername = userInfoService.getByNickName(cmsUserInfo.getUserName());
            if (null!=sysByUsername) {
                ResponseResult.userNameErrorResult(result);
                return result;
            }
            CmsUserInfo cmsUserInfo1 = cmsUserService.getByUsername(cmsUserInfo.getUserName());
            if (null!=cmsUserInfo1) {
                ResponseResult.userNameErrorResult(result);
                return result;
            }
            if (null==cmsUserInfo.getPassword()||"".equals(cmsUserInfo.getPassword())){
                ResponseResult.passWordIsNull(result);
                return result;
            }

            if (null==cmsUserInfo.getPhone()||"".equals(cmsUserInfo.getPhone().trim())){
                ResponseResult.phoneIsNull(result);
                return result;
            }
            SysUserInfo byPhone = cmsUserService.findByPhone(cmsUserInfo.getPhone());
            if (null!=byPhone){
                ResponseResult.phoneErrorResult(result);
                return result;
            }
            { //正常注册的用户
                //生成salt
                //设置昵称
                String username = cmsUserService.getTocUsername();
                //前台 c_开头的用户名,昵称是传过来的名字
                sysUserInfo.setNickname(cmsUserInfo.getUserName());
                sysUserInfo.setUsername(username);
                cmsUserInfo.setSalt(randomNumberGenerator.nextBytes().toHex());
                sysUserInfo.setSalt(cmsUserInfo.getSalt());
                //生成加密密码
                String cmsNewPassword =
                        new SimpleHash(algorithmName, cmsUserInfo.getPassword(),
                                ByteSource.Util.bytes(cmsUserInfo.getCredentialsSalt()), hashIterations).toHex();
                String userNewPassword =
                        new SimpleHash(algorithmName, cmsUserInfo.getPassword(),
                                ByteSource.Util.bytes(sysUserInfo.getCredentialsSalt()), hashIterations).toHex();
                cmsUserInfo.setPassword(cmsNewPassword);
                sysUserInfo.setPassword(userNewPassword);
                //生成guid
                UUID uuid = UUID.randomUUID();
                String s = uuid.toString();
                String replace = s.replace("-", "");
                cmsUserInfo.setGuid(replace);
                sysUserInfo.setGuid(replace);
                cmsUserInfo.setCreatedTime(new Date());
                cmsUserInfo.setIsDeleted(0);
                cmsUserInfo.setLastModifyTime(new Date());
                sysUserInfo.setLastModify(System.currentTimeMillis());
                cmsUserInfo.setLastModifyId(guid);
                cmsUserInfo.setCreatedId(guid);
                cmsUserInfo.setState(0);
                cmsUserInfo.setRole(1);
                sysUserInfo.setIsDeleted(0);
                if (null==cmsUserInfo.getHeadimgurl()||"".equals(cmsUserInfo.getHeadimgurl().trim())){
                    cmsUserInfo.setHeadimgurl(headImageUrl);
                    sysUserInfo.setHeadimgurl(headImageUrl);
                }else {
                    sysUserInfo.setHeadimgurl(cmsUserInfo.getHeadimgurl());
                }
                sysUserInfo.setManifesto("一句话介绍我自己");
                sysUserInfo.setPhone(cmsUserInfo.getPhone());
                sysUserInfo.setRegistts(System.currentTimeMillis());
                sysUserInfo.setIsAdmin(true);
                sysUserInfo.setUserLevel("1");
                sysUserInfo.setIsStu(true);
                sysUserInfo.setIsEnterprise("0");
                sysUserInfo.setNoviceGuidanceSteps("1");
                sysUserInfo.setSource(3);
                logger.info("register==>userName={},phone={}", cmsUserInfo.getUserName(), cmsUserInfo.getPhone());

                cmsUserService.addUserInfo(cmsUserInfo,sysUserInfo);
                //用户受欢迎度 初始化
                try {
                    userVisitService.findByGuid(sysUserInfo.getGuid());
                } catch (Exception e) {
                    logger.error("User popularity initialization failed", e);
                }
                result.put("code", SystemConstants.CODE_SUCCESS);
                result.put("msg", "注册成功");
            }
        } catch (Exception e) {
            result.put("code", SystemConstants.CODE_FAILURE);
            result.put("msg", "用户注册失败：" + e.getMessage());
            logger.error(result.toString(),e);
        }
        logger.info(result.toString());

        return result;
    }

    /**
     * @desc 登出
     * @auth lky
     * @date 2019/7/31 10:24
     * @param
     * @return
     */
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject logout(@RequestBody Map<String, Object> map) {
        String guid = (String) map.get("guid");
        String token = (String) map.get("token");
        logger.info("logout==>guid={},token={}", guid, token);
        JSONObject retJson = new JSONObject();
        if (StringUtils.isNoneBlank(guid, token)) {
            //shiro退出    SecurityUtils.getSubject().getPrincipal() 获取当前登录用户信息
            Subject subject = SecurityUtils.getSubject();
            subject.logout();
            long result = loginService.logout(guid);
            if (result > 0) {
                retJson.put("code", SystemConstants.CODE_SUCCESS);
                retJson.put("msg", SystemConstants.MSG_200);
                retJson.put("data", new JSONObject());
            } else {
                retJson.put("code", SystemConstants.CODE_FAILURE);
                retJson.put("msg", "guid与token不匹配！");
                retJson.put("data", new JSONObject());
            }
        } else {
            retJson.put("key", SystemConstants.CODE_FAILURE);
            retJson.put("msg", "guid,token不能为空！");
            retJson.put("data", new JSONObject());
        }
        return retJson;
    }


    /**
     * @desc 修改管理员信息(只更新后台的用户名,前台的昵称不会更新)
     * @auth lky
     * @date 2019/7/31 10:24
     * @param
     * @return
     */
    @RequestMapping(value = "/updateMessage", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject updatePassword(@RequestBody CmsUserInfo cmsUserInfo,@RequestParam(value = "guid")String guid) {
        logger.info("updatePassword==>userName={}", cmsUserInfo.getUserName());
        JSONObject result = new JSONObject();
        if (null==cmsUserInfo.getUserName()||"".equals(cmsUserInfo.getUserName().trim())){
            //用户名为空
            ResponseResult.userNameNullResult(result);
            return result;
        }
        //因为用户表也要添加数据,所以判断用户表中有没有该用户名
        CmsUserInfo byUsername = cmsUserService.getByUsername(cmsUserInfo.getUserName());
        if (byUsername != null&&!byUsername.getGuid().equals(cmsUserInfo.getGuid())) {
            //用户名已存在
            ResponseResult.userNameErrorResult(result);
            return result;
        }
        SysUserInfo byPhone = userInfoService.getByPhone(cmsUserInfo.getPhone());
        if (null!=byPhone&&!byPhone.getGuid().equals(cmsUserInfo.getGuid())) {
            //手机号已存在
            ResponseResult.phoneErrorResult(result);
            return result;
        }
        if (null==cmsUserInfo.getGuid()||"".equals(cmsUserInfo.getGuid().trim())||!cmsUserService.findByGuid(cmsUserInfo.getGuid()).isPresent()){
            ResponseResult.noPeople(result);
            return result;
        }
        CmsUserInfo cmsUserInfo1 = cmsUserService.findByGuid(cmsUserInfo.getGuid()).get();
        Optional<SysUserInfo> sysByGuid = cmsUserService.findSysByGuid(cmsUserInfo.getGuid());
        if (!sysByGuid.isPresent()){
            ResponseResult.internalErrorResult(result);
            return result;
        }
        SysUserInfo sysUserInfo = sysByGuid.get();
        //先把新的用户名存起来,下面获取盐生成新密码才不会出错
        cmsUserInfo1.setUserName(cmsUserInfo.getUserName());
        cmsUserInfo1.setPhone(cmsUserInfo.getPhone());
        sysUserInfo.setPhone(cmsUserInfo.getPhone());
        cmsUserInfo1.setPermissions(cmsUserInfo.getPermissions());
        if (null!=cmsUserInfo.getPassword()&&!"".equals(cmsUserInfo.getPassword().trim())){
            //生成salt
            cmsUserInfo1.setSalt(randomNumberGenerator.nextBytes().toHex());
            sysUserInfo.setSalt(cmsUserInfo1.getSalt());
            //生成加密密码
            String newPassword =
                    new SimpleHash(algorithmName, cmsUserInfo.getPassword(),
                            ByteSource.Util.bytes(cmsUserInfo1.getCredentialsSalt()), hashIterations).toHex();
            String userNewPassword =
                    new SimpleHash(algorithmName, cmsUserInfo.getPassword(),
                            ByteSource.Util.bytes(sysUserInfo.getCredentialsSalt()), hashIterations).toHex();
            cmsUserInfo1.setPassword(newPassword);
            sysUserInfo.setPassword(userNewPassword);
        }
        if (null==cmsUserInfo.getHeadimgurl()||"".equals(cmsUserInfo.getHeadimgurl().trim())){
        }else {
            cmsUserInfo1.setHeadimgurl(cmsUserInfo.getHeadimgurl());
            sysUserInfo.setHeadimgurl(cmsUserInfo.getHeadimgurl());
        }
        cmsUserInfo1.setLastModifyTime(new Date());
        sysUserInfo.setLastModify(new Date().getTime());
        cmsUserInfo1.setLastModifyId(guid);
        sysUserInfo.setIsDeleted(0);
        cmsUserService.updateUserInfo(cmsUserInfo1,sysUserInfo);
        ResponseResult.successResult(result);
        result.put("data", new JSONObject());
        return result;
    }


    /**
     * @desc 修改密码;用户名不能重复
     * @auth lky
     * @date 2019/7/31 10:25
     * @param
     * @return
     */
    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject updatePassword(@RequestBody SysUserInfo sysUserInfo) {
        logger.info("updatePassword==>userName={}", sysUserInfo.getUsername());
        JSONObject result = new JSONObject();
        if (null==sysUserInfo.getGuid()||"".equals(sysUserInfo.getGuid().trim())||!userInfoService.getByUid(sysUserInfo.getGuid()).isPresent()){
            ResponseResult.noPeople(result);
            return result;
        }
        if(null==sysUserInfo.getPassword()||"".equals(sysUserInfo.getPassword().trim())){
            ResponseResult.passWordIsNull(result);
            return result;
        }
        SysUserInfo sysUserInfo1 = userInfoService.getByUid(sysUserInfo.getGuid()).get();
//        sysUserInfo.setP(sysUserInfo.getPassword());
        //生成salt
        sysUserInfo1.setNickname(sysUserInfo.getUsername());
        sysUserInfo1.setSalt(randomNumberGenerator.nextBytes().toHex());
        //生成加密密码
        String newPassword =
                new SimpleHash(algorithmName, sysUserInfo.getPassword(),
                        ByteSource.Util.bytes(sysUserInfo1.getCredentialsSalt()), hashIterations).toHex();
        sysUserInfo1.setPassword(newPassword);
        sysUserInfo1.setLastModify(new Date().getTime());
        userInfoService.updateUserInfo(sysUserInfo1);
        ResponseResult.successResult(result);
        result.put("data", new JSONObject());
        return result;
    }


    @RequestMapping(value = "getPermissions",method = RequestMethod.GET)
    public Object getPermissions(){
        List<CmsPermissions> allPermissions = cmsUserService.getAllPermissions();
        JSONObject result = new JSONObject();
        ResponseResult.successResult(result);
        result.put("data",allPermissions);
        return result;
    }


    //教材toB批量注册学生老师
    public SysUserInfo registUserForSchool(String nickname, String phone, String mail,String passWord) {
        //重名拼个数字
        for (int i=1;;i++){
            SysUserInfo sysByUsername = userInfoService.getByNickName(nickname);
            if (null == sysByUsername){
                break;
            }else {
                nickname = nickname+1;
            }
        }
        SysUserInfo sysUserInfo = new SysUserInfo();
        //设置昵称
        String username = cmsUserService.getTobUsername();
        sysUserInfo.setUsername(username);
        sysUserInfo.setNickname(nickname);
        sysUserInfo.setSalt(randomNumberGenerator.nextBytes().toHex());
        String newPassword = "";
        if (null==passWord||"".equals(passWord)){
            newPassword =
                    new SimpleHash(algorithmName, phone,
                            ByteSource.Util.bytes(sysUserInfo.getCredentialsSalt()), hashIterations).toHex();
        }else {
            newPassword =
                    new SimpleHash(algorithmName, passWord,
                            ByteSource.Util.bytes(sysUserInfo.getCredentialsSalt()), hashIterations).toHex();
        }


        sysUserInfo.setPassword(newPassword);
        //生成guid
        UUID uuid = UUID.randomUUID();
        String s = uuid.toString();
        String replace = s.replace("-", "");
        sysUserInfo.setGuid(replace);
        sysUserInfo.setLastModify(System.currentTimeMillis());

        sysUserInfo.setIsDeleted(0);
        sysUserInfo.setHeadimgurl(headImageUrl);
        sysUserInfo.setManifesto("一句话介绍我自己");
        sysUserInfo.setPhone(phone);
        sysUserInfo.setRegistts(System.currentTimeMillis());
        sysUserInfo.setIsAdmin(false);
        sysUserInfo.setUserLevel("1");
        sysUserInfo.setIsStu(true);
        sysUserInfo.setIsEnterprise("0");
        sysUserInfo.setNoviceGuidanceSteps("1");
        sysUserInfo.setSource(7);
        logger.info("register==>userName={},phone={}", sysUserInfo.getUsername(), sysUserInfo.getPhone());
        SysUserInfo sysUserInfo1 = userInfoService.addUserInfo(sysUserInfo);
        //用户受欢迎度 初始化
        try {
            userVisitService.findByGuid(sysUserInfo.getGuid());
        } catch (Exception e) {
            logger.error("User popularity initialization failed", e);
        }
        return sysUserInfo1;
    }


    /**
     * @Author lky
     * @Description 注册toB普通用户,用户名,密码必填,昵称,手机号非必填(会出现手机号为空的情况)
     * @Date 12:51 2020/4/23
     * @Param
     * @return
     **/
    @RequestMapping("registerCommonUser")
    public Object registerCommonUser(@RequestBody SysUserInfo sysUserInfo) {
        JSONObject result = ResponseResult.getResult();
        try {
            String username = sysUserInfo.getUsername();
            if (null==username||"".equals(username)){
                //用户名为空
                ResponseResult.userNameNullResult(result);
                return result;
            }
            String nickname = sysUserInfo.getNickname();
            if (null == nickname || "".equals(nickname)) {
                //没传nickname,username就是nickname
                nickname = username;
            }
            username = tobNamePre + username;
            SysUserInfo byUsername = userInfoService.getByUsername(username);
            if (null != byUsername) {
                //用户名被占用
                ResponseResult.userNameErrorResult(result);
                return result;
            }
            //出现重复的情况就往后面补1,保证不会重复
            for (;;) {
                SysUserInfo byNickName = userInfoService.getByNickName(nickname);
                if (null != byNickName) {
                    nickname = nickname + 1;
                } else {
                    break;
                }
            }
            String phone = sysUserInfo.getPhone();
            if (null == phone) {
                phone = "";
            }
            String password = sysUserInfo.getPassword();
            if (null == password || "".equals(password.trim())) {
                //密码没填
                ResponseResult.passWordIsNull(result);
                return result;
            }
            sysUserInfo.setUsername(username);
            sysUserInfo.setNickname(nickname);
            sysUserInfo.setPhone(phone);
            sysUserInfo.setPassword(password);
            sysUserInfo.setSalt(randomNumberGenerator.nextBytes().toHex());
            String userNewPassword =
                    new SimpleHash(algorithmName, sysUserInfo.getPassword(),
                            ByteSource.Util.bytes(sysUserInfo.getCredentialsSalt()), hashIterations).toHex();
            sysUserInfo.setPassword(userNewPassword);
            //生成guid
            UUID uuid = UUID.randomUUID();
            String s = uuid.toString();
            String replace = s.replace("-", "");

            sysUserInfo.setGuid(replace);
            sysUserInfo.setLastModify(new Date().getTime());
            sysUserInfo.setIsDeleted(0);

            sysUserInfo.setHeadimgurl(headImageUrl);

            sysUserInfo.setManifesto("一句话介绍我自己");
            sysUserInfo.setRegistts(new Date().getTime());
            sysUserInfo.setIsAdmin(false);
            sysUserInfo.setUserLevel("1");
            sysUserInfo.setIsStu(true);
            sysUserInfo.setIsEnterprise("0");
            sysUserInfo.setNoviceGuidanceSteps("1");
            sysUserInfo.setSource(5);

            //7天免费试用
            Integer day = 7;
            Calendar instance = Calendar.getInstance();
            instance.set(instance.get(Calendar.YEAR),instance.get(Calendar.MONTH),instance.get(Calendar.DATE)+day);
            Date time = instance.getTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String format = simpleDateFormat.format(time);

            //在图灵引擎注册账号
            JSONObject param = new JSONObject();
            param.put("userId",sysUserInfo.getGuid());
            param.put("loginId",sysUserInfo.getUsername());
            param.put("userName",sysUserInfo.getNickname());
            param.put("password",sysUserInfo.getPassword());
            param.put("salt",sysUserInfo.getSalt());
            param.put("mobile",sysUserInfo.getPhone());
            param.put("email",sysUserInfo.getEmail()==null?"":sysUserInfo.getEmail());
            param.put("failureTime",format);
            String encrypt = Encryptor.encrypt(param.toJSONString());
            param.clear();
            param.put("encryptedString",encrypt);
            JSONObject jsonObject = HttpUtil.sendPostRequestByJson(datasienceRegisterUrl, param);
            if (null!=jsonObject){
                Integer code = jsonObject.getInteger("code");
                if(null!=code&&SystemConstants.CODE_SUCCESS-code==0){
                    //注册成功
                }else {
                    result.put("code", SystemConstants.CODE_FAILURE);
                    result.put("data",new JSONObject());
                    result.put("msg", "用户注册失败");
                    logger.error("图灵引擎注册用户失败"+result.toString());
                }
            }

            JSONObject param1 = new JSONObject();
            param1.put("userId",sysUserInfo.getGuid());
            param1.put("loginId",sysUserInfo.getUsername());
            param1.put("purchasingVersion","1");
            param1.put("expirationDate",format);
            encrypt = Encryptor.encrypt(param1.toJSONString());
            param1.clear();
            param1.put("encryptedString",encrypt);
            JSONObject returnStr = HttpUtil.sendPostRequestByJson(datasiencePurchase, param1);
            if (null!=returnStr){
                if(1000-returnStr.getInteger("code")==0){
                }else if (400-returnStr.getInteger("code")==0) {
                    logger.error("datasiencePurchase is failed,return msg:{}",s);
                    throw new RuntimeException();
                }else {
                    logger.error("datasiencePurchase is failed,return msg:{}",s);
                    throw new RuntimeException();
                }
            }else {
                logger.error("datasiencePurchase is failed,return msg is null");
                throw new RuntimeException();
            }


            logger.info("register==>userName={},phone={}", sysUserInfo.getUsername(), sysUserInfo.getPhone());
            SysUserInfo sysUserInfo1 = userInfoService.addUserInfo(sysUserInfo);
            //用户受欢迎度 初始化
            try {
                userVisitService.findByGuid(sysUserInfo.getGuid());
            } catch (Exception e) {
                logger.error("User popularity initialization failed", e);
            }
            result.put("code", SystemConstants.CODE_SUCCESS);
            result.put("data",new JSONObject());
            result.put("msg", "注册成功");
        } catch (Exception e) {
            result.put("code", SystemConstants.CODE_FAILURE);
            result.put("data",new JSONObject());
            result.put("msg", "用户注册失败");
            logger.error(result.toString(), e);
        }
        logger.info(result.toString());

        return result;

    }


    /**
     * @Author lky
     * @Description 教务后台账号的注册
     * @Date 17:56 2020/6/29
     * @Param
     * @return
     **/
    @PostMapping("registerSchoolBackground")
    public Object registerSchoolBackground(@RequestBody CmsUserInfo cmsUserInfo,@RequestParam(value = "guid")String guid,@RequestParam("schoolName")String schoolName){
        JSONObject result = ResponseResult.getResult();
        SysUserInfo sysUserInfo = new SysUserInfo();
        try {
            //后台的用户名是前台的昵称,昵称不能重复
            SysUserInfo sysByUsername = userInfoService.getByNickName(cmsUserInfo.getUserName());
            if (null!=sysByUsername) {
                ResponseResult.userNameErrorResult(result);
                return result;
            }
            SysUserInfo byUsername = userInfoService.getByUsername(cmsUserInfo.getUserName());
            if (null!=byUsername) {
                ResponseResult.userNameErrorResult(result);
                return result;
            }
            if (null==cmsUserInfo.getPassword()||"".equals(cmsUserInfo.getPassword())){
                ResponseResult.passWordIsNull(result);
                return result;
            }

            if (null==cmsUserInfo.getPhone()||"".equals(cmsUserInfo.getPhone().trim())){
                ResponseResult.phoneIsNull(result);
                return result;
            }
            SysUserInfo byPhone = cmsUserService.findByPhone(cmsUserInfo.getPhone());
            if (null!=byPhone){
                ResponseResult.phoneErrorResult(result);
                return result;
            }
            { //正常注册的用户
                //生成salt
                //设置昵称
                String username = cmsUserService.getTocUsername();
                //前台 c_开头的用户名,昵称是传过来的名字
                sysUserInfo.setNickname(cmsUserInfo.getUserName());
                sysUserInfo.setUsername(username);
                cmsUserInfo.setNickname(cmsUserInfo.getUserName());
                cmsUserInfo.setSalt(randomNumberGenerator.nextBytes().toHex());
                sysUserInfo.setSalt(cmsUserInfo.getSalt());
                //生成加密密码
                String cmsNewPassword =
                        new SimpleHash(algorithmName, cmsUserInfo.getPassword(),
                                ByteSource.Util.bytes(cmsUserInfo.getCredentialsSalt()), hashIterations).toHex();
                String userNewPassword =
                        new SimpleHash(algorithmName, cmsUserInfo.getPassword(),
                                ByteSource.Util.bytes(sysUserInfo.getCredentialsSalt()), hashIterations).toHex();
                cmsUserInfo.setPassword(cmsNewPassword);
                sysUserInfo.setPassword(userNewPassword);
                //生成guid
                UUID uuid = UUID.randomUUID();
                String s = uuid.toString();
                String replace = s.replace("-", "");
                cmsUserInfo.setGuid(replace);
                sysUserInfo.setGuid(replace);
                cmsUserInfo.setCreatedTime(new Date());
                cmsUserInfo.setIsDeleted(0);
                cmsUserInfo.setLastModifyTime(new Date());
                sysUserInfo.setLastModify(System.currentTimeMillis());
                cmsUserInfo.setLastModifyId(guid);
                cmsUserInfo.setCreatedId(guid);
                cmsUserInfo.setState(0);
                cmsUserInfo.setRole(-1);
                sysUserInfo.setIsDeleted(0);
                if (null==cmsUserInfo.getHeadimgurl()||"".equals(cmsUserInfo.getHeadimgurl().trim())){
                    cmsUserInfo.setHeadimgurl(headImageUrl);
                    sysUserInfo.setHeadimgurl(headImageUrl);
                }else {
                    sysUserInfo.setHeadimgurl(cmsUserInfo.getHeadimgurl());
                }
                sysUserInfo.setManifesto("一句话介绍我自己");
                sysUserInfo.setPhone(cmsUserInfo.getPhone());
                sysUserInfo.setRegistts(System.currentTimeMillis());
                sysUserInfo.setIsAdmin(false);
                sysUserInfo.setUserLevel("1");
                sysUserInfo.setIsStu(true);
                sysUserInfo.setIsEnterprise("0");
                sysUserInfo.setNoviceGuidanceSteps("1");
                sysUserInfo.setSource(9);
                cmsUserService.addUserInfo(cmsUserInfo,sysUserInfo);

                //用户和学校进行绑定
                CosSchool cosSchool = cosSchoolRepository.findBySchoolName(schoolName);
                if (null==cosSchool){
                    cosSchool = new CosSchool();
                    cosSchool.setSchoolName(schoolName);
                    cosSchool.setCreatedTime(new Date());
                    cosSchool = cosSchoolRepository.save(cosSchool);
                }
                CosUserMsg cosUserMsg = new CosUserMsg();
                cosUserMsg.setSchoolId(cosSchool.getSchoolId());
                cosUserMsg.setSchoolName(schoolName);
                cosUserMsg.setGuid(replace);
                cosUserMsg.setModifyTime(new Date());
                cosUserMsg.setCreatedTime(new Date());
                cosUserMsg.setPhone(cmsUserInfo.getPhone());
                cosUserMsg.setRole(3);
                cosUserMsgRepository.save(cosUserMsg);


                logger.info("register==>userName={},phone={}", cmsUserInfo.getUserName(), cmsUserInfo.getPhone());

                cmsUserService.addUserInfo(cmsUserInfo,sysUserInfo);
                //用户受欢迎度 初始化
                try {
                    userVisitService.findByGuid(sysUserInfo.getGuid());
                } catch (Exception e) {
                    logger.error("User popularity initialization failed", e);
                }
                result.put("code", SystemConstants.CODE_SUCCESS);
                result.put("msg", "注册成功");
            }
        } catch (Exception e) {
            result.put("code", SystemConstants.CODE_FAILURE);
            result.put("msg", "用户注册失败：" + e.getMessage());
            logger.error(result.toString(),e);
        }
        logger.info(result.toString());

        return result;
    }
}
