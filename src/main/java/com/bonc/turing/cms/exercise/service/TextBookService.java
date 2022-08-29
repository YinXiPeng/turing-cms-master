package com.bonc.turing.cms.exercise.service;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;


public interface TextBookService {

    Object createOrEditTextBook(JSONObject params, HttpServletRequest request);

    Object createOrEditChapter(JSONObject params);

    Object showBackStageTextBook(HttpServletRequest request);

    Object showBackStageTextBookList(JSONObject params);

    Object showVideoCourseList(JSONObject params);

    Object showBackStageChapter(HttpServletRequest request);

    Object setTextBookPrice(JSONObject params);

    Object changeBookStatus(JSONObject params);

    Object getTeacherList();

    Object chapterTotalNum(HttpServletRequest request);

    Object checkDuplicate(HttpServletRequest request);

    Object bookList();
}
