package com.bonc.turing.cms.exercise.dao.repository;

import com.bonc.turing.cms.exercise.domain.CosExercisesOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface CosExercisesOptionRepository extends JpaRepository<CosExercisesOption,Long>, JpaSpecificationExecutor<CosExercisesOption> {

    @Query(value = "SELECT co.* FROM cos_exercises_option co LEFT JOIN cos_exercises ce ON co.question_id = ce.id LEFT JOIN cos_section cs ON ce.section_id = cs.id LEFT JOIN cos_chapter cc ON cs.chapter_id = cc.id WHERE cc.course_id = :courseId", nativeQuery = true)
    List<CosExercisesOption> getCosOptionByCourseId(@Param("courseId") String courseId);

    @Query(value = "SELECT co.* FROM cos_exercises_option co LEFT JOIN cos_exercises ce on co.question_id = ce.id WHERE ce.section_id = :sectionId", nativeQuery = true)
    List<CosExercisesOption> getCosOptionBySectionId(@Param("sectionId") String sectionId);

    @Query(value = "SELECT co.id FROM cos_exercises_option co LEFT JOIN cos_exercises ce on co.question_id = ce.id WHERE ce.id in (:questionIds)", nativeQuery = true)
    List<Long> getOptionIdsByQuestionIds(List<Long> questionIds);

    void deleteByIdIn(List<Long> OptionIds);
}
