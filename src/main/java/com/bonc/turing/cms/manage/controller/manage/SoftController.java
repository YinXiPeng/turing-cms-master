package com.bonc.turing.cms.manage.controller.manage;

import com.alibaba.fastjson.JSONObject;
import com.bonc.turing.cms.manage.ResultEntity;
import com.bonc.turing.cms.manage.entity.OrderInfo;
import com.bonc.turing.cms.manage.service.manage.ManageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @desc 后台的软件管理部分
 * @author lky
 * @date 2019/7/19 9:55
 */
@RestController
@RequestMapping(value = "/soft")
public class SoftController {

    private static Logger logger = LoggerFactory.getLogger(SoftController.class);

    @Autowired
    private ManageService manageService;

    /**
     * @desc 查询产品列表
     * @auth lky
     * @date 2019/7/31 10:29
     * @param
     * @return
     */
    @RequestMapping(value = "queryProduct",method = RequestMethod.GET)
    public Object queryProduct(){
        logger.info("begin to request queryProduct interface");
        Object o = manageService.queryProduct();
        if(null != o){
            logger.info("get queryProduct success!");
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, o,"查询商品信息成功！"));
        }
        return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, o,"未找到商品！"));
    }

    /**
     * @desc 查询试用列表
     * @auth lky
     * @date 2019/7/31 10:30
     * @param
     * @return
     */
    @RequestMapping(value = "queryProductPeopleList",method = RequestMethod.POST)
    @ResponseBody
    public Object queryProductPeopleList(@RequestBody(required = true)JSONObject params){
        if (null==params.getString("productId")||"".equals(params.getString("productId").trim())){
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "","未找到商品！"));
        }
        Object o = manageService.queryProductPeopleList(params);
        return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, o,"查询列表成功！"));
    }

    /**
     * @desc 查询订单列表
     * @auth lky
     * @date 2019/7/31 10:30
     * @param
     * @return
     */
    @RequestMapping(value = "queryOrderList",method = RequestMethod.POST)
    @ResponseBody
    public Object queryOrderList(@RequestBody(required = true)JSONObject params){
        if (null==params.getString("productId")||"".equals(params.getString("productId").trim())){
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "","未找到商品！"));
        }
        Object orderInfos = manageService.queryOrderList(params);
        return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, orderInfos,"查询列表成功！"));
    }
}
