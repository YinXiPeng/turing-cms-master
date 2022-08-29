package com.bonc.turing.cms.exercise.dao.repository;

import com.bonc.turing.cms.exercise.domain.CosSchool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CosSchoolRepository extends JpaRepository<CosSchool,String>, JpaSpecificationExecutor<CosSchool> {

    CosSchool findBySchoolDomainName(String schoolDomainName);

    CosSchool findBySchoolName(String schoolName);
}
