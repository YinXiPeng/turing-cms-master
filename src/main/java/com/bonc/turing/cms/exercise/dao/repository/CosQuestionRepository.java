package com.bonc.turing.cms.exercise.dao.repository;

import com.bonc.turing.cms.exercise.domain.CosQuestion;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * description:
 *
 * @author lxh
 * @date 2020/3/23 12:17
 */
public interface CosQuestionRepository extends PagingAndSortingRepository<CosQuestion, Long> {

    /**
     * 根据试卷ID查询试题列表
     *
     * @param cosPaperId
     * @return
     */
    List<CosQuestion> findByCosPaperId(String cosPaperId);

    /**
     * 根据试卷ID删除试题
     *
     * @param cosPaperId
     */
    @Modifying
    void deleteByCosPaperId(String cosPaperId);
}
