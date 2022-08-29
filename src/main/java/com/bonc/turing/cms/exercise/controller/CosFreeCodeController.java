package com.bonc.turing.cms.exercise.controller;

import com.bonc.turing.cms.exercise.service.CosFreeCodeService;
import com.bonc.turing.cms.manage.ResultEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Author yh
 * @Description 后台-课程兑换码
 * @Date  2020/4/17 17:58
 **/
@RestController
@RequestMapping(value = "cosFreeCode")
public class CosFreeCodeController {

    private static Logger logger = LoggerFactory.getLogger(CosFreeCodeController.class);

    @Autowired
    private CosFreeCodeService cosFreeCodeService;


    /**
     * @Author yh
     * @Description 生成课程兑换码
     * @Date  2020/4/17 18:02
     * @param courseId
     * @param num
     **/
    @RequestMapping(value = "createCosFreeCode",method = RequestMethod.GET)
    public Object createCosFreeCode(String courseId, Integer num){
        try {
            Object o = cosFreeCodeService.createFreeCode(courseId,num);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, o, "生成课程兑换码成功"));
        }catch (Exception e){
            e.printStackTrace();
            logger.error("createCosFreeCode exception{}",e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE,"","生成课程兑换码失败"));
        }
    }

    /**
     * @Author yh
     * @Description 查看课程兑换码
     * @Date  2020/4/17 18:02
     * @param courseId
     **/
    @RequestMapping(value = "showFreeCodeByCourseId",method = RequestMethod.GET)
    public Object showFreeCodeByCourseId(String courseId){
        try {
            Object o = cosFreeCodeService.showFreeCodeByCourseId(courseId);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, o, "查看课程兑换码成功"));
        }catch (Exception e){
            e.printStackTrace();
            logger.error("showFreeCodeByCourseId exception{}",e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE,"","查看课程兑换码失败"));
        }
    }
}
