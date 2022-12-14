package com.bonc.turing.cms.exercise.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import javax.persistence.AttributeConverter;
import java.util.ArrayList;
import java.util.List;

/**
 * description:
 *
 * @author lxh
 * @date 2020/3/23 19:27
 */
public class CorrectOptionConverter implements AttributeConverter<List<String>, String> {
    /**
     * Converts the value stored in the entity attribute into the
     * data representation to be stored in the database.
     *
     * @param attribute the entity attribute value to be converted
     * @return the converted data to be stored in the database column
     */
    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        return JSON.toJSONString(attribute);
    }

    /**
     * Converts the data stored in the database column into the
     * value to be stored in the entity attribute.
     * Note that it is the responsibility of the converter writer to
     * specify the correct dbData type for the corresponding column
     * for use by the JDBC driver: i.e., persistence providers are
     * not expected to do such type conversion.
     *
     * @param dbData the data from the database column to be converted
     * @return the converted value to be stored in the entity attribute
     */
    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        return JSONObject.parseObject(dbData, new TypeReference<ArrayList<String>>() {
        });
    }
}
