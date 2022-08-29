package com.bonc.turing.cms.exercise.service;


import com.bonc.turing.cms.exercise.domain.CosSchoolHomeCourse;

/**
 * @desc 高校首页-展示课程相关
 * @author  yh
 * @date 2020.06.28
 */
public interface CosSchoolHomeCourseService {
    /**
     * 可选的课程列表
     * @param schoolId
     * @return
     */
    Object showOptionalCourseList(String schoolId);


    /**
     * 添加/编辑首页展示课程
     * @param cosSchoolHomeCourse
     * @return
     */
    Object addOrEditSchoolHomeCourse(CosSchoolHomeCourse cosSchoolHomeCourse);

    /**
     * 删除首页展示课程
     * @param id
     * @return
     */
    Object deleteSchoolHomeCourse(String id);


    /**
     * 显示首页课程展示列表
     * @param schoolId
     * @return
     */
    Object showCmsSchoolHomeCourseList(String schoolId);




}
