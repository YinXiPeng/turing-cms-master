package com.bonc.turing.cms.home.repository;

import com.bonc.turing.cms.home.domain.NotebookLikes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotebookLikesRepository extends JpaRepository<NotebookLikes,String> {

    NotebookLikes findByNotebookIdAndGuid(String noteBookId,String guid);

    int countAllByNotebookId(String noteBookId);

}
