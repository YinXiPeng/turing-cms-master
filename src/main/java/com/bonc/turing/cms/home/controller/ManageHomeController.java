package com.bonc.turing.cms.home.controller;

import com.alibaba.fastjson.JSONObject;
import com.bonc.turing.cms.home.domain.Activity;
import com.bonc.turing.cms.home.service.ManageHomeService;
import com.bonc.turing.cms.manage.ResultEntity;
import com.bonc.turing.cms.manage.entity.user.SysUserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/home")
public class ManageHomeController {

    private static Logger logger = LoggerFactory.getLogger(ManageHomeController.class);

    @Autowired
    private ManageHomeService manageHomeService;


    /**
     * 创建马甲号（运营使用）
     *
     * @param sysUserInfo
     * @return
     */
    @RequestMapping(value = "createAccount", method = RequestMethod.POST)
    Object createAccount(@RequestBody SysUserInfo sysUserInfo) {
        Object isDuplicate = manageHomeService.checkUserName(sysUserInfo.getUsername());
        if (1 == Integer.parseInt(isDuplicate.toString())) {
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "用户名重复"));
        }
        Object o = manageHomeService.createAccount(sysUserInfo);
        if (o.equals("failure")) {
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE));
        } else {
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, "", "创建成功"));
        }
    }


    /**
     *删除马甲号账号
     *
     * @return
     */
    @RequestMapping(value = "deleteAccount", method = RequestMethod.POST)
    Object deleteAccount(@RequestBody JSONObject params) {
        Object o = manageHomeService.deleteAccount(params);
        if (o.equals("success")) {
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, "", "删除马甲号账户成功"));
        } else {
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE));
        }
    }


    /**
     * 获取马甲号列表（运营使用）
     *
     * @return
     */
    @RequestMapping(value = "accountList", method = RequestMethod.GET)
    Object accountList(int pageNum, int pageSize) {
        try {
            Object o = manageHomeService.accountList(pageNum, pageSize);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, o, "获取列表成功"));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("accountList exception{}", e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE));
        }
    }

    /**
     * 创建活动
     *
     * @return
     */
    @RequestMapping(value = "createActivity", method = RequestMethod.POST)
    Object createActivity(@RequestBody Activity activity) {
        Object o = manageHomeService.createActivity(activity);
        if (o.equals("failure")) {
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE));
        } else {
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, "", "创建活动成功"));
        }
    }


    /**
     * 获取活动列表
     *
     * @return
     */
    @RequestMapping(value = "activityList", method = RequestMethod.GET)
    Object activityList(int pageNum, int pageSize, String title) {
        try {
            Object o = manageHomeService.activityList(pageNum, pageSize, title);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, o, "获取活动列表成功"));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("");
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE));
        }
    }

    /**
     * 上/下线活动
     *
     * @return
     */
    @RequestMapping(value = "changeActivityStatus", method = RequestMethod.GET)
    Object changeActivityStatus(int status, String activityId) {
        Object o = manageHomeService.changeActivityStatus(status, activityId);
        if (o.equals("success")) {
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, "", "操作成功"));
        } else {
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE));
        }
    }

    /**
     * 删除活动
     *
     * @return
     */
    @RequestMapping(value = "deleteActivity", method = RequestMethod.GET)
    Object deleteActivity(String activityId) {
        Object o = manageHomeService.deleteActivity(activityId);
        if (o.equals("success")) {
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, "", "删除成功"));
        } else {
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE));
        }
    }

    /**
     * 删除feed流相应内容
     *
     * @return
     */
    @RequestMapping(value = "deleteHomeContent", method = RequestMethod.POST)
    Object deleteHomeContent(@RequestBody JSONObject params) {
        Object o = manageHomeService.deleteHomeContent(params);
        if (o.equals("success")) {
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, "", "删除成功"));
        } else {
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", o.toString()));
        }
    }

    /**
     * 隐藏feed流相应内容
     *
     * @return
     */
    @RequestMapping(value = "hideHomeContent", method = RequestMethod.POST)
    Object hideHomeContent(@RequestBody JSONObject params) {
        Object o = manageHomeService.hideHomeContent(params);
        if (o.equals("success")) {
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, "", "隐藏成功"));
        } else {
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", o.toString()));
        }
    }

    /**
     * 用运营号对内容点赞
     *
     * @return
     */
    @RequestMapping(value = "likeHomeContent", method = RequestMethod.POST)
    Object likeHomeContent(@RequestBody JSONObject params) {
        Object o = manageHomeService.likeHomeContent(params);
        if (o.equals("success")) {
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, "", "点赞成功"));
        } else {
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", o.toString()));
        }
    }

    /**
     * feed内容类型列表
     *
     * @return
     */
    @RequestMapping(value = "feedTypeList", method = RequestMethod.GET)
    Object feedTypeList() {
        try {
            Object o = manageHomeService.feedTypeList();
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, o, "获取成功"));
        }catch (Exception e){
            e.printStackTrace();
            logger.error("feedTypeList exception{}",e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, ""));
        }

    }


}
