package com.bonc.turing.cms.manage.controller.manage;

import com.alibaba.fastjson.JSONObject;
import com.bonc.turing.cms.manage.ResultEntity;
import com.bonc.turing.cms.manage.service.manage.ManageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @desc 模型,话题,新闻的首页管理部分
 * @author lky
 * @date 2019/7/19 18:41
 */
@RestController
@RequestMapping(value = "manage")
public class ManageController {
    private static Logger logger = LoggerFactory.getLogger(ManageController.class);

    @Autowired
    private ManageService manageService;

    /**
     * 模型,话题,资源的状态,置顶,加精
     * @param specialId
     * @param type id的类型(0模型(存的是parentId),1话题,2新闻,3数据集(存的是history_version_id),4资源)
     * @param method
     * @param guid
     * @return
     */
    @RequestMapping(value = "changeState")
    public Object changeState(@RequestParam(value = "specialId")String id,@RequestParam(value = "type")int type,@RequestParam(value = "method")int method,@RequestParam(value = "guid")String guid){
        try {
            logger.info("changeState开始执行==>id:{},type:{},method:{},guid:{}",id,type,method,guid);
            int i = manageService.changeState(id, type, method, guid);
            if (0==i){
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, "","执行成功！"));
            }else {
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "","父id字段为空,无法进行操作! "));
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("changeState执行失败==>id:{},type:{},method:{},guid:{}",id,type,method,guid);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "","执行失败！"));
        }
    }

    /**
     * @desc 首页管理中获取新闻,模型,话题的列表
     * @auth lky
     * @date 2019/7/31 10:28
     * @param
     * @return
     */
    @RequestMapping(value = "getList")
    public Object getList(@RequestParam(value = "type")int type,@RequestParam(value = "keyWord",required = false,defaultValue = "")String keyWord,@RequestParam(value = "pageSize",required = false,defaultValue = "30")int pageSize,@RequestParam(value = "pageNum",required = false,defaultValue = "0")int pageNum){
        Map<String, Object> resultList = new HashMap<String, Object>();
        if (0==type){
            //模型
            resultList = manageService.getModelList(keyWord, pageNum, pageSize);
        }else if (1==type){
            //沙龙
            resultList = manageService.getSalonList(keyWord,pageNum,pageSize);
        }else if(2==type){
            //新闻
            resultList = manageService.getNewsList(keyWord,pageNum,pageSize);
        }
        return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, resultList,"查询成功！"));
    }


    /**
     * @desc 改变新闻,话题,模型的管理员标签
     * @auth lky
     * @date 2019/7/31 10:28
     * @param
     * @return
     */
    @RequestMapping(value = "updateRemark",method = RequestMethod.POST)
    private Object updateRemark(@RequestBody JSONObject params,@RequestParam(value = "guid")String guid){
        try {
            String type = params.getString("type");
            String discussId = params.getString("primaryId");
            String remark = params.getString("remark");
            if (null==discussId||"".equals(discussId.trim())||null==remark||null==type||"".equals(type.trim())){
                logger.error("参数不能为空");
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "","参数不能为空"));
            }
            manageService.updateRemark(params,guid);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("添加标签失败 primaryId ==> {},type==>{}",params.getString("primaryId").toString(),params.getString("type"));
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, e.getMessage()));
        }
        return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, ""));
    }


}
