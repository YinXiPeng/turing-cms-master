package com.bonc.turing.cms.exercise.dao.mapper;

import com.alibaba.fastjson.JSONObject;
import com.bonc.turing.cms.exercise.domain.QtTextBookPaging;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author lky
 * @desc
 * @date 2019/12/26 21:26
 */
@Mapper
public interface QtTextBookBindingMapper {

    List<Map> findByQuestionId(@Param("qid") String qid);

    List<JSONObject> getProjectListByDictionaryName(@Param("dictionaryName") String dictionaryName);

    List<JSONObject> getProjectListByParentId(@Param("parentId") String parentId);

    List<JSONObject> getQuestionByTextBookId(@Param("guid") String guid, @Param("id") Integer id);

    List<QtTextBookPaging> findPagingListByTextBookIdAndIsFree(@Param("textBookId") String textBookId);
}
