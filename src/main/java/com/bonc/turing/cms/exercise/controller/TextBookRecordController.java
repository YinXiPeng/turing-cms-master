package com.bonc.turing.cms.exercise.controller;

import com.alibaba.fastjson.JSONObject;
import com.bonc.turing.cms.exercise.service.TextBookRecordService;
import com.bonc.turing.cms.manage.ResultEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 购买教材记录
 */
@RequestMapping("/textBookRecord")
@RestController
public class TextBookRecordController {

    private static Logger logger = LoggerFactory.getLogger(TextBookRecordController.class);

    @Autowired
    private TextBookRecordService textBookRecordService;

    /**
     * 查询教材购买的记录
     * @param pageNum
     * @param pageSize
     * @param textBookId
     * @param keyWord
     * @param type
     * @return
     */
    @RequestMapping("/textBook")
    public Object textBook(@RequestParam("pageNum")Integer pageNum,@RequestParam("pageSize")Integer pageSize,@RequestParam(value = "textBookId",required = false)String textBookId,@RequestParam(value = "keyWord",required = false)String keyWord,@RequestParam("type")Integer type){
        try {
            JSONObject jsonObject = textBookRecordService.textBook(pageNum,pageSize,textBookId,keyWord,type);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK,jsonObject,"查询成功"));
        } catch (Exception e) {
            logger.error("textBook is error",e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE,"","查询失败"));
        }
    }

    /**
     * 给订单添加备注
     * @param jsonObject
     * @return
     */
    @RequestMapping("/editRemarks")
    public Object editRemarks(@RequestBody JSONObject jsonObject){
        try {
            textBookRecordService.editRemarks(jsonObject);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK,"","添加备注成功"));
        } catch (Exception e) {
            logger.error("editRemarks is error",e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE,"","添加备注失败"));
        }
    }

    /**
     * 删除订单
     * @param jsonObject
     * @return
     */
    @RequestMapping("/deleteOrder")
    public Object deleteOrder(@RequestBody JSONObject jsonObject){
        try {
            textBookRecordService.deleteOrder(jsonObject);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK,"","删除订单成功"));
        } catch (Exception e) {
            logger.error("editRemarks is error",e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE,"","删除订单失败"));
        }
    }
}
