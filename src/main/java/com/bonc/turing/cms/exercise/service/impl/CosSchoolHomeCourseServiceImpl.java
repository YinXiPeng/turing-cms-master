package com.bonc.turing.cms.exercise.service.impl;

import com.bonc.turing.cms.exercise.dao.repository.CosCourseRepository;
import com.bonc.turing.cms.exercise.dao.repository.CosSchoolHomeCourseRepository;
import com.bonc.turing.cms.exercise.domain.CosSchoolHomeCourse;
import com.bonc.turing.cms.exercise.service.CosNewsService;
import com.bonc.turing.cms.exercise.service.CosSchoolHomeCourseService;
import com.bonc.turing.cms.manage.ResultEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

/**
 * @author yh
 * @desc 高校首页-展示课程相关
 * @date 2020.06.28
 */
@Service
public class CosSchoolHomeCourseServiceImpl implements CosSchoolHomeCourseService {

    private static Logger logger = LoggerFactory.getLogger(CosNewsService.class);

    @Autowired
    private CosCourseRepository cosCourseRepository;

    @Autowired
    private CosSchoolHomeCourseRepository cosSchoolHomeCourseRepository;

    /**
     * 可选的课程列表
     *
     * @param schoolId
     * @return
     */
    @Override
    public Object showOptionalCourseList(String schoolId) {
        return cosCourseRepository.findAllOptionalCourse(schoolId);
    }


    /**
     * 添加/编辑首页展示课程
     *
     * @param cosSchoolHomeCourse
     * @return
     */
    @Override
    public Object addOrEditSchoolHomeCourse(CosSchoolHomeCourse cosSchoolHomeCourse) {
        try {
            if (null == cosSchoolHomeCourse.getId() || "".equals(cosSchoolHomeCourse.getId())) {
                //新建
                cosSchoolHomeCourse.setCreateTime(new Date());
                cosSchoolHomeCourse.setUpdateTime(new Date());
            } else {
                //编辑
                Optional<CosSchoolHomeCourse> course = cosSchoolHomeCourseRepository.findById(cosSchoolHomeCourse.getId());
                if (course.isPresent()) {
                    cosSchoolHomeCourse.setCreateTime(course.get().getCreateTime());
                }
                cosSchoolHomeCourse.setUpdateTime(new Date());
            }
            cosSchoolHomeCourseRepository.save(cosSchoolHomeCourse);
            return ResultEntity.ResultStatus.OK.getMsg();
        } catch (Exception e) {
            logger.error("addOrEditSchoolHomeCourse exception", e);
            return ResultEntity.ResultStatus.FAILURE.getMsg();
        }
    }

    /**
     * 删除首页展示课程
     *
     * @param id
     * @return
     */
    @Override
    public Object deleteSchoolHomeCourse(String id) {
        try {
            cosSchoolHomeCourseRepository.deleteById(id);
            return ResultEntity.ResultStatus.OK.getMsg();
        } catch (Exception e) {
            logger.error("deleteSchoolHomeCourse exception", e);
            return ResultEntity.ResultStatus.FAILURE.getMsg();
        }
    }

    /**
     * 显示首页课程展示列表
     *
     * @param schoolId
     * @return
     */
    @Override
    public Object showCmsSchoolHomeCourseList(String schoolId) {
        return cosSchoolHomeCourseRepository.findAllBySchoolId(schoolId);
    }
}
