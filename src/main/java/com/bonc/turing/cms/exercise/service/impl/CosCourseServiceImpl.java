package com.bonc.turing.cms.exercise.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bonc.turing.cms.common.utils.HttpUtil;
import com.bonc.turing.cms.exercise.dao.mapper.CosContainerMapper;
import com.bonc.turing.cms.exercise.dao.mapper.CosCourseMapper;
import com.bonc.turing.cms.exercise.dao.repository.*;
import com.bonc.turing.cms.exercise.domain.*;
import com.bonc.turing.cms.exercise.dto.CmsCourseDTO;
import com.bonc.turing.cms.exercise.service.CosCourseService;
import com.bonc.turing.cms.manage.repository.CmsPermissionsRepository;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static com.bonc.turing.cms.enums.BCreateCourseEnum.CODE_3001;
import static com.bonc.turing.cms.enums.BCreateCourseEnum.CODE_3002;
import static com.bonc.turing.cms.enums.CourseFormEnum.COMPOSITE_COURSE;
import static com.bonc.turing.cms.enums.CourseFormEnum.SAS_COURSE;

/**
 * @author yh
 * @desc 课程相关
 * @desc 2020.03.23
 */
@Service
public class CosCourseServiceImpl implements CosCourseService {
    private static Logger logger = LoggerFactory.getLogger(CosCourseService.class);

    @Autowired
    private CosCourseRepository cosCourseRepository;

    @Autowired
    private CosUserMsgRepository cosUserMsgRepository;

    @Autowired
    private CosProgressRepository cosProgressRepository;

    @Autowired
    private CosPaperRepository cosPaperRepository;

    @Resource
    private CosCourseMapper cosCourseMapper;

    @Autowired
    private CosContainerMapper cosContainerMapper;

    @Autowired
    private CosContainerRepository cosContainerRepository;

    @Autowired
    private CosSchoolCourseRepository cosSchoolCourseRepository;

    @Value("${course.detail.refresh.url}")
    private String refreshCourseDetailUrl;

    @Autowired
    private CmsPermissionsRepository cmsPermissionsRepository;

    /**
     * 创建或编辑
     *
     * @param course
     * @return
     */
    @Override
    public Object createOrEditCourse(CosCourse course) {
        JSONObject result = new JSONObject();
        String courseId = "";
        try {
            if (null == course.getId() || "".equals(course.getId())) {
                //如果是新建
                //若为学校课程,课程作者则为创建课程的人
                if (course.getType() == 1) {
                    List<Integer> permissions = cmsPermissionsRepository.findByGuid(course.getCreateById());
                    if (permissions.size() > 0) {
                        if (course.getForm().equals(COMPOSITE_COURSE.getFormValue())) {
                            //复合课程
                            if (!permissions.contains(CODE_3001.getRoleCode())) {
                                logger.error("createOrEditCourse:no permission");
                                return "no_permission";
                            }
                        } else if (course.getForm().equals(SAS_COURSE.getFormValue())) {
                            //sas课程
                            if (!permissions.contains(CODE_3002.getRoleCode())) {
                                logger.error("createOrEditCourse:no permission");
                                return "no_permission";
                            }
                        }

                    }
                    Optional<CosUserMsg> userMsg = cosUserMsgRepository.findByGuid(course.getCreateById());
                    if (userMsg.isPresent()) {
                        course.setAuthorName(userMsg.get().getRealName());
                        course.setSchoolId(userMsg.get().getSchoolId());
                    }

                }
                course.setCreateTime(new Date());
                course.setDiscount(1);
                if (course.getType() == 3) {
                    //为图灵优选课程
                    course.setPrice(course.getPrice());
                } else {
                    course.setPrice(0);
                }
                course.setStatus(0);
                course.setIsDeleted(0);
                course.setRandomNum(new Random().nextInt(51) + 150);
                course.setIsSelected(0);
                CosCourse save = cosCourseRepository.save(course);
                courseId = save.getId();
            } else {
                //编辑
                Optional<CosCourse> cosCourse = cosCourseRepository.findById(course.getId());
                courseId = course.getId();
                if (cosCourse.isPresent()) {
                    cosCourse.get().setName(course.getName());
                    cosCourse.get().setDirection(course.getDirection());
                    if (cosCourse.get().getType() == 2) {
                        //平台课程
                        cosCourse.get().setAuthorName(course.getAuthorName());
                    }
                    cosCourse.get().setForm(course.getForm());
                    cosCourse.get().setCoverDesc(course.getCoverDesc());
                    cosCourse.get().setCoverUrl(course.getCoverUrl());
                    cosCourse.get().setBriefTxt(course.getBriefTxt());
                    cosCourse.get().setCodeUrl(course.getCodeUrl());
                    if (course.getType() == 3) {
                        //为图灵优选课程
                        cosCourse.get().setPrice(course.getPrice());
                    }
                    cosCourseRepository.save(cosCourse.get());
                }
            }
            HttpUtil.sendGetRequest(refreshCourseDetailUrl + courseId);
            result.put("courseId", courseId);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("createOrEditCourse exception{}", e);
            return "failure";
        }
    }

