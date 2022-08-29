package com.bonc.turing.cms.exercise.dao.repository;

import com.bonc.turing.cms.exercise.domain.CosTeacherHome;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface CosTeacherHomeRepository extends JpaRepository<CosTeacherHome,String>, JpaSpecificationExecutor<CosTeacherHome> {

    Page<CosTeacherHome> findBySchoolIdAndStatus(String schoolId, Integer status, Pageable page);

    Page<CosTeacherHome> findBySchoolId(String schoolId, Pageable page);
}
