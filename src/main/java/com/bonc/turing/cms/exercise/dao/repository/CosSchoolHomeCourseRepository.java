package com.bonc.turing.cms.exercise.dao.repository;

import com.bonc.turing.cms.exercise.domain.CosSchoolHomeCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

/**
 * @desc 特定高校首页展示课程dao
 * @author  yh
 * @date 2020.06.28
 *
 */
public interface CosSchoolHomeCourseRepository extends JpaRepository<CosSchoolHomeCourse,String> {

    @Query(value = "SELECT cshc.id,cshc.course_id as courseId,cc.`name` as courseName,UNIX_TIMESTAMP(cshc.update_time)*1000 as createTime from cos_school_home_course cshc \n" +
            "LEFT JOIN cos_course cc on cc.id = cshc.course_id where cshc.school_id=:schoolId",nativeQuery = true)
    List<Map> findAllBySchoolId(String schoolId);
}
