package com.bonc.turing.cms.exercise.controller;

import com.alibaba.fastjson.JSONObject;

import com.bonc.turing.cms.exercise.service.QtTeacherService;
import com.bonc.turing.cms.manage.ResultEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 教师相关操作
 */
@RestController
@RequestMapping("/teacher")
public class QtTeacherController {
    private static Logger logger = LoggerFactory.getLogger(QtTeacherController.class);

    @Autowired
    QtTeacherService qtTeacherService;

    /**
     * 显示导师列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("showTeacherList")
    public ResponseEntity showTeacherList(@RequestParam("pageNum")int pageNum, @RequestParam("pageSize") int pageSize){

        Object o = qtTeacherService.showTeacherList(pageNum, pageSize);
        if(o.equals("failure")){
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE,"","获取导师列表失败"));
        }
        else {
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK,o,"获取导师列表成功"));
        }
    }
    /**
     * 显示待审核导师列表
     * @param pageNum
     * @param pageSize
     * @param state
     * @return
     */
    @GetMapping("showPendingTeacherList")
    public ResponseEntity showPendingTeacherList(@RequestParam("pageNum")int pageNum, @RequestParam("pageSize") int pageSize, @RequestParam("state") String state){
        Object o = qtTeacherService.showPendingTeacherList(pageNum, pageSize,state);
        if(o.equals("failure")){
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE,"","获取待审核导师列表失败"));
        }
        else {
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK,o,"获取待审核导师列表成功"));
        }
    }

    /**
     * 上、下线导师
     * @param isOnline
     * @param teacherId
     * @return
     */
    @GetMapping("changeStatus")
    public ResponseEntity changeStatus(@RequestParam("isOnline")int isOnline, @RequestParam("teacherId") String teacherId){
        Object o = qtTeacherService.changeStatus(isOnline,teacherId);
        if(o.equals("success")){
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK,"","改变状态成功"));
        }
        else {
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE,"","改变状态失败"));
        }
    }

    /**
     * 审核导师
     * @param isPass
     * @param teacherId
     * @return
     */
    @GetMapping("verifyTeacher")
    public ResponseEntity verifyTeacher(@RequestParam("isPass")int isPass, @RequestParam("teacherId") String teacherId){
        Object o = qtTeacherService.verifyTeacher(isPass,teacherId);
        if(o.equals("success")){
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK,"","审核导师成功"));
        }
        else {
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE,"","审核导师失败"));
        }
    }

    /**
     * 显示导师信息详情
     * @param teacherId
     * @return
     */
    @GetMapping("showTeacherDetail")
    public ResponseEntity showTeacherDetail(@RequestParam("teacherId") String teacherId){
        Object o = qtTeacherService.showTeacherDetail(teacherId);
        if(o.equals("failure")){
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE,"","获取导师详情失败"));
        }
        else {
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK,o,"获取导师详情成功"));
        }
    }

    /**
     * 编辑教师信息
     * @param params
     * @return
     */
    @PostMapping("/editTeacherInfo")
    public ResponseEntity editTeacherInfo(@RequestBody JSONObject params){
        Object o =  qtTeacherService.editTeacherInfo(params);
        if(o.equals("success")){
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK,"","编辑导师成功"));
        }
        else {
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE,"","审核导师失败"));
        }
    }




}
