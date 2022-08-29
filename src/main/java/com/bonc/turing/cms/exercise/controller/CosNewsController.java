package com.bonc.turing.cms.exercise.controller;

import com.bonc.turing.cms.exercise.domain.CosNews;
import com.bonc.turing.cms.exercise.service.CosNewsService;
import com.bonc.turing.cms.manage.ResultEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 高校首页-资讯相关
 */
@RestController
@RequestMapping(value = "cosNews")
public class CosNewsController {
    private static Logger logger = LoggerFactory.getLogger(CosNewsController.class);

    @Autowired
    private CosNewsService cosNewsService;

    /**
     * 创建/编辑资讯
     *
     * @param cosNews
     * @return
     */
    @PostMapping("addOrEditNews")
    public Object addOrEditNews(@RequestBody CosNews cosNews) {
        Object o = cosNewsService.addOrEditNews(cosNews);
        if (o.equals(ResultEntity.ResultStatus.OK.getMsg())) {
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, "", "创建或编辑资讯成功"));
        } else {
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "创建或编辑资讯失败"));
        }
    }

    /**
     * 删除资讯
     *
     * @param id
     * @return
     */
    @GetMapping("deleteNewsById")
    public Object deleteNewsById(String id) {
        Object o = cosNewsService.deleteNewsById(id);
        if (o.equals(ResultEntity.ResultStatus.OK.getMsg())) {
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, "", "删除资讯成功"));
        } else {
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "删除资讯失败"));
        }
    }

    /**
     * 查询资讯详情
     *
     * @param id
     * @return
     */
    @GetMapping("findNewsById")
    public Object findNewsById(String id) {
        try {
            Object o = cosNewsService.findNewsById(id);
            if (o.equals(ResultEntity.ResultStatus.FAILURE.getMsg())) {
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "查询资讯详情失败"));
            } else {
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, o, "查询资讯详情成功"));
            }
        } catch (Exception e) {
            logger.error("findNewsById exception", e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "查询资讯详情时异常"));
        }

    }

    /**
     * 隐藏/显示
     *
     * @param id
     * @param isHide
     * @return
     */
    @GetMapping("changeNewsStatus")
    public Object changeNewsStatus(String id, Integer isHide) {
        try {
            Object o = cosNewsService.changeNewsStatus(id, isHide);
            if (o.equals(ResultEntity.ResultStatus.OK.getMsg())) {
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, "", "隐藏/显示成功"));
            } else {
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "隐藏/显示失败"));
            }
        } catch (Exception e) {
            logger.error("changeNewsStatus exception", e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "隐藏/显示时异常"));
        }
    }

    /**
     * 后台资讯列表
     * @param schoolId
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @GetMapping("showCmsNewsList")
    public Object showCmsNewsList(String schoolId,Integer newsType,Integer pageNumber,Integer pageSize) {
        try {
            Object o = cosNewsService.showCmsNewsList(schoolId,newsType,pageNumber,pageSize);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, o, "查询后台资讯列表成功"));
        } catch (Exception e) {
            logger.error("showCmsNewsList exception", e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "查询后台资讯列表时异常"));
        }
    }

}
