package com.bonc.turing.cms.manage.controller.manage;

import com.alibaba.fastjson.JSONObject;
import com.bonc.turing.cms.manage.ResultEntity;
import com.bonc.turing.cms.manage.entity.notify.Notify;
import com.bonc.turing.cms.manage.service.manage.NotifyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lky
 * @desc  后台通知列表部分
 * @date 2019/8/6 16:21
 */
@RestController
@RequestMapping("/notify")
public class NotifyController {

    private static Logger logger = LoggerFactory.getLogger(NotifyController.class);

    @Autowired
    private NotifyService notifyService;

    @RequestMapping("/getNotifyList")
    public Object getNotifyList(@RequestParam(value = "pageNum",required = false,defaultValue = "0")int pageNum,@RequestParam(value = "pageSize",required = false,defaultValue = "50")int pageSize,@RequestParam(value = "keyWord",required = false,defaultValue = "")String keyWord){
        try{
            logger.info("getNotifyList 开始执行");
            JSONObject notifyList = notifyService.getNotifyList(pageNum,pageSize,keyWord);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, notifyList,"执行成功！"));
        }catch (Exception e){
            logger.error("getNotifyList 执行失败",e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "","执行失败！"));
        }

    }
}
