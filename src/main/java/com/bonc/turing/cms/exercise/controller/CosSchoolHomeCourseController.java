package com.bonc.turing.cms.exercise.controller;

import com.bonc.turing.cms.exercise.domain.CosSchoolHomeCourse;
import com.bonc.turing.cms.exercise.service.CosSchoolHomeCourseService;
import com.bonc.turing.cms.manage.ResultEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @desc 高校首页-课程展示相关
 * @author yh
 * @date 2020.06.29
 */
@RestController
@RequestMapping("cosSchoolHomeCourse")
public class CosSchoolHomeCourseController {
    private static Logger logger = LoggerFactory.getLogger(CosSchoolHomeCourseController.class);

    @Autowired
    private CosSchoolHomeCourseService cosSchoolHomeCourseService;

    /**
     * 可选的课程列表
     *
     * @param schoolId
     * @return
     */
    @GetMapping("showOptionalCourseList")
    public Object showOptionalCourseList(String schoolId) {
        try {
            Object o = cosSchoolHomeCourseService.showOptionalCourseList(schoolId);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, o, "获取可选的课程列表成功"));
        } catch (Exception e) {
            logger.error("showOptionalCourseList exception", e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "获取可选的课程列表时异常"));
        }
    }

    /**
     * 添加/编辑首页展示课程
     *
     * @param cosSchoolHomeCourse
     * @return
     */
    @PostMapping("addOrEditSchoolHomeCourse")
    public Object addOrEditSchoolHomeCourse(@RequestBody CosSchoolHomeCourse cosSchoolHomeCourse) {
        Object o = cosSchoolHomeCourseService.addOrEditSchoolHomeCourse(cosSchoolHomeCourse);
        if (o.equals(ResultEntity.ResultStatus.OK.getMsg())) {
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, "", "添加/编辑首页展示课程成功"));
        } else {
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "添加/编辑首页展示课程时异常"));
        }
    }

    /**
     * 删除首页展示课程
     *
     * @param id
     * @return
     */
    @GetMapping("deleteSchoolHomeCourse")
    public Object deleteSchoolHomeCourse(String id) {
        Object o = cosSchoolHomeCourseService.deleteSchoolHomeCourse(id);
        if (o.equals(ResultEntity.ResultStatus.OK.getMsg())) {
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, "", "删除首页展示课程成功"));
        } else {
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "删除首页展示课程时异常"));
        }
    }

    /**
     * 显示首页课程展示列表
     * @param schoolId
     * @return
     */
    @GetMapping("showCmsSchoolHomeCourseList")
    Object showCmsSchoolHomeCourseList(String schoolId){
        try {
            Object o = cosSchoolHomeCourseService.showCmsSchoolHomeCourseList(schoolId);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, o, "显示首页课程展示列表列表成功"));
        }catch (Exception e){
            logger.error("showCmsSchoolHomeCourseList exception",e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "显示首页课程展示列表时异常"));
        }


    }


}
