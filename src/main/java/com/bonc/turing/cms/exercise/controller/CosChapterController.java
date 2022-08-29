package com.bonc.turing.cms.exercise.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bonc.turing.cms.exercise.service.CosChapterService;
import com.bonc.turing.cms.manage.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/chapter")
@RestController
@Slf4j
public class CosChapterController {

    @Autowired
    private CosChapterService cosChapterService;

    @RequestMapping("/chapterAdd")
    public Object chapterAdd(@RequestBody JSONObject jsonObject){
        try {
            cosChapterService.addChapter(jsonObject);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK,"","新增成功"));
        } catch (Exception e) {
            log.error("chapterAdd is failed", e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "新增失败"));
        }
    }

    @RequestMapping("/chapterInfo")
    public Object chapterInfo(String courseId){
        try {
            JSONArray ja = cosChapterService.getChapterInfo(courseId);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK,ja,"查看成功"));
        } catch (Exception e) {
            log.error("chapterInfo is failed", e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "查看失败"));
        }
    }

    @RequestMapping("/chapterPreview")
    public Object chapterPreview(String courseId){
        try {
            JSONObject jo = cosChapterService.chapterPreview(courseId);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK,jo,"预览成功"));
        } catch (Exception e) {
            log.error("chapterPreview is failed", e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "预览失败"));
        }
    }

    @RequestMapping("/sectionInfo")
    public Object sectionInfo(String sectionId){
        try {
            JSONObject jo = cosChapterService.sectionInfo(sectionId);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, jo,"查看成功"));
        } catch (Exception e) {
            log.error("sectionInfo is failed", e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "查看失败"));
        }
    }

    @RequestMapping("/costSetPage")
    public Object costSetPage(String courseId){
        try {
            JSONObject jo = cosChapterService.chapterAndSectionList(courseId);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, jo,"查看成功"));
        } catch (Exception e) {
            log.error("costSetPage is failed", e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "查看失败"));
        }
    }

    @RequestMapping("/updateCoursePayment")
    public Object updateCoursePayment(@RequestBody JSONObject jo){
        try {
            cosChapterService.updateCoursePayment(jo);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, "","设置成功"));
        } catch (Exception e) {
            log.error("updateCoursePayment is failed", e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "设置失败"));
        }
    }


    @RequestMapping("/updateChapterInfo")
    public Object updateChapterInfo(@RequestBody JSONObject jsonObject){
        try {
            cosChapterService.updateChapterInfo(jsonObject);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK,"","更新成功"));
        } catch (Exception e) {
            log.error("updateChapterInfo is failed", e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "更新失败"));
        }
    }
}
