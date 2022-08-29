package com.bonc.turing.cms.exercise.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bonc.turing.cms.common.utils.HttpUtil;
import com.bonc.turing.cms.exercise.dao.repository.*;
import com.bonc.turing.cms.exercise.domain.*;
import com.bonc.turing.cms.exercise.service.CosChapterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
public class CosChapterServiceImpl implements CosChapterService {

    @Autowired
    private CosChapterRepository cosChapterRepository;
    @Autowired
    private CosSectionRepository cosSectionRepository;
    @Autowired
    private CosExercisesRepository cosExercisesRepository;
    @Autowired
    private CosExercisesOptionRepository cosExercisesOptionRepository;
    @Autowired
    private CosCourseRepository cosCourseRepository;
    @Autowired
    private CosProgressRepository cosProgressRepository;
    @Autowired
    private CosExercisesAnswerRecordRepository cosExercisesAnswerRecordRepository;

    @Value("${course.detail.refresh.url}")
    private String refreshCourseDetailUrl;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addChapter(JSONObject jo){
        String courseId = jo.getString("courseId");
        //章list
        JSONArray chapters = jo.getJSONArray("chapters");
        Date nowTime = new Date();
        for(int i = 0; i < chapters.size(); i++){
            JSONObject chapter = chapters.getJSONObject(i);
            CosChapter cosChapter = new CosChapter();
            cosChapter.setCourseId(courseId);
            cosChapter.setCreateTime(nowTime);
            cosChapter.setChapterName(chapter.getString("chapterName"));
            cosChapter.setIntroduction(chapter.getString("introduction"));
            cosChapter.setSort(i + 1);
            long chapterId = cosChapterRepository.save(cosChapter).getId();
            //小节list
            JSONArray sections = chapter.getJSONArray("sections");
            for(int j = 0; j < sections.size(); j++){
                JSONObject section = sections.getJSONObject(j);
                CosSection cosSection = new CosSection();
                cosSection.setChapterId(chapterId);
                cosSection.setFilePath(section.getString("filePath"));
                cosSection.setType(section.getInteger("type"));
                cosSection.setName(section.getString("name"));
                cosSection.setTotalNumber(section.getInteger("totalNumber"));
                cosSection.setFileName(section.getString("fileName"));
                cosSection.setCreateTime(nowTime);
                cosSection.setSectionType(CosSectionType.indexOfMsg(section.getString("sectionType")));
                cosSection.setSort(j + 1);
                String sectionId = cosSectionRepository.save(cosSection).getId();
                //问题list
                JSONArray questions = section.getJSONArray("questions");
                for(int k = 0; k < questions.size(); k++){
                    JSONObject question = questions.getJSONObject(k);
                    CosExercises cosExercises = new CosExercises();
                    cosExercises.setSectionId(sectionId);
                    cosExercises.setContent(question.getString("content"));
                    cosExercises.setCorrectOptions(question.getJSONArray("correctOptions"));
                    cosExercises.setName(question.getString("name"));
                    cosExercises.setQuestionType(CosExercisesType.indexOfName(question.getString("questionType")));
                    cosExercises.setCreateTime(nowTime);
                    cosExercises.setSort(k + 1);
                    long questionId = cosExercisesRepository.save(cosExercises).getId();
                    JSONArray options = question.getJSONArray("options");
                    //选项list
                    for(int l = 0; l < options.size(); l++){
                        JSONObject option = options.getJSONObject(l);
                        CosExercisesOption cosExercisesOption = new CosExercisesOption();
                        cosExercisesOption.setOption(option.getString("option"));
                        cosExercisesOption.setQuestionId(questionId);
                        cosExercisesOption.setContent(option.getString("content"));
                        cosExercisesOption.setCreateTime(nowTime);
                        cosExercisesOption.setSort(l + 1);
                        cosExercisesOptionRepository.save(cosExercisesOption);
                    }
                }
            }
        }
        HttpUtil.sendGetRequest(refreshCourseDetailUrl+courseId);
    }

