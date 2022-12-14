package com.bonc.turing.cms.exercise.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bonc.turing.cms.exercise.dao.repository.*;
import com.bonc.turing.cms.exercise.domain.*;
import com.bonc.turing.cms.exercise.dto.CosPaperListDTO;
import com.bonc.turing.cms.exercise.dto.CosSchoolCourseListDTO;
import com.bonc.turing.cms.exercise.service.CosPaperService;
import com.bonc.turing.cms.exercise.type.OptionType;
import com.bonc.turing.cms.exercise.type.PaperOrderType;
import com.bonc.turing.cms.exercise.type.QuestionType;
import com.bonc.turing.cms.exercise.utils.GeneralException;
import com.bonc.turing.cms.exercise.vo.OptionAddVO;
import com.bonc.turing.cms.exercise.vo.PaperAddUpdateVO;
import com.bonc.turing.cms.exercise.vo.QuestionAddVO;
import com.bonc.turing.cms.exercise.vo.SearchPaperVO;
import com.bonc.turing.cms.manage.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * description:
 *
 * @author lxh
 * @date 2020/3/23 12:22
 */
@Slf4j
@Service
public class CosPaperServiceImpl implements CosPaperService {

    @Autowired
    private CosPaperRepository cosPaperRepository;

    @Autowired
    private CosQuestionRepository cosQuestionRepository;

    @Autowired
    private CosOptionRepository cosOptionRepository;

    @Autowired
    private CosUserMsgRepository cosUserMsgRepository;

    @Autowired
    private CosCourseRepository cosCourseRepository;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public CosPaper addOrUpdatePaper(PaperAddUpdateVO paperAddUpdateVo, boolean isAdd) {
        //????????????????????????
        CosUserMsg cosUserMsg = checkRole(paperAddUpdateVo.getCreateById());
        //??????????????????????????????
        Boolean aBoolean = checkUniqueName(paperAddUpdateVo.getName(), paperAddUpdateVo.getSchoolId(), paperAddUpdateVo.getId());
        if (!aBoolean) {
            throw new GeneralException(ResultEntity.ResultStatus.ERROR_PARAMETER, "??????????????????");
        }
        //?????????????????????PO??????
        CosPaper cosPaperDb = updateOrAddParameterFilling(isAdd, paperAddUpdateVo);
        List<QuestionAddVO> questionList = paperAddUpdateVo.getQuestionList();
        //????????????
        int questionCount = questionList.size();
        int optionQuestionCount = 0;
        int shortQuestionCount = 0;
        int videoQuestionCount = 0;
        int programQuestionCount = 0;
        List<CosOption> cosOptionList = new ArrayList<>();
        for (QuestionAddVO questionAddVo : questionList) {
            //??????????????????
            if (Objects.isNull(questionAddVo.getType()) || StringUtils.isBlank(questionAddVo.getName())
                    || StringUtils.isBlank(questionAddVo.getContent())) {
                log.error("add question fail:{}", questionAddVo.toString());
                throw new GeneralException(ResultEntity.ResultStatus.ERROR_PARAMETER, "????????????");
            } else {
                CosQuestion cosQuestion = new CosQuestion();
                BeanUtils.copyProperties(questionAddVo, cosQuestion);
                cosQuestion.setCosPaperId(cosPaperDb.getId());
                if (CollectionUtils.isEmpty(questionAddVo.getCorrectOptionItem())) {
                    cosQuestion.setCorrectOptionItem(new ArrayList<>());
                }
                //????????????????????????????????????????????????,????????????SINGLE---0
                if (cosQuestion.getCorrectOptionItem().size() > 1) {
                    cosQuestion.setOptionType(OptionType.MULTIPLE);
                } else {
                    cosQuestion.setOptionType(OptionType.SINGLE);
                }
                cosQuestion.setCreateTime(new Date());
                //????????????
                CosQuestion saveCosQuestion = cosQuestionRepository.save(cosQuestion);
                //?????????
                if (questionAddVo.getType().equals(QuestionType.OPTION_QUESTION)) {
                    //?????????????????????
                    if (CollectionUtils.isEmpty(questionAddVo.getCorrectOptionItem()) || CollectionUtils.isEmpty(questionAddVo.getOptionList())) {
                        log.error("add option question fail:{}", questionAddVo.toString());
                        throw new GeneralException(ResultEntity.ResultStatus.ERROR_PARAMETER, "????????????");
                    }
                    for (OptionAddVO optionAddVo : questionAddVo.getOptionList()) {
                        if (StringUtils.isBlank(optionAddVo.getOptionItem()) || StringUtils.isBlank(optionAddVo.getContent())) {
                            log.error("add option fail:{}", optionAddVo.toString());
                            throw new GeneralException(ResultEntity.ResultStatus.ERROR_PARAMETER, "????????????");
                        }
                        toCosOption(cosOptionList, optionAddVo, saveCosQuestion.getId());
                    }
                    optionQuestionCount++;
                } else if (questionAddVo.getType().equals(QuestionType.SHORT_QUESTION)) {
                    //?????????
                    shortQuestionCount++;
                } else if (questionAddVo.getType().equals(QuestionType.VIDEO_QUESTION)) {
                    //???????????????
                    videoQuestionCount++;
                } else if (questionAddVo.getType().equals(QuestionType.PROGRAM_QUESTION)) {
                    //?????????
                    programQuestionCount++;
                }
            }
        }
        if (CollectionUtils.isNotEmpty(cosOptionList)) {
            //??????????????????
            cosOptionRepository.saveAll(cosOptionList);
        }
        //????????????????????????
        cosPaperDb.setCreateByName(cosUserMsg.getRealName());
        cosPaperDb.setQuestionCount(questionCount);
        cosPaperDb.setOptionQuestionCount(optionQuestionCount);
        cosPaperDb.setShortQuestionCount(shortQuestionCount);
        cosPaperDb.setVideoQuestionCount(videoQuestionCount);
        cosPaperDb.setProgramQuestionCount(programQuestionCount);
        //????????????????????????????????????
        cosPaperRepository.save(cosPaperDb);
        return cosPaperDb;
    }

