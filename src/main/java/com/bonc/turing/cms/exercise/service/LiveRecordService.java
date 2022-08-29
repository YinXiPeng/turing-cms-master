package com.bonc.turing.cms.exercise.service;

import com.alibaba.fastjson.JSONObject;

public interface LiveRecordService {
    JSONObject getLiveRecord(int type, String keyWord, int pageNum, int pageSize);

    void editLiveRecordRemark(JSONObject jsonObject);
}
