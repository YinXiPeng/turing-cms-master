package com.bonc.turing.cms.exercise.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bonc.turing.cms.exercise.dao.mapper.QtTextBookBindingMapper;
import com.bonc.turing.cms.exercise.dao.repository.*;
import com.bonc.turing.cms.exercise.domain.*;
import com.bonc.turing.cms.exercise.dto.QtTextBookBindingDTO;
import com.bonc.turing.cms.exercise.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QtQuestionRepository qtQuestionRepository;

    @Autowired
    private QtTextBookBindingRepository qtTextBookBindingRepository;
    @Autowired
    private QtTextBookBindingMapper qtTextBookBindingMapper;
    @Autowired
    private QtChapterRepository qtChapterRepository;
    @Autowired
    private QtAnswerRecordRepository qtAnswerRecordRepository;
    @Autowired
    private QtTextBookPagingRepository qtTextBookPagingRepository;

    @Override
    @Transactional
    public Boolean createAndUpdateQuestion(QtQuestion qtQuestion, String guid) {
        String qid = qtQuestion.getQid();
        if (null==qid||"".equals(qid)){
            qtQuestion.setCreatedTime(new Date());
            qtQuestion.setLastModifyTime(new Date());
            qtQuestion.setCreatedGuid(guid);
        }else {
            Optional<QtQuestion> byId = qtQuestionRepository.findById(qid);
            if (byId.isPresent()){
                QtQuestion qtQuestion1 = byId.get();
                qtQuestion.setCreatedGuid(qtQuestion1.getCreatedGuid());
                qtQuestion.setCreatedTime(qtQuestion1.getCreatedTime());
                qtQuestion.setLastModifyTime(new Date());
                qtQuestion.setAnswerNum(qtQuestion1.getAnswerNum());
                qtQuestion.setCodeNum(qtQuestion1.getCodeNum());
                qtQuestion.setCommentNum(qtQuestion1.getCommentNum());
            }else {
                return false;
            }
        }
        QtQuestion save = qtQuestionRepository.save(qtQuestion);
        return true;
    }

    @Transactional
    @Override
    public int bindingQuestionAndTextBook(JSONObject jsonObject) {
        Integer type = jsonObject.getInteger("type");
        String textBookId = jsonObject.getString("textBookId");
        List<QtTextBookPaging> pagingList = qtTextBookPagingRepository.findByTextBookIdOrderBySortAscBeginPageAscEndPageAsc(textBookId);
        //本教材该分类的所有绑定都删掉
        for (QtTextBookPaging qtTextBookPaging:pagingList){
            qtTextBookBindingRepository.deleteByTypeAndPagingId(type,qtTextBookPaging.getId());
        }
        JSONArray bindingList = jsonObject.getJSONArray("bindingList");
        for (Object o:bindingList){
            QtTextBookBindingDTO qtTextBookBindingDTO = JSONObject.parseObject(JSON.toJSONString(o), QtTextBookBindingDTO.class);
            QtTextBookPaging paging = qtTextBookPagingRepository.findByTextBookIdAndChapterIdAndBeginPageAndEndPage(qtTextBookBindingDTO.getTextBookId(), qtTextBookBindingDTO.getChapterId(), qtTextBookBindingDTO.getBeginPage(), qtTextBookBindingDTO.getEndPage());
            if (null==paging){
                //没有,存一条
                QtTextBookPaging qtTextBookPaging = new QtTextBookPaging();
                qtTextBookPaging.setBeginPage(qtTextBookBindingDTO.getBeginPage());
                qtTextBookPaging.setChapterId(qtTextBookBindingDTO.getChapterId());
                qtTextBookPaging.setEndPage(qtTextBookBindingDTO.getEndPage());
                qtTextBookPaging.setTextBookId(qtTextBookBindingDTO.getTextBookId());
                qtTextBookPaging.setSort(qtTextBookBindingDTO.getSort());
                paging = qtTextBookPagingRepository.save(qtTextBookPaging);
            }
            if (1==type){
                //绑定题库
                List<String> titleNumbers = qtTextBookBindingDTO.getTitleNumbers();
                for (String i:titleNumbers){
                    QtTextBookBinding qtTextBookBinding = JSONObject.parseObject(JSON.toJSONString(o), QtTextBookBinding.class);
                    qtTextBookBinding.setTitleNumber(i);
                    qtTextBookBinding.setPagingId(paging.getId());
                    qtTextBookBindingRepository.save(qtTextBookBinding);
                }
            }else if (2==type){
                //绑定代码块
                QtTextBookBinding qtTextBookBinding = JSONObject.parseObject(JSON.toJSONString(o), QtTextBookBinding.class);
                qtTextBookBinding.setPagingId(paging.getId());
                qtTextBookBindingRepository.save(qtTextBookBinding);
            }else {
                return 2;
            }
        }
        return 1;
    }


    @Override
    @Transactional
    public void deleteQuestion(String questionId) {
            qtTextBookBindingRepository.deleteByTitleNumberAndType(questionId,1);
            qtAnswerRecordRepository.deleteByQuestionId(questionId);
            qtQuestionRepository.deleteById(questionId);
    }

    @Override
    public JSONObject questionList(Integer type, String keyWord,Integer pageNum,Integer pageSize) {
        PageRequest pageable = PageRequest.of(pageNum, pageSize);
        Specification<QtQuestion> spec = new Specification<QtQuestion>() {
            @Override
            public Predicate toPredicate(Root<QtQuestion> root, CriteriaQuery<?> criteriaQuery,
                                         CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(criteriaBuilder.equal(root.get("questionType"), type));
                if (null!=keyWord&&!"".equals(keyWord)) {
                    predicates.add(criteriaBuilder.or(criteriaBuilder.like(root.get("qid"), "%"+keyWord+"%"),criteriaBuilder.like(root.get("questionName"), "%"+keyWord+"%"),criteriaBuilder.like(root.get("questionDescription"), "%"+keyWord+"%")));
                }
                Predicate[] p = new Predicate[predicates.size()];
                return criteriaBuilder.and(predicates.toArray(p));
            }
        };
        Page<QtQuestion> pageResult = qtQuestionRepository.findAll(spec, pageable);
        List<QtQuestion> content = pageResult.getContent();
        long totalElements = pageResult.getTotalElements();
        ArrayList<Object> objects = new ArrayList<>();
        for (QtQuestion qtQuestion:content){
            List<Map> byQuestionId = qtTextBookBindingMapper.findByQuestionId(qtQuestion.getQid());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("textBook",byQuestionId);
            jsonObject.put("question",qtQuestion);
            objects.add(jsonObject);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total",totalElements);
        jsonObject.put("questions",objects);
        return jsonObject;
    }

    @Override
    public List<QtTextBookBindingDTO> getBindingMsg(String textBookId) {
        List<QtTextBookPaging> pagingList = qtTextBookPagingRepository.findByTextBookIdOrderBySortAscBeginPageAscEndPageAsc(textBookId);
        ArrayList<QtTextBookBindingDTO> qtTextBookBindingDTOS = new ArrayList<>();
        for (QtTextBookPaging qtTextBookPaging:pagingList){
            List<QtTextBookBinding> questions = qtTextBookBindingRepository.findByPagingIdAndType(qtTextBookPaging.getId(),1);
            List<QtTextBookBinding> codes = qtTextBookBindingRepository.findByPagingIdAndType(qtTextBookPaging.getId(),2);
            if (0==questions.size()&&0==codes.size()){
                //该页中没有题目和代码,不放进list
                continue;
            }
            QtTextBookBindingDTO qtTextBookBindingDTO = JSONObject.parseObject(JSON.toJSONString(qtTextBookPaging), QtTextBookBindingDTO.class);
            ArrayList<String> strings = new ArrayList<>();
            for (QtTextBookBinding qtTextBookBinding:questions){
                strings.add(qtTextBookBinding.getTitleNumber());
            }
            qtTextBookBindingDTO.setTitleNumbers(strings);
            qtTextBookBindingDTO.setCodeList(codes);
            qtTextBookBindingDTOS.add(qtTextBookBindingDTO);
        }
        return qtTextBookBindingDTOS;
    }

    @Override
    public QtQuestion getQuestion(String qid) {
        Optional<QtQuestion> byId = qtQuestionRepository.findById(qid);
        if (byId.isPresent()){
            QtQuestion qtQuestion = byId.get();
            return qtQuestion;
        }else {
            return null;
        }
    }

    @Override
    public List<QtChapter> getChapterList(String textBookId) {
        List<QtChapter> allByTextBookId = qtChapterRepository.findAllByTextBookId(textBookId);
        return allByTextBookId;
    }

    @Override
    @Transactional
    public int changeQuestionStatus(String qid,Integer status) {
        if (1==status||0==status){
            return qtQuestionRepository.updateQuestionStatus(qid,status);
        }else if (2==status){
            qtTextBookBindingRepository.deleteByTitleNumberAndType(qid,1);
            qtAnswerRecordRepository.deleteByQuestionId(qid);
            qtQuestionRepository.deleteById(qid);
            return 1;
        }else {
            return -1;
        }
    }

    @Override
    public List<JSONObject> getExerciseList() {
        List<JSONObject> exerciseList = qtTextBookBindingMapper.getProjectListByDictionaryName("exercise方向");
        for (JSONObject jsonObject:exerciseList){
            List<JSONObject> list = qtTextBookBindingMapper.getProjectListByParentId(jsonObject.getString("id"));
            jsonObject.put("list",list);
        }
        return exerciseList;
    }

    @Override
    public List<JSONObject> getLanguageList() {
        List<JSONObject> exerciseList = qtTextBookBindingMapper.getProjectListByDictionaryName("编程语言");
        return exerciseList;
    }

}
