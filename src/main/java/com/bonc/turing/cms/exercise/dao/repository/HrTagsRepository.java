package com.bonc.turing.cms.exercise.dao.repository;

import com.bonc.turing.cms.exercise.domain.HrTags;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author lky
 * @desc
 * @date 2020/2/25 10:02
 */
public interface HrTagsRepository extends JpaRepository<HrTags,Integer>, JpaSpecificationExecutor<HrTags> {

    List<HrTags> findByTagNameAndTagType(String tagName, String tagType);

    List<HrTags> findByTagTypeOrderByUsedNumDesc(String tagType, Pageable pageable);

    List<HrTags> findByTagTypeAndAndTagNameLike(String tagType, String tagName);
}
