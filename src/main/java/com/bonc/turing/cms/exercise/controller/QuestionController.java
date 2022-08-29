package com.bonc.turing.cms.exercise.controller;

import com.alibaba.fastjson.JSONObject;
import com.bonc.turing.cms.exercise.domain.QtChapter;
import com.bonc.turing.cms.exercise.domain.QtQuestion;
import com.bonc.turing.cms.exercise.dto.QtTextBookBindingDTO;
import com.bonc.turing.cms.exercise.service.QuestionService;
import com.bonc.turing.cms.manage.ResultEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping({"/question/"})
public class QuestionController {
    private static Logger logger = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    private QuestionService questionService;

    /**
     * @desc 创建题目
     * @auth lky
     * @date 2019/12/26 19:27
     * @param
     * @return
     */
    @RequestMapping("/creatAndUpdateQuestion")
    public Object creatAndUpdateQuestion(@RequestBody QtQuestion qtQuestion, @RequestParam("guid")String guid){

        try {
            Boolean flag = questionService.createAndUpdateQuestion(qtQuestion,guid);
            if (flag){
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK,"","创建成功"));
            }else {
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE,"","创建失败"));
            }
        } catch (Exception e) {
            logger.error("creatAndUpdateQuestion is failed",e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE,"","创建失败"));
        }
    }

    /**
     * @desc 绑定题目,绑定代码块(新建和更新)
     * @auth lky
     * @date 2019/12/26 19:27
     * @param
     * @return
     */
    @RequestMapping("/bindingQuestionAndTextBook")
    public Object bindingQuestionAndTextBook(@RequestBody JSONObject jsonObject){
        try {

            int i = questionService.bindingQuestionAndTextBook(jsonObject);
            if (1==i){
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK,"","绑定成功"));
            }else {
                logger.error("bindingQuestionAndTextBook is failed");
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE,"","不存在这种类型!"));
            }
        } catch (Exception e) {
            logger.error("bindingQuestionAndTextBook is failed",e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE,"","绑定失败"));
        }
    }



    /**
     * @desc 删除题目
     * @auth lky
     * @date 2019/12/26 19:34
     * @param
     * @return
     */
    @RequestMapping("/deleteQuestion")
    public Object deleteQuestion(@RequestBody JSONObject jsonObject){
        try {
            String questionId = jsonObject.getString("questionId");
            questionService.deleteQuestion(questionId);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK,"","删除成功"));
        } catch (Exception e) {
            logger.error("deleteQuestion is failed",e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE,"","删除失败"));
        }
    }



    /**
     * @desc 题库列表
     * @auth lky
     * @date 2019/12/26 19:52
     * @param
     * @return
     */
    @RequestMapping("/questionList")
    public Object questionList(@RequestBody JSONObject jsonObject){
        try {
            Integer type = jsonObject.getInteger("type");
            String keyWord = jsonObject.getString("keyWord");
            Integer pageNum = jsonObject.getInteger("pageNum");
            Integer pageSize = jsonObject.getInteger("pageSize");
            JSONObject jsonObject1 = questionService.questionList(type, keyWord, pageNum, pageSize);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK,jsonObject1,"查询成功"));
        } catch (Exception e) {
            logger.error("questionList is failed",e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE,"","查询失败"));
        }
    }


    /**
     * @desc 查询绑定信息
     * @auth lky
     * @date 2019/12/27 10:47
     * @param
     * @return
     */
    @RequestMapping("/getBindingMsg")
    public Object getBindingMsg(@RequestParam("textBookId")String textBookId){
        try {
            List<QtTextBookBindingDTO> bindingMsg = questionService.getBindingMsg(textBookId);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK,bindingMsg,"查询成功"));
        } catch (Exception e) {
            logger.error("getBindingMsg is failed",e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE,"","查询失败"));
        }

    }


    /**
     * @desc 反显题目
     * @auth lky
     * @date 2019/12/27 12:05
     * @param
     * @return
     */
    @RequestMapping("/getQuestion")
    public Object getQuestion(@RequestParam("qid")String qid){
        try {
            QtQuestion qtQuestion = questionService.getQuestion(qid);
            if (null!=qtQuestion){
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK,qtQuestion,"查询成功"));
            }else {
                logger.error("getQuestion not found question,qid:"+qid);
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE,"","没有该题目"));
            }
        } catch (Exception e) {
            logger.error("getQuestion is failed",e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE,"","查询失败"));
        }
    }


    /**
     * @desc 查询章节列表
     * @auth lky
     * @date 2019/12/27 12:11
     * @param
     * @return
     */
    @RequestMapping("/getChapterList")
    public Object getChapterList(@RequestParam("textBookId")String textBookId){
        try {
            List<QtChapter> chapterList = questionService.getChapterList(textBookId);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK,chapterList,"查询成功"));
        } catch (Exception e) {
            logger.error("getChapterList is failed",e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE,"","查询失败"));
        }
    }


    /**
     * @desc 改变题目状态
     * @auth lky
     * @date 2019/12/27 21:26
     * @param
     * @return
     */
    @RequestMapping("/changeQuestionStatus")
    public Object changeQuestionStatus(@RequestParam("status")Integer status, @RequestParam("qid")String qid){
        try {
            int a = questionService.changeQuestionStatus(qid,status);
            if(a>0){
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK,"","状态修改成功"));
            }else if (0==a){
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE,"","题目状态已改变,请刷新重试"));
            }else if (-1==a){
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE,"","没有该状态"));
            }else {
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE,"","未知异常"));
            }
        } catch (Exception e) {
            logger.error("changeQuestionStatus is failed",e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE,"","状态更改失败"));
        }

    }


    /**
     * @desc 题库教材部分的标签列表
     * @auth lky
     * @date 2019/12/28 12:44
     * @param
     * @return
     */
    @RequestMapping("/getExerciseList")
    public Object getExerciseList(){
        try {
            List<JSONObject> exerciseList = questionService.getExerciseList();
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK,exerciseList,"查询成功"));
        } catch (Exception e) {
            logger.error("getExerciseList is failed",e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE,"","查询失败"));
        }
    }


    /**
     * @desc 语言列表
     * @auth lky
     * @date 2019/12/28 21:00
     * @param
     * @return
     */
    @RequestMapping("/getLanguageList")
    public Object getLanguageList(){
        try{
            List<JSONObject> list = questionService.getLanguageList();
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK,list,"查询成功"));
        }catch (Exception e){
            logger.error("getLanguageList is failed",e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE,"","查询失败"));
        }
    }

}
