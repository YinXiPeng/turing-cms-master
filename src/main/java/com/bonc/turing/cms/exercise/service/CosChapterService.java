package com.bonc.turing.cms.exercise.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public interface CosChapterService {

    void addChapter(JSONObject jo);

    JSONArray getChapterInfo(String courseId);

    void updateChapterInfo(JSONObject jo);

    JSONObject chapterPreview(String courseId);

    JSONObject sectionInfo(String sectionId);

    JSONObject chapterAndSectionList(String courseId);

    void updateCoursePayment(JSONObject jo);

}
