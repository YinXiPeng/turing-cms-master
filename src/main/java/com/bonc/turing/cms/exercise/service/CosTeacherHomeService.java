package com.bonc.turing.cms.exercise.service;

import com.bonc.turing.cms.exercise.domain.CosTeacherHome;


public interface CosTeacherHomeService {
    Object getTeacherList(String guid, String schoolId,Integer pageNum,Integer pageSize);

    void setTeacherMsg(CosTeacherHome cosTeacherHome);

    void delTeacher(String teacherId);
}
