package com.bonc.turing.cms.exercise.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bonc.turing.cms.exercise.dao.mapper.OrderMapper;
import com.bonc.turing.cms.exercise.service.TextBookRecordService;
import com.bonc.turing.cms.manage.entity.OrderInfo;
import com.bonc.turing.cms.manage.repository.OrderInfoRepository;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TextBookRecordServiceImpl implements TextBookRecordService {

    @Resource
    private OrderMapper orderMapper;
    @Autowired
    private OrderInfoRepository orderInfoRepository;

    @Override
    public JSONObject textBook(Integer pageNum, Integer pageSize, String textBookId, String keyWord, Integer type) {
        Page<Object> objects = PageHelper.startPage(pageNum, pageSize);
        List<Map> list = orderMapper.getTextBookOrder(textBookId,keyWord,type);
        List<Object> result = objects.getResult();
        long total = objects.getTotal();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("orderList",result);
        jsonObject.put("total",total);
        return jsonObject;
    }

    @Override
    @Transactional
    public void editRemarks(JSONObject jsonObject) {
        String orderId = jsonObject.getString("orderId");
        Optional<OrderInfo> byId = orderInfoRepository.findById(orderId);
        OrderInfo orderInfo = byId.get();
        orderInfo.setRemarks(jsonObject.getString("remarks"));
        orderInfoRepository.save(orderInfo);
    }

    @Override
    @Transactional
    public void deleteOrder(JSONObject jsonObject) {
        JSONArray orderList = jsonObject.getJSONArray("orderList");
        for (Object object:orderList){
            orderInfoRepository.deleteById(object.toString());
        }
    }
}
