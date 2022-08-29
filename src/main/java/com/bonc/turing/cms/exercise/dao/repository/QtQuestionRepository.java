package com.bonc.turing.cms.exercise.dao.repository;

import com.bonc.turing.cms.exercise.domain.QtQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @author lky
 * @desc
 * @date 2019/12/26 16:18
 */
public interface QtQuestionRepository extends JpaRepository<QtQuestion,String>,JpaSpecificationExecutor<QtQuestion> {

    @Modifying
    @Query(value = "UPDATE qt_question SET question_status=?2 WHERE qid=?1",nativeQuery = true)
    int updateQuestionStatus(String qid, Integer status);
}