    @Override
    public JSONArray getChapterInfo(String courseId){
        //章
        List<CosChapter> chapterList = cosChapterRepository.findByCourseId(courseId);
        //节
        List<CosSection> sectionList = cosSectionRepository.getCosSectionByCourseId(courseId);
        //问题
        List<CosExercises> questionList = cosExercisesRepository.getCosQuestionByCourseId(courseId);
        //选项
        List<CosExercisesOption> optionList = cosExercisesOptionRepository.getCosOptionByCourseId(courseId);
        JSONArray data = new JSONArray();
        Map sectionsMap = new HashMap();
        Map questionsMap = new HashMap();
        Map optionsMap = new HashMap();
        optionList.forEach(cosOption -> {
            JSONArray ja = (JSONArray) optionsMap.get(cosOption.getQuestionId());
            JSONObject optionJO = JSONObject.parseObject(JSONObject.toJSON(cosOption).toString());
            if(ja == null){
                JSONArray newOptionJa = new JSONArray();
                newOptionJa.add(optionJO);
                optionsMap.put(cosOption.getQuestionId(), newOptionJa);
            }else{
                ja.add(optionJO);
                optionsMap.put(cosOption.getQuestionId(), ja);
            }
        });
        questionList.forEach(cosQuestion -> {
            List<JSONObject> cosOptions = (List<JSONObject>) optionsMap.get(cosQuestion.getId());
            JSONArray ja = (JSONArray) questionsMap.get(cosQuestion.getSectionId());
            JSONObject questionJO = JSONObject.parseObject(JSONObject.toJSON(cosQuestion).toString());
            List<JSONObject> optionJOs = new ArrayList<>();
            for(JSONObject option : cosOptions){
                if(cosQuestion.getCorrectOptions().contains(option.getString("option"))){
                    option.put("isCorrect", 1);//正确选项
                }else{
                    option.put("isCorrect", 0);//错误选项
                }
                optionJOs.add(option);
            }
            questionJO.put("options", optionJOs);
            if(ja == null){
                JSONArray newJa = new JSONArray();
                newJa.add(questionJO);
                questionsMap.put(cosQuestion.getSectionId(), newJa);
            }else{
                ja.add(questionJO);
                questionsMap.put(cosQuestion.getSectionId(), ja);
            }
        });
        sectionList.forEach(cosSection -> {
            List<CosExercises> cosQuestions = (List<CosExercises>) questionsMap.get(cosSection.getId());
            JSONArray ja = (JSONArray) sectionsMap.get(cosSection.getChapterId());
            JSONObject sectionJO = JSONObject.parseObject(JSONObject.toJSON(cosSection).toString());
            sectionJO.put("questions", cosQuestions == null ? new ArrayList<>() : cosQuestions);
            if(ja == null){
                JSONArray newJa = new JSONArray();
                newJa.add(sectionJO);
                sectionsMap.put(cosSection.getChapterId(), newJa);
            }else{
                ja.add(sectionJO);
                sectionsMap.put(cosSection.getChapterId(), ja);
            }
        });
        chapterList.forEach(cosChapter -> {
            JSONObject chapterJO = JSONObject.parseObject(JSONObject.toJSON(cosChapter).toString());
            chapterJO.put("sections", sectionsMap.get(cosChapter.getId()) == null ? new ArrayList<>() : sectionsMap.get(cosChapter.getId()));
            data.add(chapterJO);
        });
        return data;
    }

