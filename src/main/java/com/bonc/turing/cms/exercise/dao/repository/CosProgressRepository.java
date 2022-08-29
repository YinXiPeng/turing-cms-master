package com.bonc.turing.cms.exercise.dao.repository;

import com.bonc.turing.cms.exercise.domain.CosProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * @desc 学习进度表
 * @author yh
 */
@Repository
public interface CosProgressRepository extends JpaRepository<CosProgress,String> {

    void deleteAllByCourseId(String courseId);


    @Transactional
    @Modifying
    @Query(value = "DELETE from cos_progress WHERE guid = :guid and course_id in (SELECT id FROM cos_course WHERE type = :type and form = :form)",nativeQuery = true)
    void deleteAllByGuid(@Param("guid") String guid, @Param("type") Integer type,@Param("form") Integer form);

    CosProgress findByCourseIdAndGuid(String courseId,String guid);

}
