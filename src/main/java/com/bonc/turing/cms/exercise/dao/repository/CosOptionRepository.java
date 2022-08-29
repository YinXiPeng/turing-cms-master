package com.bonc.turing.cms.exercise.dao.repository;

import com.bonc.turing.cms.exercise.domain.CosOption;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

/**
 * description:
 *
 * @author lxh
 * @date 2020/3/23 12:15
 */
public interface CosOptionRepository extends PagingAndSortingRepository<CosOption, Long> {
    /**
     * 根据试题ID删除试题选项
     *
     * @param cosQuestionId
     */
    @Modifying
    void deleteByCosQuestionId(Long cosQuestionId);

    List<CosOption> findByCosQuestionId(Long id);

    @Query(nativeQuery = true, value = "select ho.* from cos_question hq left join cos_Option ho on hq.id = ho.cos_question_id where hq.cos_paper_id = :paperId and hq.type = 0")
    List<CosOption> findByCosPaperId(@Param("paperId") String paperId);


    List<CosOption> findByCosQuestionIdIn(Set<Long> questionIds);
}
