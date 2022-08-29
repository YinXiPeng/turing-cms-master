package com.bonc.turing.cms.exercise.controller;

import com.alibaba.fastjson.JSONObject;
import com.bonc.turing.cms.exercise.service.LiveRecordService;
import com.bonc.turing.cms.manage.ResultEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/liveRecord")
@RestController
public class LiveRecordController {
    private static Logger logger = LoggerFactory.getLogger(LiveRecordController.class);

    @Autowired
    private LiveRecordService liveRecordService;

    /**
     * 获取直播记录或者直播订单
     * @param type
     * @param keyWord
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/getLiveRecord")
    public Object getLiveRecord(@RequestParam("type")int type,@RequestParam(value = "keyWord",required = false)String keyWord,@RequestParam("pageNum")int pageNum,@RequestParam("pageSize")int pageSize){
        try {
            JSONObject jsonObject = liveRecordService.getLiveRecord(type,keyWord,pageNum,pageSize);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK,jsonObject,"查询成功"));
        } catch (Exception e) {
            logger.error("getLiveRecord is error",e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE,"","查询失败"));
        }
    }


    @RequestMapping("/editLiveRecordRemark")
    public Object editLiveRecordRemark(@RequestBody JSONObject jsonObject){
        try {
            liveRecordService.editLiveRecordRemark(jsonObject);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK,"","添加备注成功"));
        } catch (Exception e) {
            logger.error("editRemarks is error",e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE,"","添加备注失败"));
        }
    }
}
