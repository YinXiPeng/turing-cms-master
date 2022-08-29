package com.bonc.turing.cms.exercise.dao.repository;

import com.bonc.turing.cms.exercise.domain.CosExercises;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CosExercisesRepository extends JpaRepository<CosExercises,Long>, JpaSpecificationExecutor<CosExercises> {

    @Query(value = "SELECT ce.* FROM cos_exercises ce LEFT JOIN cos_section cs ON ce.section_id = cs.id LEFT JOIN cos_chapter cc ON cs.chapter_id = cc.id WHERE cc.course_id = :courseId", nativeQuery = true)
    List<CosExercises> getCosQuestionByCourseId(@Param("courseId") String courseId);

    @Query(value = "select ce.* from cos_exercises ce left join cos_section cs on ce.section_id = cs.id where cs.id = :sectionId", nativeQuery = true)
    List<CosExercises> getCosQuestionBySectionId(@Param("sectionId") String sectionId);

    void deleteByIdIn(List<Long> questionIds);
}
