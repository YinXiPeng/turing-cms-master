package com.bonc.turing.cms.exercise.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bonc.turing.cms.exercise.dao.mapper.OrderMapper;
import com.bonc.turing.cms.exercise.service.LiveRecordService;
import com.bonc.turing.cms.manage.entity.OrderInfo;
import com.bonc.turing.cms.manage.entity.ProductInfo;
import com.bonc.turing.cms.manage.repository.ProductInfoRepository;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class LiveRecordServiceImpl implements LiveRecordService {

    @Resource
    private OrderMapper orderMapper;
    @Autowired
    private ProductInfoRepository productInfoRepository;

    @Override
    public JSONObject getLiveRecord(int type, String keyWord, int pageNum, int pageSize) {
        ProductInfo byProductName = productInfoRepository.findByProductName("1v1直播");
        JSONObject jsonObject = new JSONObject();
        String id = byProductName.getId();
        Page<Object> objects = PageHelper.startPage(pageNum, pageSize);
        if (1==type||2==type){
            //查询直播订单
            List<Map> list = orderMapper.getOrderByProductId(id,type,keyWord);
            List<Object> result = objects.getResult();
            long total = objects.getTotal();
            jsonObject.put("list",result);
            jsonObject.put("total",total);
        }else {
            //查询直播记录流水
            List<Map> list = orderMapper.getLiveRecord(keyWord);
            List<Object> result = objects.getResult();
            long total = objects.getTotal();
            ArrayList<Object> objects1 = new ArrayList<>();
            for (Object object:result){
                JSONObject jsonObject1 = JSONObject.parseObject(JSON.toJSONString(object));
                Object productId = jsonObject1.get("productId");
                if (null==productId){
                    //之前没关联的数据
                    jsonObject1.put("price","无");
                }else if (id.equals(productId)){
                    //购买的直播次数
                    double v = jsonObject1.getDouble("price") / jsonObject1.getInteger("buyLiveNum");
                    jsonObject1.put("price",String.valueOf(v));
                }else {
                    //购买教材赠送的次数
                    jsonObject1.put("price","0");
                }
                objects1.add(jsonObject1);
            }
            jsonObject.put("list",objects1);
            jsonObject.put("total",total);
        }
        return jsonObject;
    }

    @Override
    public void editLiveRecordRemark(JSONObject jsonObject) {
        String liveRecordId = jsonObject.getString("liveRecordId");
        String remarks = jsonObject.getString("remarks");
        Integer v = orderMapper.updateLiveRecordRemark(liveRecordId,remarks);
    }
}