    @Override
    public Page<CosPaperListDTO> listPaper(Pageable pageable, SearchPaperVO searchPaperVo) {
        // ???????????????????????????
        Specification<CosPaper> queryCondition = (Specification<CosPaper>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (StringUtils.isNotBlank(searchPaperVo.getCreateById())) {
                predicateList.add(criteriaBuilder.equal(root.get("createById"), searchPaperVo.getCreateById()));
            }
            if (StringUtils.isNotBlank(searchPaperVo.getName())) {
                predicateList.add(criteriaBuilder.like(root.get("name"), "%" + searchPaperVo.getName() + "%"));
            }
            if (Objects.nonNull(searchPaperVo.getCreateTimeStart()) && Objects.nonNull(searchPaperVo.getCreateTimeEnd())) {
                predicateList.add(criteriaBuilder.between(root.get("createTime"), new Date(searchPaperVo.getCreateTimeStart()), new Date(searchPaperVo.getCreateTimeEnd())));
            }
            predicateList.add(criteriaBuilder.equal(root.get("isDelete"), 0));
            return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
        };
        Page<CosPaper> all = cosPaperRepository.findAll(queryCondition, pageable);
        List<CosPaper> content = all.getContent();
        List<CosPaperListDTO> collect;
        if (CollectionUtils.isNotEmpty(content)) {
            collect = content.stream().map(cosPaper -> {
                CosPaperListDTO paperListDTO = new CosPaperListDTO();
                BeanUtils.copyProperties(cosPaper, paperListDTO);
                paperListDTO.setExamTime(cosPaper.getExamTime().getTime());
                paperListDTO.setFinishTime(Objects.isNull(paperListDTO.getFinishTime()) ? 0 : paperListDTO.getFinishTime() / 60);
                return paperListDTO;
            }).collect(Collectors.toList());
        } else {
            collect = new ArrayList<>();
        }
        return new PageImpl<>(collect, all.getPageable(), all.getTotalElements());
    }

