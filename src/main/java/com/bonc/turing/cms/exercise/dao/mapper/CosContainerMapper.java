package com.bonc.turing.cms.exercise.dao.mapper;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CosContainerMapper {
    @Select("select csc.id, csc.file_name as fileName, csc.file_path as filePath, csc.score, csc.file_upload_time, cum.people_id as number, cum.real_name as name from cos_container csc left join cos_user_msg cum on csc.guid = cum.guid where csc.teacher_id = #{guid} and csc.school_course_id = #{schoolCourseId} order by csc.file_upload_time desc")
    List<JSONObject> getStudentCommit(@Param("schoolCourseId") String schoolCourseId, @Param("guid")String guid);

    @Select("select cc.name from cos_course cc left join cos_school_course csc on cc.id = csc.course_id where csc.id = #{schoolCourseId}")
    String getCourseName(@Param("schoolCourseId")String schoolCourseId);
}
