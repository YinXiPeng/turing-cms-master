package com.bonc.turing.cms.exercise.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bonc.turing.cms.annotation.RoleJudge;
import com.bonc.turing.cms.enums.UserRoleEnum;
import com.bonc.turing.cms.exercise.domain.CosTeacherHome;
import com.bonc.turing.cms.exercise.service.CosTeacherHomeService;
import com.bonc.turing.cms.manage.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("teacherHome")
@Slf4j
public class CosTeacherHomeController {

    @Autowired
    private CosTeacherHomeService cosTeacherHomeService;

    /**
     * @Author lky
     * @Description 查询教师列表
     * @Date 18:22 2020/6/28
     * @Param
     * @return
     **/
    @RoleJudge(role = UserRoleEnum.CODE_2001)
    @GetMapping("getTeacherList")
    public Object getTeacherList(@RequestParam(value = "guid",required = false,defaultValue = "")String guid,@RequestParam("schoolId")String schoolId,@RequestParam(value = "pageNum",required = false,defaultValue = "0")Integer pageNum,@RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize){
        try {
            Object teacherList = cosTeacherHomeService.getTeacherList(guid,schoolId,pageNum,pageSize);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK,teacherList,"查询教师列表成功"));
        } catch (Exception e) {
            log.error("getTeacherList is failed",e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE,new JSONArray(),"查询教师列表失败"));
        }
    }

    /**
     * @Author lky
     * @Description 新增或修改教师信息
     * @Date 18:22 2020/6/28
     * @Param
     * @return
     **/
    @RoleJudge(role = UserRoleEnum.CODE_2001)
    @PostMapping("setTeacherMsg")
    public Object setTeacherMsg(@RequestBody CosTeacherHome cosTeacherHome){
        try {
            cosTeacherHomeService.setTeacherMsg(cosTeacherHome);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK,"","更新教师信息成功"));
        } catch (Exception e) {
            log.error("setTeacherMsg is faile",e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE,"","更新教师信息失败"));
        }
    }


    /**
     * @Author lky
     * @Description 删除教师
     * @Date 18:37 2020/6/28
     * @Param
     * @return
     **/
    @RoleJudge(role = UserRoleEnum.CODE_2001)
    @PostMapping("delTeacher")
    public Object delTeacher(@RequestBody JSONObject jsonObject){
        try {
            String teacherId = jsonObject.getString("teacherId");
            cosTeacherHomeService.delTeacher(teacherId);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK,"","删除教师成功"));
        } catch (Exception e) {
            log.error("delTeacher is failed",e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE,"","删除教师失败"));
        }
    }
}
