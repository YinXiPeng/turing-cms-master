package com.bonc.turing.cms.exercise.dao.repository;

import com.bonc.turing.cms.exercise.domain.CosChapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CosChapterRepository extends JpaRepository<CosChapter,Long>, JpaSpecificationExecutor<CosChapter> {

    @Query(value = "SELECT cc.course_id FROM cos_chapter cc LEFT JOIN cos_section cs ON cs.chapter_id = cc.id WHERE cs.id = :sectionId", nativeQuery = true)
    String getCourseIdBySectionId(@Param("sectionId") String sectionId);

    List<CosChapter> findByCourseId(String courseId);

    void deleteByIdIn(List<Long> chapterIds);
}
