package com.bonc.turing.cms.exercise.dao.repository;

import com.bonc.turing.cms.exercise.domain.QtTextBookPaging;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author lky
 * @desc
 * @date 2019/12/30 18:49
 */
public interface QtTextBookPagingRepository extends JpaRepository<QtTextBookPaging,Integer>,JpaSpecificationExecutor<QtTextBookPaging> {

    QtTextBookPaging findByTextBookIdAndChapterIdAndBeginPageAndEndPage(String textBookId, String chapterId, Integer beginPage, Integer endPage);

    List<QtTextBookPaging> findByTextBookIdOrderBySortAscBeginPageAscEndPageAsc(String textBookId);

    int deleteByChapterId(String chapterId);

    List<QtTextBookPaging> findByChapterId(String chapterId);
}
