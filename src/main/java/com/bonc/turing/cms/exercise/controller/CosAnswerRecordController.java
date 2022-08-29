package com.bonc.turing.cms.exercise.controller;

import com.bonc.turing.cms.exercise.service.CosAnswerRecordService;
import com.bonc.turing.cms.manage.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 查看学生答题记录等相关操作
 * @author lxm
 */
@Slf4j
@RestController
@RequestMapping("/cosAnswerRecord")
public class CosAnswerRecordController {

    private  CosAnswerRecordService cosAnswerRecordService;
    private  CosAnswerRecordController(CosAnswerRecordService cosAnswerRecordService){
        this.cosAnswerRecordService = cosAnswerRecordService;
    }

    /**
     * 根据 试卷id 查看所有已考试学生列表
     * @param teacherId
     * @param paperId
     * @return
     */
    @GetMapping("/answerRecords")
    public ResponseEntity getUserList(String paperId,Integer pageSize,Integer pageNumber,String teacherId){
        if (StringUtils.isEmpty(teacherId) || StringUtils.isEmpty(paperId)){
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE,"","参数传递错误"));
        }else{
            try {
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK,cosAnswerRecordService.getAnswerRecordByPaperId(teacherId,paperId,pageSize,pageNumber)));
            }catch (Exception e){
                log.error("get user answerRecord error",e);
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE,"","无权限或内部异常"));
            }
        }
    }
    /**
     * 教师 查看用户答题
     * 顺序显示题目
     * @param answerRecordId
     * @return
     */
    @GetMapping("/checkAnswer")
    public ResponseEntity getAnswerRecordWithCorrectAnswer(String answerRecordId,String teacherId) {
        if (StringUtils.isEmpty(teacherId) || StringUtils.isEmpty(answerRecordId)) {
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "参数传递错误"));
        } else {
            try {
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, cosAnswerRecordService.getAnswerRecordWithCorrectAnswer(answerRecordId, teacherId)));
            } catch (Exception e) {
                log.error("teacher check answer error", e);
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "无权限或内部异常"));
            }
        }
    }

    /**
     * 根据答题记录更新成绩
     * @param answerRecordId
     * @param teacherId
     * @param score
     * @return
     */
    @GetMapping("/score")
    public  ResponseEntity updateScoreByAnswerRecordId(String answerRecordId,String teacherId,Float score,String comment) {
        if (StringUtils.isEmpty(teacherId) || StringUtils.isEmpty(answerRecordId) || StringUtils.isEmpty(comment)) {
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "参数传递错误"));
        } else {
            try {
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, cosAnswerRecordService.updateScoreByAnswerRecordId(answerRecordId, teacherId, score,comment)));
            } catch (Exception e) {
                log.error("teacher check answer error", e);
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, false, "无权限或内部异常"));
            }
        }
    }

}
