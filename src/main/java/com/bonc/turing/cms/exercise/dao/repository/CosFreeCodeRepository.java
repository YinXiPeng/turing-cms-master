package com.bonc.turing.cms.exercise.dao.repository;

import com.bonc.turing.cms.exercise.domain.CosFreeCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author  yh
 * @desc 课程兑换码
 */
public interface CosFreeCodeRepository extends JpaRepository<CosFreeCode,String> {

    CosFreeCode findByCourseIdAndCode(String courseId,String code);

    List<CosFreeCode> findAllByCourseId(String courseId);
}
