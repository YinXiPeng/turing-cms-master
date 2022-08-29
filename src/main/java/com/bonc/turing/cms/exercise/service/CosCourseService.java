package com.bonc.turing.cms.exercise.service;

import com.alibaba.fastjson.JSONObject;
import com.bonc.turing.cms.exercise.domain.CosCourse;

public interface CosCourseService {

    Object createOrEditCourse(CosCourse course);

    Object deleteCourse(String courseId);

    Object showCourse(String courseId);

    Object changeStatus(String courseId,int operateType);

    Object cmsCourseList(JSONObject params);

    JSONObject getStudentCommit(String guid, String schoolCourseId, int pageNum, int pageSize);

    void saveScore(String id, int score);

    void displayOrHideCourse(String schoolCourseId, int state);

}
