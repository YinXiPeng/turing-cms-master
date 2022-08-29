package com.bonc.turing.cms.exercise.dao.repository;

import com.bonc.turing.cms.exercise.domain.QtChapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QtChapterRepository extends JpaRepository<QtChapter,String>,JpaSpecificationExecutor<QtChapter> {

    List<QtChapter>  findAllByTextBookId(String textBookId);

    List<QtChapter> findAllByTextBookIdAndTypeOrderBySort(String textBookId,int type);

    List<QtChapter> findAllByTextBookIdAndType(String textBookId, int type);



    void  deleteById(String id);

}
