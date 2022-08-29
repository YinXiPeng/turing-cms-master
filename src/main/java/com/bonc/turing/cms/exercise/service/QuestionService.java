package com.bonc.turing.cms.exercise.service;

import com.alibaba.fastjson.JSONObject;
import com.bonc.turing.cms.exercise.domain.QtChapter;
import com.bonc.turing.cms.exercise.domain.QtQuestion;
import com.bonc.turing.cms.exercise.dto.QtTextBookBindingDTO;

import java.util.List;

public interface QuestionService {
    Boolean createAndUpdateQuestion(QtQuestion qtQuestion, String guid);

    int bindingQuestionAndTextBook(JSONObject jsonObject) throws CloneNotSupportedException;

    void deleteQuestion(String questionId);

    JSONObject questionList(Integer type, String keyWord, Integer pageNum, Integer pageSize);

    List<QtTextBookBindingDTO> getBindingMsg(String textBookId);

    QtQuestion getQuestion(String qid);

    List<QtChapter> getChapterList(String textBookId);

    int changeQuestionStatus(String qid, Integer status);

    List<JSONObject> getExerciseList();

    List<JSONObject> getLanguageList();
}
