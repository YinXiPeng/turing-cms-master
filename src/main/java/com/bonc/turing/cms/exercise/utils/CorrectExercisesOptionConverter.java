package com.bonc.turing.cms.exercise.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import javax.persistence.AttributeConverter;
import java.util.ArrayList;
import java.util.List;


public class CorrectExercisesOptionConverter implements AttributeConverter<JSONArray, String> {

    @Override
    public String convertToDatabaseColumn(JSONArray attribute) {
        return JSON.toJSONString(attribute);
    }

    @Override
    public JSONArray convertToEntityAttribute(String dbData) {
        return JSONObject.parseObject(dbData, new TypeReference<JSONArray>() {
        });
    }
}
