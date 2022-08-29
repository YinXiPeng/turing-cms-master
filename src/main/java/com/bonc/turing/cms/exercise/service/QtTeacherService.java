package com.bonc.turing.cms.exercise.service;

import com.alibaba.fastjson.JSONObject;

public interface QtTeacherService {



    Object changeStatus(int isOnline, String guid);

    Object verifyTeacher(int isPass, String guid);

    Object editTeacherInfo(JSONObject params);

    Object showTeacherDetail(String guid);

    Object showPendingTeacherList(int pageNum, int pageSize, String state);

    Object showTeacherList(int pageNum, int pageSize);
}
