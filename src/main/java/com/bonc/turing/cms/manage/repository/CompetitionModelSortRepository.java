package com.bonc.turing.cms.manage.repository;

import com.bonc.turing.cms.manage.entity.competition.CompetitionModelSort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * @ProjectName: cms
 * @Package: com.bonc.turing.cms.manage.repository
 * @ClassName: CompetitionModelSortRepository
 * @Author: bxt
 * @Description:
 * @Date: 2019/7/19 11:10
 * @Version: 1.0
 */
public interface CompetitionModelSortRepository extends JpaRepository<CompetitionModelSort, Long>, JpaSpecificationExecutor<CompetitionModelSort> {

    @Query(value = "select * from  notebook_sort where notebook_id = :notebookId", nativeQuery = true)
    Optional<CompetitionModelSort> getCompetitionModelSortById(@Param("notebookId") String notebookId);

}