    /**
     * 后台-课程基本信息返显
     *
     * @param courseId
     * @return
     */
    @Override
    public Object showCourse(String courseId) {
        try {
            Optional<CosCourse> course = cosCourseRepository.findById(courseId);
            if (null != course) {
                return course;
            } else {
                return "failure";
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("showCourse exception{}", e);
            return "failure";
        }
    }

    /**
     * 删除课程
     *
     * @param courseId
     * @return
     */
    @Transactional
    @Override
    public Object deleteCourse(String courseId) {
        try {
            Optional<CosCourse> course = cosCourseRepository.findById(courseId);
            if (course.isPresent()) {
                //删除课程
                course.get().setIsDeleted(1);
                cosCourseRepository.save(course.get());
                //删除学习进度
                cosProgressRepository.deleteAllByCourseId(courseId);
                //删除试卷
                cosPaperRepository.updateIsDeleteByCourseId(1, courseId);
            } else {
                return "不存在该id对应的课程";
            }
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("deleteCourse exception{}", e);
            return "删除时异常";
        }
    }

    /**
     * 上/下架课程
     *
     * @param courseId
     * @param operateType
     * @return
     */
    @Override
    public Object changeStatus(String courseId, int operateType) {
        //operateType 0:下线 1：上线
        Optional<CosCourse> course = cosCourseRepository.findById(courseId);
        if (course.isPresent()) {
            course.get().setStatus(operateType);
            cosCourseRepository.save(course.get());
            return "success";
        } else {
            return "不存在该id对应的课程";
        }
    }

    /**
     * 后台-课程列表
     *
     * @param params
     * @return
     */
    @Override
    public Object cmsCourseList(JSONObject params) {
        try {
            Integer pageNum = params.getInteger("pageNum");
            Integer pageSize = params.getInteger("pageSize");
            String guid = params.getString("guid");
            Integer type = params.getInteger("type");
            Integer form = -1;
            if (3 == type) {
                //如果是图灵严选课程
                form = params.getInteger("form");
            }
            String schoolId = null;
            if (type == 1 && cosUserMsgRepository.findByGuid(guid).isPresent()) {
                schoolId = cosUserMsgRepository.findByGuid(guid).get().getSchoolId();
            }
            PageHelper.startPage(pageNum, pageSize);
            List<CmsCourseDTO> cmsCourseList = cosCourseMapper.getCmsCourseList(guid, type, form, schoolId);
            PageInfo<CmsCourseDTO> pageInfo = new PageInfo<>(cmsCourseList);
            return pageInfo;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("cmsCourseList exception{}", e);
            return "failure";
        }
    }

    @Override
    public JSONObject getStudentCommit(String guid, String schoolCourseId, int pageNum, int pageSize) {
        JSONObject data = new JSONObject();
        PageHelper.startPage(pageNum, pageSize);
        data.put("page", new PageInfo(cosContainerMapper.getStudentCommit(schoolCourseId, guid)));
        data.put("name", cosContainerMapper.getCourseName(schoolCourseId));
        return data;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveScore(String id, int score) {
        cosContainerRepository.findById(id).ifPresent(cosContainer -> {
            cosContainer.setScore(score);
            cosContainerRepository.save(cosContainer);
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void displayOrHideCourse(String schoolCourseId, int state) {
        cosSchoolCourseRepository.findById(schoolCourseId).ifPresent(cosSchoolCourse -> {
            cosSchoolCourse.setState(state);
            cosSchoolCourseRepository.save(cosSchoolCourse);
        });
    }


}
