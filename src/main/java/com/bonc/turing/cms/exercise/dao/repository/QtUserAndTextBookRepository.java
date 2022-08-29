package com.bonc.turing.cms.exercise.dao.repository;

import com.bonc.turing.cms.exercise.domain.QtUserAndTextBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QtUserAndTextBookRepository extends JpaRepository<QtUserAndTextBook,String>,JpaSpecificationExecutor<QtUserAndTextBook> {



    List<QtUserAndTextBook> findAllByTextBookIdAndRole(String textBookId, int role);

    QtUserAndTextBook findByTextBookIdAndGuidAndRole(String textBookId, String guid, int role);

    QtUserAndTextBook findByTextBookIdAndGuid(String textBookId, String guid);

    void deleteAllByRoleAndTextBookId(int role, String textBookId);

    void deleteAllByGuidAndRole(String guid,int role);

}