    @Override
    public JSONObject chapterPreview(String courseId){
        //章
        List<CosChapter> chapterList = cosChapterRepository.findByCourseId(courseId);
        //节
        List<CosSection> sectionList = cosSectionRepository.getCosSectionByCourseId(courseId);
        JSONObject data = new JSONObject();
        List<JSONObject> chapters = new ArrayList<>();
        List<JSONObject> exercises = new ArrayList<>();
        Map sectionsMap = new HashMap();
        Map exercisesOptionMap = new HashMap();
        sectionList.forEach(cosSection -> {
            JSONArray ja = (JSONArray) sectionsMap.get(cosSection.getChapterId());
            JSONObject sectionJO = JSONObject.parseObject(JSONObject.toJSON(cosSection).toString());
            if(ja == null){
                JSONArray newJa = new JSONArray();
                newJa.add(sectionJO);
                sectionsMap.put(cosSection.getChapterId(), newJa);
            }else{
                ja.add(sectionJO);
                sectionsMap.put(cosSection.getChapterId(), ja);
            }
        });
        chapterList.forEach(cosChapter -> {
            JSONObject chapterJO = JSONObject.parseObject(JSONObject.toJSON(cosChapter).toString());
            chapterJO.put("sections", sectionsMap.get(cosChapter.getId()) == null ? new ArrayList<>() : sectionsMap.get(cosChapter.getId()));
            chapters.add(chapterJO);
        });
        //预览取第一章第一节的题目
        String firstSectionId = chapters.get(0).getJSONArray("sections").getJSONObject(0).getString("id");
        List<CosExercises> cosExercisesList = cosExercisesRepository.getCosQuestionBySectionId(firstSectionId);
        List<CosExercisesOption> cosExercisesOptionList = cosExercisesOptionRepository.getCosOptionBySectionId(firstSectionId);
        cosExercisesOptionList.forEach(cosExercisesOption -> {
            JSONArray ja = (JSONArray) exercisesOptionMap.get(cosExercisesOption.getQuestionId());
            JSONObject exercisesOptionJO = JSONObject.parseObject(JSONObject.toJSON(cosExercisesOption).toString());
            if(ja == null){
                JSONArray newJa = new JSONArray();
                newJa.add(exercisesOptionJO);
                exercisesOptionMap.put(cosExercisesOption.getQuestionId(), newJa);
            }else{
                ja.add(exercisesOptionJO);
                exercisesOptionMap.put(cosExercisesOption.getQuestionId(), ja);
            }
        });
        cosExercisesList.forEach(cosExercises -> {
            JSONObject exercisesJO = JSONObject.parseObject(JSONObject.toJSON(cosExercises).toString());
            exercisesJO.put("options", exercisesOptionMap.get(cosExercises.getId()));
            exercises.add(exercisesJO);
        });
        data.put("chapters", chapters);
        data.put("exercises", exercises);
        data.put("nowFilePath", chapters.get(0).getJSONArray("sections").getJSONObject(0).getString("filePath"));
        data.put("totalNumber", chapters.get(0).getJSONArray("sections").getJSONObject(0).getInteger("totalNumber"));
        return data;
    }

    @Override
    public JSONObject sectionInfo(String sectionId){
        CosSection section = cosSectionRepository.findById(sectionId).get();
        String courseId = cosChapterRepository.getCourseIdBySectionId(sectionId);
        //章
        List<CosChapter> chapterList = cosChapterRepository.findByCourseId(courseId);
        //节
        List<CosSection> sectionList = cosSectionRepository.getCosSectionByCourseId(courseId);
        JSONObject data = new JSONObject();
        List<JSONObject> chapters = new ArrayList<>();
        List<JSONObject> exercises = new ArrayList<>();
        Map sectionsMap = new HashMap();
        Map exercisesOptionMap = new HashMap();
        sectionList.forEach(cosSection -> {
            JSONArray ja = (JSONArray) sectionsMap.get(cosSection.getChapterId());
            JSONObject sectionJO = JSONObject.parseObject(JSONObject.toJSON(cosSection).toString());
            if(ja == null){
                JSONArray newJa = new JSONArray();
                newJa.add(sectionJO);
                sectionsMap.put(cosSection.getChapterId(), newJa);
            }else{
                ja.add(sectionJO);
                sectionsMap.put(cosSection.getChapterId(), ja);
            }
        });
        chapterList.forEach(cosChapter -> {
            JSONObject chapterJO = JSONObject.parseObject(JSONObject.toJSON(cosChapter).toString());
            chapterJO.put("sections", sectionsMap.get(cosChapter.getId()) == null ? new ArrayList<>() : sectionsMap.get(cosChapter.getId()));
            chapters.add(chapterJO);
        });
        List<CosExercises> cosExercisesList = cosExercisesRepository.getCosQuestionBySectionId(sectionId);
        List<CosExercisesOption> cosExercisesOptionList = cosExercisesOptionRepository.getCosOptionBySectionId(sectionId);
        cosExercisesOptionList.forEach(cosExercisesOption -> {
            JSONArray ja = (JSONArray) exercisesOptionMap.get(cosExercisesOption.getQuestionId());
            JSONObject exercisesOptionJO = JSONObject.parseObject(JSONObject.toJSON(cosExercisesOption).toString());
            if(ja == null){
                JSONArray newJa = new JSONArray();
                newJa.add(exercisesOptionJO);
                exercisesOptionMap.put(cosExercisesOption.getQuestionId(), newJa);
            }else{
                ja.add(exercisesOptionJO);
                exercisesOptionMap.put(cosExercisesOption.getQuestionId(), ja);
            }
        });
        cosExercisesList.forEach(cosExercises -> {
            JSONObject exercisesJO = JSONObject.parseObject(JSONObject.toJSON(cosExercises).toString());
            exercisesJO.put("options", exercisesOptionMap.get(cosExercises.getId()));
            exercises.add(exercisesJO);
        });
        data.put("chapters", chapters);
        data.put("exercises", exercises);
        data.put("nowFilePath", section.getFilePath());
        data.put("totalNumber", section.getTotalNumber());
        return data;
    }

