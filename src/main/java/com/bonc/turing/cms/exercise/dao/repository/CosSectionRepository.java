package com.bonc.turing.cms.exercise.dao.repository;

import com.bonc.turing.cms.exercise.domain.CosSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CosSectionRepository extends JpaRepository<CosSection,String>, JpaSpecificationExecutor<CosSection> {

    @Query(value = "select cs.* from cos_section cs left join cos_chapter cc on cc.id = cs.chapter_id where cc.course_id = :courseId order by cs.sort", nativeQuery = true)
    List<CosSection> getCosSectionByCourseId(@Param("courseId") String courseId);

    @Query(value = "SELECT cs.id FROM cos_section cs LEFT JOIN cos_chapter cc ON cs.chapter_id = cc.id WHERE cc.id in (:chapterIds)", nativeQuery = true)
    List<String> getSectionIdsByChapterIds(List<Long> chapterIds);

    void deleteByIdIn(List<String> ids);

}
