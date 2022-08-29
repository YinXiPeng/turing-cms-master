package com.bonc.turing.cms.exercise.dao.repository;

import com.bonc.turing.cms.exercise.domain.CosCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @desc 课程表
 * @author yh
 * @date 2020.03.24
 */
@Repository
public interface CosCourseRepository extends JpaRepository<CosCourse, String> {
    /**
     * 根据学校ID查询课程列表 上架且未删除
     *
     * @param schoolId
     * @return
     */
    @Query("select cc from CosCourse cc where cc.schoolId = :schoolId and cc.status = 1 and cc.isDeleted=0")
    List<CosCourse> findBySchoolId(@Param("schoolId") String schoolId);

    /**
     * 查询高校首页可选展示课程（本校课程+平台课程）
     * @param schoolId
     * @return
     */
    @Query(value = "SELECT id as courseId,`name` as courseName from cos_course \n" +
            "WHERE ( (type=2 and form!=5 and price>0) or (type=2 and form=5 and id in (SELECT course_id from cos_school_course  \n" +
            "WHERE school_id=:schoolId) ) \n" +
            "or (type=1 and school_id=:schoolId) ) \n" +
            "and `status`=1 and is_deleted=0",nativeQuery = true)
    List<Map> findAllOptionalCourse(@Param("schoolId") String schoolId);
}
