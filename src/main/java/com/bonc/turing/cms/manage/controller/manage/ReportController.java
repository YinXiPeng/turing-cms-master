package com.bonc.turing.cms.manage.controller.manage;

import com.bonc.turing.cms.manage.ResultEntity;
import com.bonc.turing.cms.manage.service.manage.ReportService;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author lky
 * @desc 举报列表及审核部分
 * @date 2019/8/7 14:44
 */
@RestController
@RequestMapping("/report")
public class ReportController {

    private static Logger logger = LoggerFactory.getLogger(ReportController.class);

    @Autowired
    private ReportService reportService;

    /**
     * @desc 获取举报列表
     * @auth lky
     * @date 2019/8/7 20:35
     * @param
     * @return
     */
    @RequestMapping("/reportList")
    public Object getReportList(@RequestParam(value = "pageNum",required = false,defaultValue = "-1")int pageNum,@RequestParam(value = "pageSize",required = false,defaultValue = "-1")int pageSize){
        try {
            logger.info("/reportList 开始执行");
            Map<String,Object> reportList = reportService.getReportList(pageNum,pageSize);
            logger.info("/reportList 执行成功");
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, reportList,"执行成功！"));
        } catch (Exception e) {
            logger.error("/reportList 执行失败",e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "","执行失败！"));
        }
    }


    /**
     * @desc 改变举报的状态,做了防线程并发的处理
     * @auth lky
     * @date 2019/8/7 20:31
     * @param
     * @return
     */
    @RequestMapping(value = "/changeReportStatus",method = RequestMethod.POST)
    public Object changeReportStatus(@RequestParam(value = "guid")String guid, @RequestParam(value = "reportType")String type, @RequestParam(value = "reportId")String reportId, @Param(value = "status")int status){
        try {
            logger.info("/changeReportStatus 开始执行");
            int i = reportService.changeReportStatus(guid, type, reportId,status);
            if (1==i){
                logger.info("/changeReportStatus 执行成功");
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, "","执行成功！"));
            }
            if (2==i){
                logger.info("/changeReportStatus 执行失败,状态已改变,请刷新重试！");
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "","状态已改变,请刷新重试！"));
            }
            if (-1==i){
                logger.info("/changeReportStatus 执行失败,参数错误！");
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "","参数错误！"));
            }
            logger.info("/changeReportStatus 执行失败,未知错误！");
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "","未知错误！"));

        } catch (Exception e) {
            logger.error("/changeReportStatus 执行失败",e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "","执行失败！"));
        }
    }
}
