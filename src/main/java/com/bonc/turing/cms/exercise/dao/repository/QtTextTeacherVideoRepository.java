package com.bonc.turing.cms.exercise.dao.repository;

import com.bonc.turing.cms.exercise.domain.QtTextTeacherVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QtTextTeacherVideoRepository extends JpaRepository<QtTextTeacherVideo,String>,JpaSpecificationExecutor<QtTextTeacherVideo> {


    List<QtTextTeacherVideo> findAllByGuid(String guid);

    void deleteAllByGuid(String guid);
}
