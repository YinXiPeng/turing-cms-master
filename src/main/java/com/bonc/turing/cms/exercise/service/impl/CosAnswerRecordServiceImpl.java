package com.bonc.turing.cms.exercise.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bonc.turing.cms.exercise.dao.mapper.CosAnswerRecordMapper;
import com.bonc.turing.cms.exercise.dao.repository.CosAnswerRecordRepository;
import com.bonc.turing.cms.exercise.dao.repository.CosPaperRepository;
import com.bonc.turing.cms.exercise.dao.repository.CosUserMsgRepository;
import com.bonc.turing.cms.exercise.dto.CosAnswerRecordsDTO;
import com.bonc.turing.cms.exercise.service.CosAnswerRecordService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


/**
 * @author lxm
 */
@Service
public class CosAnswerRecordServiceImpl implements CosAnswerRecordService {

    private CosAnswerRecordMapper cosAnswerRecordMapper;
    private CosAnswerRecordRepository cosAnswerRecordRepository;
    private CosUserMsgRepository cosUserMsgRepository;
    private CosPaperRepository cosPaperRepository;
    public CosAnswerRecordServiceImpl(CosAnswerRecordMapper cosAnswerRecordMapper,CosAnswerRecordRepository cosAnswerRecordRepository,
                                      CosUserMsgRepository cosUserMsgRepository,CosPaperRepository cosPaperRepository){
        this.cosAnswerRecordMapper = cosAnswerRecordMapper;
        this.cosAnswerRecordRepository = cosAnswerRecordRepository;
        this.cosUserMsgRepository = cosUserMsgRepository;
        this.cosPaperRepository = cosPaperRepository;

    }
    /**
     * 根据guid 校验身份 是否是老师
     * @param guid
     * @return
     */
    public boolean checkRoleByGuid(String guid){
        if (StringUtils.isEmpty(guid)) {
            return false;
        }else{
            return cosUserMsgRepository.findByGuid(guid).map(temp-> temp.getRole()==1?true:false).orElse(false);
        }

    }

    /**
     * 根据试卷查询答题
     * @param guid
     * @param paperId
     * @return
     */
    @Override
    public JSONObject getAnswerRecordByPaperId(String guid, String paperId,Integer pageSize,Integer pageNumber) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("content",new ArrayList<>());
        if(checkRoleByGuid(guid)){
            if (pageNumber == null){
                pageNumber = 1;
            }else{
                pageNumber = pageNumber+1;
            }
            if (pageSize == null){
                pageSize = 10;
            }
            // 查询已答题和已判卷的答题记录
            Integer[] statusArr = {2,3};
            PageHelper.startPage(pageNumber,pageSize);
            List<CosAnswerRecordsDTO> allCosAnswerRecord = cosAnswerRecordMapper.getAllCosAnswerRecord(paperId,statusArr);
            cosPaperRepository.findById(paperId).ifPresent(temp->{
                if (CollectionUtils.isNotEmpty(allCosAnswerRecord)){
                    PageInfo<List> pageInfo = new PageInfo(allCosAnswerRecord);
                    jsonObject.put("content",pageInfo.getList());
                    jsonObject.put("total",pageInfo.getTotal());
                }else{
                    jsonObject.put("content",new ArrayList<>());
                    jsonObject.put("total",0);
                }
                jsonObject.put("paperName",temp.getName());
                //暂时获取已答题的人数
            });
        }
        return  jsonObject;
    }

    /**
     * @param answerRecordId
     * @param guid
     * @return
     */
    @Override
    public JSONObject getAnswerRecordWithCorrectAnswer(String answerRecordId, String guid) throws Exception {
        if (!checkRoleByGuid(guid)){
            throw  new Exception("check user role error");
        }else{
            JSONObject data  = new JSONObject();
            cosAnswerRecordRepository.findById(answerRecordId).ifPresent(temp->{
                data.put("name",temp.getName());
                data.put("answerRecordId",temp.getId());
                data.put("choiceNum",temp.getChoiceNum());
                data.put("correctRate",temp.getCorrectRate());
                data.put("duration",(temp.getEndTime().getTime()-temp.getStartTime().getTime())/1000/60);
                data.put("score",temp.getScore());
                data.put("comment",temp.getComment());
                //反显答案
                data.put("questionAndAnswerList",JSON.parseArray(temp.getUserAndCorrectAnswerJson()));
            });
            return data;
        }
    }


    /**
     * 修改成绩和状态
     * @param answerRecordId
     * @param guid
     * @param score
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean updateScoreByAnswerRecordId(String answerRecordId, String guid, Float score,String comment) throws Exception {
      if (StringUtils.isNotEmpty(answerRecordId) && StringUtils.isNotEmpty(guid)){
          if (!checkRoleByGuid(guid)){
                throw  new Exception("check user role error");
          }else{
            int  effectRows = cosAnswerRecordMapper.updateScoreAndStatusByAnswerRecordId(score,answerRecordId,comment);
            if (effectRows == 1){
                return true;
            }else{
                throw new Exception("update score error ");
            }
          }
      }else{
          //参数传递错误
          throw new Exception("参数错误");
      }

    }
}