    @Override
    public JSONObject paperInfo(Boolean isUpdate, String paperId) {
        CosPaper cosPaper = cosPaperRepository.findById(paperId).orElse(null);
        if (Objects.isNull(cosPaper)) {
            log.error("paperId error not found:{}", paperId);
            throw new GeneralException(ResultEntity.ResultStatus.ERROR_PARAMETER, "????????????");
        }
        JSONObject paperJson = JSONObject.parseObject(JSONObject.toJSON(cosPaper).toString());
        List<CosQuestion> questions = cosQuestionRepository.findByCosPaperId(cosPaper.getId());
        Map<Long, JSONObject> questionMap = new LinkedHashMap<>();
        questions.forEach(cosQuestion -> {
                    JSONObject cosQuestion1 = JSONObject.parseObject(JSONObject.toJSON(cosQuestion).toString());
                    if (QuestionType.OPTION_QUESTION != cosQuestion.getType()) {
                        //????????????
                        cosQuestion1.put("options", new ArrayList<>());
                        cosQuestion1.put("correctOptionItem", new ArrayList<>());
                    }
                    questionMap.put(cosQuestion.getId(), cosQuestion1);
                }
        );
        List<CosOption> optionsList = cosOptionRepository.findByCosPaperId(paperId);
        for (CosOption option : optionsList) {
            JSONObject questionJO = questionMap.get(option.getCosQuestionId());
            JSONArray optionList = questionJO.getJSONArray("options");
            optionList = optionList == null ? new JSONArray() : optionList;
            JSONObject optionJO = JSONObject.parseObject(JSONObject.toJSON(option).toString());
            //????????????
            if (questionJO.getJSONArray("correctOptionItem").contains(option.getOptionItem())) {
                optionJO.put("isCorrect", 1);
            } else {
                optionJO.put("isCorrect", 0);
            }
            optionList.add(optionJO);
            questionJO.put("options", optionList);
        }
        List<JSONObject> questionList = new ArrayList<>(questionMap.values());
        //???????????????,??????????????????,???????????????
        if ((!isUpdate) && (cosPaper.getPaperOrder() == PaperOrderType.RANDOM)) {
            Collections.shuffle(questionList);
        }
        paperJson.put("questions", questionList);
        paperJson.put("finishTime", paperJson.getInteger("finishTime") / 60);
        return paperJson;
    }

    @Override
    public Boolean checkUniqueName(String paperName, String schoolId, String paperId) {
        long count;
        if (StringUtils.isNotEmpty(paperId)) {
            //??????????????????????????????
            count = cosPaperRepository.checkUniqueName2(paperName, schoolId, paperId);
        } else {
            //??????????????????????????????
            count = cosPaperRepository.checkUniqueName(paperName, schoolId);
        }
        return count <= 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String paperId) {

        CosPaper cosPaperDb = mayBeDeleteOrUpdate(paperId);
        //????????????
        cosPaperRepository.deleteById(cosPaperDb.getId());
        //??????????????????????????????
        deleteQuestionAndOption(cosPaperDb.getId());
    }

