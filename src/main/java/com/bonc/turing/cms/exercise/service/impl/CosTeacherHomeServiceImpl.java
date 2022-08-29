package com.bonc.turing.cms.exercise.service.impl;

import com.bonc.turing.cms.exercise.dao.repository.CosTeacherHomeRepository;
import com.bonc.turing.cms.exercise.dao.repository.CosUserMsgRepository;
import com.bonc.turing.cms.exercise.domain.CosTeacherHome;
import com.bonc.turing.cms.exercise.domain.CosUserMsg;
import com.bonc.turing.cms.exercise.service.CosTeacherHomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CosTeacherHomeServiceImpl implements CosTeacherHomeService {

    @Autowired
    private CosTeacherHomeRepository cosTeacherHomeRepository;
    @Autowired
    private CosUserMsgRepository cosUserMsgRepository;

    @Override
    public Object getTeacherList(String guid, String schoolId,Integer pageNum,Integer pageSize) {
        Page<CosTeacherHome> teacherHomeList;
        Pageable page = PageRequest.of(pageNum,pageSize);
        teacherHomeList = cosTeacherHomeRepository.findBySchoolId(schoolId,page);
        return teacherHomeList;
    }

    @Override
    public void setTeacherMsg(CosTeacherHome cosTeacherHome) {
        String id = cosTeacherHome.getId();
        if (null==id||"".equals(id)){
            //新增
            cosTeacherHome.setCreatedTime(new Date());
        }
        cosTeacherHomeRepository.save(cosTeacherHome);
    }

    @Override
    public void delTeacher(String teacherId) {
        cosTeacherHomeRepository.deleteById(teacherId);
    }
}
