package com.bonc.turing.cms.exercise.controller;

import com.alibaba.fastjson.JSONObject;
import com.bonc.turing.cms.exercise.domain.CosCourse;
import com.bonc.turing.cms.exercise.service.CosCourseService;
import com.bonc.turing.cms.manage.ResultEntity;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


/**
 * @desc 课程相关
 * @author yh
 * @date 2020.03.23
 */
@RequestMapping("cos")
@RestController
public class CosCourseController {
    private static Logger logger = LoggerFactory.getLogger(CosCourseController.class);

    @Autowired
    private CosCourseService cosCourseService;


    /**
     * 创建/编辑课程
     *
     * @param course
     * @return
     */
    @RequestMapping(value = "createOrEditCourse", method = RequestMethod.POST)
    Object createOrEditCourse(@RequestBody CosCourse course) {
        Object o = cosCourseService.createOrEditCourse(course);
        if (o.equals("failure")) {
            logger.error("createOrEditCourse failure");
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE,"","创建课程失败"));
        } else if(o.equals("no_permission")){
            logger.error("createOrEditCourse no permission");
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE,"","您没有权限创建此类型课程，如有疑问请联系管理员"));
        }
        else {
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, o, "创建课程成功"));
        }
    }

    /**
     * 后台-返显课程基本信息
     *
     * @param courseId
     * @return
     */
    @RequestMapping(value = "showCourse", method = RequestMethod.GET)
    Object showCourse(String courseId) {
        Object o = cosCourseService.showCourse(courseId);
        if (o.equals("failure")) {
            logger.error("showCourse failure");
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE,"","返显课程基本信息失败"));
        } else {
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, o, "返显课程基本信息成功"));
        }
    }

    /**
     * 删除课程
     *
     * @param courseId
     * @return
     */
    @RequestMapping(value = "deleteCourse", method = RequestMethod.GET)
    Object deleteCourse(String courseId) {
        Object o = cosCourseService.deleteCourse(courseId);
        if (o.equals("success")) {
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, "", "删除课程成功"));
        } else {
            logger.error("deleteCourse failure");
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE,"",o.toString()));
        }
    }


    /**
     * 上/下线课程
     * @param courseId
     * @return
     */
    @RequestMapping(value = "changeStatus", method = RequestMethod.GET)
    Object deleteCourse(String courseId,int operateType) {
        Object o = cosCourseService.changeStatus(courseId,operateType);
        if (o.equals("success")) {
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, "", "修改课程状态成功"));
        } else {
            logger.error("changeStatus failure");
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE,"",o.toString()));
        }
    }

    /**
     *后台-课程列表
     * @param params
     * @return
     */
    @RequestMapping(value = "cmsCourseList", method = RequestMethod.POST)
    Object cmsCourseList(@RequestBody JSONObject params) {
        Object o = cosCourseService.cmsCourseList(params);
        if (o.equals("failure")) {
            logger.error("cmsCourseList failure");
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE,"","获取课程列表失败"));

        } else {
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, o, "获取课程列表成功"));
        }
    }

    @GetMapping("/studentCommit")
    public Object studentCommit(String schoolCourseId, String guid, int pageNum, int pageSize){
        try {
            JSONObject jo = cosCourseService.getStudentCommit(guid, schoolCourseId, pageNum, pageSize);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, jo,"获取成功"));
        } catch (Exception e) {
            logger.error("studentCommit is failed", e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "获取失败"));
        }
    }

    @GetMapping("/saveScore")
    public Object saveScore(String id, int score){
        try {
            cosCourseService.saveScore(id, score);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, "","保存成功"));
        } catch (Exception e) {
            logger.error("saveScore is failed", e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "保存失败"));
        }
    }

    @GetMapping("/displayOrHideCourse")
    public Object displayOrHideCourse(String schoolCourseId, int state){
        try {
            cosCourseService.displayOrHideCourse(schoolCourseId, state);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, "","保存成功"));
        } catch (Exception e) {
            logger.error("displayOrHideCourse is failed", e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "保存失败"));
        }
    }



}
