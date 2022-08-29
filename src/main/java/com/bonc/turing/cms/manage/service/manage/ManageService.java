package com.bonc.turing.cms.manage.service.manage;

import com.alibaba.fastjson.JSONObject;
import com.bonc.turing.cms.manage.entity.OrderInfo;

import java.util.List;
import java.util.Map;

/**
 * @author lky
 * @date 2019/7/19 10:13
 */
public interface ManageService {

    Object queryProduct();

    Object queryProductPeopleList(JSONObject param);

    Object queryOrderList(JSONObject param);

    int changeState(String id, int type, int method, String guid);

    Map<String,Object> getNewsList(String keyWord, int pageNum, int pageSize);

    void updateRemark(JSONObject params,String guid);

    Map<String,Object> getSalonList(String keyWord, int pageNum, int pageSize);

    Map<String,Object> getModelList(String keyWord, int pageNum, int pageSize);
}
