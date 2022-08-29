package com.bonc.turing.cms.manage.controller.user;


import com.alibaba.fastjson.JSONObject;
import com.bonc.turing.cms.common.utils.PageInfoEntity;
import com.bonc.turing.cms.manage.ResultEntity;
import com.bonc.turing.cms.manage.entity.user.CmsPermissions;
import com.bonc.turing.cms.manage.entity.user.CmsUserInfo;
import com.bonc.turing.cms.manage.service.user.CmsUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private CmsUserService cmsUserService;

    /**
     * @desc 后台查询管理员,超级管理员,普通用户的接口
     * @auth lky
     * @date 2019/8/5 11:23
     * @param
     * @return
     */
    @RequestMapping(value = "getUserList",method = RequestMethod.GET)
    public Object getUserList(@RequestParam(value = "type")int type,@RequestParam(value = "keyWord",required = false)String keyWord,@RequestParam(value = "pageNum",required = false,defaultValue = "1")int pageNum,@RequestParam(value = "pageSize",required = false,defaultValue = "0")int pageSize){
        logger.info("getUserList==>type:{},keyWord:{}",type,keyWord);
        if (null==keyWord){
            keyWord="";
        }
        Map all = cmsUserService.findAll(type, keyWord, pageNum, pageSize);
        return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, all,"操作成功!"));
    }

    @RequestMapping(value = "deleteUser")
    public Object deleteUser(@RequestParam(value = "userId")String userId,@RequestParam(value = "guid")String guid){
        logger.info("deleteUser==>userId:{},guid:{}",userId,guid);
        Optional<CmsUserInfo> byGuid = cmsUserService.findByGuid(guid);
        //0无权限删除,1有权限删除

        if (byGuid.isPresent()){
            CmsUserInfo cmsUserInfo = byGuid.get();
            int role = cmsUserInfo.getRole();
            if (1!=role){
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, null, "您没有删除的权限！"));
            }

        }
        cmsUserService.deleteByGuid(userId,guid);
        return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, null, "删除成功！"));
    }


    /**
     * @Author lky
     * @Description 更新用户积分
     * @Date 21:29 2020/6/19
     * @Param
     * @return
     **/
    @RequestMapping("updateUserCredit")
    public Object updateUserCredit(@RequestBody JSONObject jsonObject){
        try {
            boolean flag = cmsUserService.updateUserCredit(jsonObject);
            if (flag){
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, "", "修改积分成功！"));
            }else {
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "修改积分失败！"));
            }
        } catch (Exception e) {
            logger.error("updateUserCredit is failed",e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "修改积分失败！"));
        }
    }

    /**
     * @Author lky
     * @Description 查询b端用户的账号列表
     * @Date 20:56 2020/7/24
     * @Param
     * @return
     **/
    @RequestMapping("getUserListOfB")
    public Object getUserListOfB(@RequestParam("pageNum")Integer pageNum,@RequestParam("pageSize")Integer pageSize,@RequestBody JSONObject jsonObject){
        try {
            String keyWord = jsonObject.getString("keyWord");
            PageInfoEntity userListOfB = cmsUserService.getUserListOfB(keyWord, pageNum, pageSize);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, userListOfB, "查询B端用户列表成功"));
        } catch (Exception e) {
            logger.error("getUserListOfB is failed",e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "查询B端用户列表失败"));
        }
    }


    /**
     * @Author lky
     * @Description 更新用户的权限
     * @Date 17:25 2020/7/27
     * @Param
     * @return
     **/
    @RequestMapping("updateUserPermissions")
    public Object updateUserPermissions(@RequestBody JSONObject jsonObject){
        try {
            cmsUserService.updateUserPermissions(jsonObject);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, "", "更新用户权限成功"));
        } catch (Exception e) {
            logger.error("updateUserPermissions is failed",e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "更新用户权限失败"));
        }
    }

    /**
     * @Author lky
     * @Description 查询用户的权限
     * @Date 18:13 2020/7/27
     * @Param
     * @return
     **/
    @RequestMapping("getUserPermissions")
    public Object getUserPermissions(@RequestParam("guid")String guid){
        try {
            List permissions = cmsUserService.getUserPermissions(guid);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, permissions, "查询用户权限成功"));
        } catch (Exception e) {
            logger.error("getUserPermissions is failed",e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "查询用户权限失败"));
        }
    }
}
