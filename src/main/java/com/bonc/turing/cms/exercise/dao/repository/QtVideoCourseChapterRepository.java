package com.bonc.turing.cms.exercise.dao.repository;

import com.bonc.turing.cms.exercise.domain.QtVideoCourseChapter;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface QtVideoCourseChapterRepository extends PagingAndSortingRepository<QtVideoCourseChapter, Long> {


    List<QtVideoCourseChapter> findByTextBookId(String textBookId);

    List<QtVideoCourseChapter> findAllByTextBookIdAndType(String textBookId,int type);

    void deleteByTextBookId(String textBookId);

}
