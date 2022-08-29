package com.bonc.turing.cms.exercise.service;

import com.alibaba.fastjson.JSONObject;

public interface TextBookRecordService {
    JSONObject textBook(Integer pageNum, Integer pageSize, String textBookId, String keyWord, Integer type);

    void editRemarks(JSONObject jsonObject);

    void deleteOrder(JSONObject jsonObject);
}
