package com.bonc.turing.cms.exercise.dao.repository;


import com.bonc.turing.cms.exercise.domain.QtTeacherInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface QtTeacherRepository extends JpaRepository<QtTeacherInfo,String>,JpaSpecificationExecutor<QtTeacherInfo> {

    List<QtTeacherInfo> findAllByFlagAndState(int flag,String state);

    QtTeacherInfo findByGuidAndFlag(String guid, int flag);
}
