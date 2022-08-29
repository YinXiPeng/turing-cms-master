package com.bonc.turing.cms.exercise.dao.repository;

import com.bonc.turing.cms.exercise.domain.CosNews;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * @desc 高校资讯
 * @author yh
 * @date 2020.06.28
 */
public interface CosNewsRepository extends JpaRepository<CosNews,String> {

    /**
     * 根据学校id查询资讯列表
     * @param schoolId
     * @param pageable
     * @return
     */
    Page<CosNews> findAllBySchoolIdAndNewsType(String schoolId,Integer newsType, Pageable pageable);

}
