package com.bonc.turing.cms.exercise.dao.mapper;


import com.bonc.turing.cms.exercise.domain.QtTeacherInfo;
import com.bonc.turing.cms.exercise.domain.QtTeacherTryCourse;
import com.bonc.turing.cms.exercise.dto.TeacherListDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QtTeacherMapper {


    @Select("select a.guid,a.name,a.real_name as realName,a.id_card as idCard,a.phone,count(distinct b.room_number) as liveNum,count(distinct c.course_id) as bookNum,a.stars,a.level,a.is_online \n" +
            " FROM qt_teacher_info a \n" +
            " left join qt_live_record b on a.guid = b.tea_guid \n" +
            " left join cos_progress c on a.guid = c.guid\n" +
            " where a.flag=1 and a.state='1'\n" +
            " group by a.guid")
    List<TeacherListDTO> getTeacherList();

    @Select("select a.guid as guid,a.name as name ,a.real_name as realName,a.id_card as idCard,a.phone as phone,a.state from qt_teacher_info a where a.flag=1 and a.state=#{state}")
    List<QtTeacherInfo> getPendingTeacherList(String state);

    @Select("select a.guid as guid,a.name as name,a.real_name as realName,a.id_card as idCard,a.phone as phone,a.state  from qt_teacher_info a where a.flag=1 and (a.state='0' or a.state='2')")
    List<QtTeacherInfo> getPendingOrFailureTeacherList();

    @Select("SELECT id,name as bookName from cos_course  WHERE id = #{id}")
    QtTeacherTryCourse getTryCourse(String id);

    @Select("SELECT id,name as bookName from cos_course  WHERE (type = 2 or type = 3) and status = 1")
    List<QtTeacherTryCourse> getTryCourseByRange();
}