    @Override
    public JSONObject chapterAndSectionList(String courseId){
        //章
        List<CosChapter> chapterList = cosChapterRepository.findByCourseId(courseId);
        //节
        List<CosSection> sectionList = cosSectionRepository.getCosSectionByCourseId(courseId);
        JSONObject data = new JSONObject();
        List<JSONObject> chapters = new ArrayList<>();
        Map sectionsMap = new HashMap();
        sectionList.forEach(cosSection -> {
            JSONArray ja = (JSONArray) sectionsMap.get(cosSection.getChapterId());
            JSONObject sectionJO = JSONObject.parseObject(JSONObject.toJSON(cosSection).toString());
            if(ja == null){
                JSONArray newJa = new JSONArray();
                newJa.add(sectionJO);
                sectionsMap.put(cosSection.getChapterId(), newJa);
            }else{
                ja.add(sectionJO);
                sectionsMap.put(cosSection.getChapterId(), ja);
            }
        });
        chapterList.forEach(cosChapter -> {
            JSONObject chapterJO = JSONObject.parseObject(JSONObject.toJSON(cosChapter).toString());
            chapterJO.put("sections", sectionsMap.get(cosChapter.getId()));
            chapters.add(chapterJO);
        });
        CosCourse cosCourse = cosCourseRepository.findById(courseId).get();
        data.put("chapters", chapters);
        data.put("course", cosCourse);
        return data;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCoursePayment(JSONObject jo){
        CosCourse cosCourse = JSONObject.parseObject(jo.getJSONObject("course").toJSONString(), CosCourse.class);
        cosCourseRepository.save(cosCourse);
        JSONArray chapters = jo.getJSONArray("chapters");
        for(int i = 0; i < chapters.size(); i ++){
            JSONObject chapterJO = chapters.getJSONObject(i);
            JSONArray sections = chapterJO.getJSONArray("sections");
            chapterJO.remove("sections");
            CosChapter cosChapter = JSONObject.parseObject(chapterJO.toJSONString(), CosChapter.class);
            cosChapterRepository.save(cosChapter);
            for(int j = 0; j < sections.size(); j ++){
                JSONObject sectionJO = sections.getJSONObject(j);
                CosSection cosSection = JSONObject.parseObject(sectionJO.toJSONString(), CosSection.class);
                cosSectionRepository.save(cosSection);
            }
        }
        HttpUtil.sendGetRequest(refreshCourseDetailUrl+cosCourse.getId());
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateChapterInfo(JSONObject jo){
        //删除的章
        JSONArray deleteChpaterIds = jo.getJSONArray("deleteChpaterIds");
        if(deleteChpaterIds != null && deleteChpaterIds.size() > 0){
            List<Long> deleteChpaterIdList = JSONObject.parseArray(deleteChpaterIds.toJSONString(), Long.class);
            List<String> deleteSectionIdList = cosSectionRepository.getSectionIdsByChapterIds(deleteChpaterIdList);
            //删除节
            cosSectionRepository.deleteByIdIn(deleteSectionIdList);
            //删除章
            cosChapterRepository.deleteByIdIn(deleteChpaterIdList);
        }
        //删除的节
        JSONArray deleteSectionIds = jo.getJSONArray("deleteSectionIds");
        if(deleteSectionIds != null && deleteSectionIds.size() > 0){
            List<String> deleteSectionIdList = JSONObject.parseArray(deleteSectionIds.toJSONString(), String.class);
            //删除节
            cosSectionRepository.deleteByIdIn(deleteSectionIdList);
        }
        //删除的问题
        JSONArray deleteQuestionIds = jo.getJSONArray("deleteQuestionIds");
        if(deleteQuestionIds != null && deleteQuestionIds.size() > 0){
            List<Long> deleteQuestionIdList = JSONObject.parseArray(deleteQuestionIds.toJSONString(), Long.class);
            List<Long> deleteOptionIdList = cosExercisesOptionRepository.getOptionIdsByQuestionIds(deleteQuestionIdList);
            //删除选项
            cosExercisesOptionRepository.deleteByIdIn(deleteOptionIdList);
            //删除问题
            cosExercisesRepository.deleteByIdIn(deleteQuestionIdList);
        }
        //删除的选项
        JSONArray deleteOptionIds = jo.getJSONArray("deleteOptionIds");
        if(deleteOptionIds != null && deleteOptionIds.size() > 0){
            List<Long> deleteOptionIdList = JSONObject.parseArray(deleteOptionIds.toJSONString(), Long.class);
            //删除选项
            cosExercisesOptionRepository.deleteByIdIn(deleteOptionIdList);
        }
        String courseId = jo.getString("courseId");
        //章list
        JSONArray chapters = jo.getJSONArray("chapters");
        Date nowTime = new Date();
        for(int i = 0; i < chapters.size(); i++){
            JSONObject chapter = chapters.getJSONObject(i);
            CosChapter cosChapter = new CosChapter();
            cosChapter.setId(chapter.getLong("id"));
            cosChapter.setCourseId(courseId);
            cosChapter.setCreateTime(nowTime);
            cosChapter.setChapterName(chapter.getString("chapterName"));
            cosChapter.setIntroduction(chapter.getString("introduction"));
            cosChapter.setSort(i + 1);
            long chapterId = cosChapterRepository.save(cosChapter).getId();
            //小节list
            JSONArray sections = chapter.getJSONArray("sections");
            for(int j = 0; j < sections.size(); j++){
                JSONObject section = sections.getJSONObject(j);
                CosSection cosSection = new CosSection();
                cosSection.setId(section.getString("id"));
                cosSection.setChapterId(chapterId);
                cosSection.setFilePath(section.getString("filePath"));
                cosSection.setFileName(section.getString("fileName"));
                cosSection.setType(section.getInteger("type"));
                cosSection.setName(section.getString("name"));
                cosSection.setTotalNumber(section.getInteger("totalNumber"));
                cosSection.setCreateTime(nowTime);
                cosSection.setSectionType(CosSectionType.indexOfMsg(section.getString("sectionType")));
                cosSection.setSort(j + 1);
                String sectionId = cosSectionRepository.save(cosSection).getId();
                //问题list
                JSONArray questions = section.getJSONArray("questions");
                for(int k = 0; k < questions.size(); k++){
                    JSONObject question = questions.getJSONObject(k);
                    CosExercises cosExercises = new CosExercises();
                    cosExercises.setId(question.getLong("id"));
                    cosExercises.setSectionId(sectionId);
                    cosExercises.setContent(question.getString("content"));
                    cosExercises.setCorrectOptions(question.getJSONArray("correctOptions"));
                    cosExercises.setName(question.getString("name"));
                    cosExercises.setQuestionType(CosExercisesType.indexOfName(question.getString("questionType")));
                    cosExercises.setCreateTime(nowTime);
                    cosExercises.setSort(k + 1);
                    long questionId = cosExercisesRepository.save(cosExercises).getId();
                    JSONArray options = question.getJSONArray("options");
                    //选项list
                    for(int l = 0; l < options.size(); l++){
                        JSONObject option = options.getJSONObject(l);
                        CosExercisesOption cosExercisesOption = new CosExercisesOption();
                        cosExercisesOption.setId(option.getLong("id"));
                        cosExercisesOption.setOption(option.getString("option"));
                        cosExercisesOption.setQuestionId(questionId);
                        cosExercisesOption.setContent(option.getString("content"));
                        cosExercisesOption.setCreateTime(nowTime);
                        cosExercisesOption.setSort(l + 1);
                        cosExercisesOptionRepository.save(cosExercisesOption);
                    }
                }
            }
        }
        HttpUtil.sendGetRequest(refreshCourseDetailUrl+courseId);
    }
}
