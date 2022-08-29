package com.bonc.turing.cms.exercise.dao.repository;

import com.bonc.turing.cms.exercise.domain.QtAnswerRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author lky
 * @desc
 * @date 2019/12/30 16:09
 */
public interface QtAnswerRecordRepository extends JpaRepository<QtAnswerRecord,Integer>,JpaSpecificationExecutor<QtAnswerRecord> {

    List<QtAnswerRecord> findByGuidAndReferIdAndChapterIdAndBeginPageAndEndPageAndQuestionId(String guid, String referId, String chapterId, Integer beginPage, Integer endPage, String questionId);

    void deleteAllByChapterId(String chapterId);

    int deleteByQuestionId(String questionId);
}
