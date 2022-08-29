package com.bonc.turing.cms.exercise.service;

import com.alibaba.fastjson.JSONObject;

/**
 * @author lxm
 */
public interface CosAnswerRecordService {

    /**
     * 教师获取答题列表
     * @param guid
     * @param paperId
     * @param pageNmuber
     * @param pageSize
     * @return
     * @throws Exception paperId 不存在
     */
    JSONObject getAnswerRecordByPaperId(String guid, String paperId,Integer pageSize,Integer pageNmuber) throws Exception;

    /**
     * 根据答题记录获取带正确答案的试卷
     * 需要校验guid
     * @param answerRecordId
     * @param guid
     * @throws Exception 身份校验
     * @return
     */
    JSONObject getAnswerRecordWithCorrectAnswer(String answerRecordId, String guid) throws Exception;

    /**
     * 根据答题记录更新成绩
     * @param answerRecordId
     * @param guid
     * @param score
     * @param remark
     * @throws Exception 身份校验
     * @return
     */
    Boolean updateScoreByAnswerRecordId(String answerRecordId, String guid, Float score,String remark) throws Exception;
}
