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
        //校验角色的正确性
        CosUserMsg cosUserMsg = checkRole(paperAddUpdateVo.getCreateById());
        //校验试卷名称的唯一性
        Boolean aBoolean = checkUniqueName(paperAddUpdateVo.getName(), paperAddUpdateVo.getSchoolId(), paperAddUpdateVo.getId());
        if (!aBoolean) {
            throw new GeneralException(ResultEntity.ResultStatus.ERROR_PARAMETER, "试卷名称重复");
        }
        //修改或删除填充PO参数
        CosPaper cosPaperDb = updateOrAddParameterFilling(isAdd, paperAddUpdateVo);
        List<QuestionAddVO> questionList = paperAddUpdateVo.getQuestionList();
        //试题数量
        int questionCount = questionList.size();
        int optionQuestionCount = 0;
        int shortQuestionCount = 0;
        int videoQuestionCount = 0;
        int programQuestionCount = 0;
        List<CosOption> cosOptionList = new ArrayList<>();
        for (QuestionAddVO questionAddVo : questionList) {
            //校验试题参数
            if (Objects.isNull(questionAddVo.getType()) || StringUtils.isBlank(questionAddVo.getName())
                    || StringUtils.isBlank(questionAddVo.getContent())) {
                log.error("add question fail:{}", questionAddVo.toString());
                throw new GeneralException(ResultEntity.ResultStatus.ERROR_PARAMETER, "参数错误");
            } else {
                CosQuestion cosQuestion = new CosQuestion();
                BeanUtils.copyProperties(questionAddVo, cosQuestion);
                cosQuestion.setCosPaperId(cosPaperDb.getId());
                if (CollectionUtils.isEmpty(questionAddVo.getCorrectOptionItem())) {
                    cosQuestion.setCorrectOptionItem(new ArrayList<>());
                }
                //根据正确选项的多少判断选择题类型,默认单选SINGLE---0
                if (cosQuestion.getCorrectOptionItem().size() > 1) {
                    cosQuestion.setOptionType(OptionType.MULTIPLE);
                } else {
                    cosQuestion.setOptionType(OptionType.SINGLE);
                }
                cosQuestion.setCreateTime(new Date());
                //添加试题
                CosQuestion saveCosQuestion = cosQuestionRepository.save(cosQuestion);
                //选择题
                if (questionAddVo.getType().equals(QuestionType.OPTION_QUESTION)) {
                    //校验选择题参数
                    if (CollectionUtils.isEmpty(questionAddVo.getCorrectOptionItem()) || CollectionUtils.isEmpty(questionAddVo.getOptionList())) {
                        log.error("add option question fail:{}", questionAddVo.toString());
                        throw new GeneralException(ResultEntity.ResultStatus.ERROR_PARAMETER, "参数错误");
                    }
                    for (OptionAddVO optionAddVo : questionAddVo.getOptionList()) {
                        if (StringUtils.isBlank(optionAddVo.getOptionItem()) || StringUtils.isBlank(optionAddVo.getContent())) {
                            log.error("add option fail:{}", optionAddVo.toString());
                            throw new GeneralException(ResultEntity.ResultStatus.ERROR_PARAMETER, "参数错误");
                        }
                        toCosOption(cosOptionList, optionAddVo, saveCosQuestion.getId());
                    }
                    optionQuestionCount++;
                } else if (questionAddVo.getType().equals(QuestionType.SHORT_QUESTION)) {
                    //简答题
                    shortQuestionCount++;
                } else if (questionAddVo.getType().equals(QuestionType.VIDEO_QUESTION)) {
                    //视频问答题
                    videoQuestionCount++;
                } else if (questionAddVo.getType().equals(QuestionType.PROGRAM_QUESTION)) {
                    //编程题
                    programQuestionCount++;
                }
            }
        }
        if (CollectionUtils.isNotEmpty(cosOptionList)) {
            //保存选项信息
            cosOptionRepository.saveAll(cosOptionList);
        }
        //冗余老师姓名信息
        cosPaperDb.setCreateByName(cosUserMsg.getRealName());
        cosPaperDb.setQuestionCount(questionCount);
        cosPaperDb.setOptionQuestionCount(optionQuestionCount);
        cosPaperDb.setShortQuestionCount(shortQuestionCount);
        cosPaperDb.setVideoQuestionCount(videoQuestionCount);
        cosPaperDb.setProgramQuestionCount(programQuestionCount);
        //更新试卷试题数量统计信息
        cosPaperRepository.save(cosPaperDb);
        return cosPaperDb;
    }

    @Override
    public Page<CosPaperListDTO> listPaper(Pageable pageable, SearchPaperVO searchPaperVo) {
        // 构造自定义查询条件
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
            throw new GeneralException(ResultEntity.ResultStatus.ERROR_PARAMETER, "参数错误");
        }
        JSONObject paperJson = JSONObject.parseObject(JSONObject.toJSON(cosPaper).toString());
        List<CosQuestion> questions = cosQuestionRepository.findByCosPaperId(cosPaper.getId());
        Map<Long, JSONObject> questionMap = new LinkedHashMap<>();
        questions.forEach(cosQuestion -> {
                    JSONObject cosQuestion1 = JSONObject.parseObject(JSONObject.toJSON(cosQuestion).toString());
                    if (QuestionType.OPTION_QUESTION != cosQuestion.getType()) {
                        //非选择题
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
            //正确选项
            if (questionJO.getJSONArray("correctOptionItem").contains(option.getOptionItem())) {
                optionJO.put("isCorrect", 1);
            } else {
                optionJO.put("isCorrect", 0);
            }
            optionList.add(optionJO);
            questionJO.put("options", optionList);
        }
        List<JSONObject> questionList = new ArrayList<>(questionMap.values());
        //非编辑反显,试题顺序随机,要随机试题
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
            //试卷编辑名称唯一校验
            count = cosPaperRepository.checkUniqueName2(paperName, schoolId, paperId);
        } else {
            //试卷新增名称唯一校验
            count = cosPaperRepository.checkUniqueName(paperName, schoolId);
        }
        return count <= 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String paperId) {

        CosPaper cosPaperDb = mayBeDeleteOrUpdate(paperId);
        //删除试卷
        cosPaperRepository.deleteById(cosPaperDb.getId());
        //删除试卷关联试题内容
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
     * 校验是否符合删除或编辑的逻辑
     * 未开考的试卷可以删除或编辑
     *
     * @param paperId
     * @return
     */
    private CosPaper mayBeDeleteOrUpdate(String paperId) {
        CosPaper cosPaperDb = cosPaperRepository.findById(paperId).orElse(null);
        if (Objects.isNull(cosPaperDb)) {
            log.error("updateOrDelete fail ,cosPaperDB is null");
            throw new GeneralException(ResultEntity.ResultStatus.ERROR_PARAMETER, "参数错误");
        }
        //中有未开考的试卷可编辑和删除
        long currentTimeMillis = System.currentTimeMillis();
        Date examTime = cosPaperDb.getExamTime();
        if (Objects.nonNull(examTime)) {
            if (examTime.getTime() <= currentTimeMillis) {
                log.error("updateOrDelete fail ,examTime is overdue");
                throw new GeneralException(ResultEntity.ResultStatus.ERROR_PARAMETER, "此试卷状态不可编辑或删除");
            }
        }
        return cosPaperDb;
    }

    /**
     * 删除试卷相关的试题和选项信息
     *
     * @param paperId
     */
    private void deleteQuestionAndOption(String paperId) {
        //删除试卷关联试题内容
        List<CosQuestion> cosQuestionList = cosQuestionRepository.findByCosPaperId(paperId);
        if (CollectionUtils.isNotEmpty(cosQuestionList)) {
            cosQuestionList.forEach(cosQuestion -> {
                if (cosQuestion.getType().equals(QuestionType.OPTION_QUESTION)) {
                    //删除选项
                    cosOptionRepository.deleteByCosQuestionId(cosQuestion.getId());
                }
            });
            //删除试题
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
     * 校验老师角色的正确性
     *
     * @param createById
     * @return
     */
    private CosUserMsg checkRole(String createById) {
        CosUserMsg cosUserMsg = cosUserMsgRepository.findByGuid(createById).orElse(null);
        if (Objects.isNull(cosUserMsg)) {
            log.error("cosUserMsg is null");
            throw new GeneralException(ResultEntity.ResultStatus.ERROR_PARAMETER, "参数错误");
        } else if (cosUserMsg.getRole() != 1) {
            log.error("cosUserMsg error role:{}", cosUserMsg.toString());
            throw new GeneralException(ResultEntity.ResultStatus.ERROR_PARAMETER, "非老师角色");
        }
        return cosUserMsg;
    }

    /**
     * 填充添加或编辑参数
     *
     * @param isAdd
     * @param paperAddUpdateVo
     * @return
     */
    private CosPaper updateOrAddParameterFilling(boolean isAdd, PaperAddUpdateVO paperAddUpdateVo) {
        CosPaper cosPaperDb;
        if (!isAdd) {
            //判断是否符合编辑或删除的逻辑
            cosPaperDb = mayBeDeleteOrUpdate(paperAddUpdateVo.getId());
            //删除试卷关联试题内容
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
            //添加试卷
            cosPaperDb = cosPaperRepository.save(cosPaper);
        }
        return cosPaperDb;
    }

}