    @Override
    public List<CosSchoolCourseListDTO> schoolCourseList(String schoolId) {
        List<CosCourse> courseList = cosCourseRepository.findBySchoolId(schoolId);
        if (CollectionUtils.isNotEmpty(courseList)) {
            return courseList.stream().map(course -> {
                CosSchoolCourseListDTO cosSchoolCourseListDTO = new CosSchoolCourseListDTO();
                cosSchoolCourseListDTO.setId(course.getId());
                cosSchoolCourseListDTO.setName(course.getName());
                return cosSchoolCourseListDTO;
            }).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    /**
     * ??????????????????????????????????????????
     * ???????????????????????????????????????
     *
     * @param paperId
     * @return
     */
    private CosPaper mayBeDeleteOrUpdate(String paperId) {
        CosPaper cosPaperDb = cosPaperRepository.findById(paperId).orElse(null);
        if (Objects.isNull(cosPaperDb)) {
            log.error("updateOrDelete fail ,cosPaperDB is null");
            throw new GeneralException(ResultEntity.ResultStatus.ERROR_PARAMETER, "????????????");
        }
        //??????????????????????????????????????????
        long currentTimeMillis = System.currentTimeMillis();
        Date examTime = cosPaperDb.getExamTime();
        if (Objects.nonNull(examTime)) {
            if (examTime.getTime() <= currentTimeMillis) {
                log.error("updateOrDelete fail ,examTime is overdue");
                throw new GeneralException(ResultEntity.ResultStatus.ERROR_PARAMETER, "????????????????????????????????????");
            }
        }
        return cosPaperDb;
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param paperId
     */
    private void deleteQuestionAndOption(String paperId) {
        //??????????????????????????????
        List<CosQuestion> cosQuestionList = cosQuestionRepository.findByCosPaperId(paperId);
        if (CollectionUtils.isNotEmpty(cosQuestionList)) {
            cosQuestionList.forEach(cosQuestion -> {
                if (cosQuestion.getType().equals(QuestionType.OPTION_QUESTION)) {
                    //????????????
                    cosOptionRepository.deleteByCosQuestionId(cosQuestion.getId());
                }
            });
            //????????????
            cosQuestionRepository.deleteByCosPaperId(paperId);
        }
    }

    private void toCosOption(List<CosOption> cosOptionList, OptionAddVO optionAddVo, Long cosQuestionId) {
        CosOption cosOption = new CosOption();
        BeanUtils.copyProperties(optionAddVo, cosOption);
        cosOption.setCosQuestionId(cosQuestionId);
        cosOption.setCreateTime(new Date());
        cosOptionList.add(cosOption);
    }

    private CosPaper toCosPaper(PaperAddUpdateVO paperAddUpdateVo) {
        Date date = new Date();
        paperAddUpdateVo.setId(null);
        CosPaper cosPaper = new CosPaper();
        BeanUtils.copyProperties(paperAddUpdateVo, cosPaper);
        cosPaper.setExamTime(new Date(paperAddUpdateVo.getExamTime()));
        cosPaper.setFinishTime(paperAddUpdateVo.getFinishTime() * 60);
        cosPaper.setCreateTime(date);
        cosPaper.setUpdateTime(date);
        cosPaper.setIsDelete(0);
        return cosPaper;
    }

    /**
     * ??????????????????????????????
     *
     * @param createById
     * @return
     */
    private CosUserMsg checkRole(String createById) {
        CosUserMsg cosUserMsg = cosUserMsgRepository.findByGuid(createById).orElse(null);
        if (Objects.isNull(cosUserMsg)) {
            log.error("cosUserMsg is null");
            throw new GeneralException(ResultEntity.ResultStatus.ERROR_PARAMETER, "????????????");
        } else if (cosUserMsg.getRole() != 1) {
            log.error("cosUserMsg error role:{}", cosUserMsg.toString());
            throw new GeneralException(ResultEntity.ResultStatus.ERROR_PARAMETER, "???????????????");
        }
        return cosUserMsg;
    }

    /**
     * ???????????????????????????
     *
     * @param isAdd
     * @param paperAddUpdateVo
     * @return
     */
    private CosPaper updateOrAddParameterFilling(boolean isAdd, PaperAddUpdateVO paperAddUpdateVo) {
        CosPaper cosPaperDb;
        if (!isAdd) {
            //??????????????????????????????????????????
            cosPaperDb = mayBeDeleteOrUpdate(paperAddUpdateVo.getId());
            //??????????????????????????????
            deleteQuestionAndOption(cosPaperDb.getId());
            cosPaperDb.setUpdateTime(new Date());
            cosPaperDb.setCreateById(paperAddUpdateVo.getCreateById());
            cosPaperDb.setFinishTime(paperAddUpdateVo.getFinishTime() * 60);
            cosPaperDb.setName(paperAddUpdateVo.getName());
            cosPaperDb.setPaperOrder(paperAddUpdateVo.getPaperOrder());
            cosPaperDb.setExamTime(new Date(paperAddUpdateVo.getExamTime()));
            cosPaperDb.setRemark(paperAddUpdateVo.getRemark());
        } else {
            CosPaper cosPaper = toCosPaper(paperAddUpdateVo);
            //????????????
            cosPaperDb = cosPaperRepository.save(cosPaper);
        }
        return cosPaperDb;
    }

}
