package com.bonc.turing.cms.exercise.dao.repository;

import com.bonc.turing.cms.exercise.domain.CosSchoolCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CosSchoolCourseRepository extends JpaRepository<CosSchoolCourse,String>, JpaSpecificationExecutor<CosSchoolCourse> {
}
