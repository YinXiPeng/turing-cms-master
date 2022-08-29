package com.bonc.turing.cms.exercise.dao.repository;

import com.bonc.turing.cms.exercise.domain.CosHeadImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CosHeadImageRepository extends JpaRepository<CosHeadImage,String>, JpaSpecificationExecutor<CosHeadImage> {

    List<CosHeadImage> findBySchoolIdAndStatusOrderByCreatedTimeDesc(String schoolId,Integer status);

    List<CosHeadImage> findBySchoolIdOrderByCreatedTimeDesc(String schoolId);
}
