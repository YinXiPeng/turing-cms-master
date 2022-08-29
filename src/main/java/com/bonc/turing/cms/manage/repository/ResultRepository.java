package com.bonc.turing.cms.manage.repository;

import com.bonc.turing.cms.manage.entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ResultRepository  extends JpaRepository<Result,String> {

    List<Result> findByLastModifiedByIdAndCompetitionInfoId(String lastModifiedById,String competitionInfoId);

    @Query(value = "select r from Result r where r.createdById = ?1 and r.competitionInfoId = ?2 and r.stageType = 'COMMON'")
    List<Result> findByCreatedByIdAndCompetitionInfoId(String CreatedById,String competitionInfoId);

    @Query(value = "select r from Result r where r.createdById = ?1 and r.competitionInfoId = ?2 and r.stageType = 'PRELIMINARY'")
    List<Result>  findByCreatedByIdAndCompetitionInfoIdPre(String CreatedById,String competitionInfoId);

    @Query(value = "select r from Result r where r.createdById = ?1 and r.competitionInfoId = ?2 and r.stageType = 'FINALS'")
    List<Result>  findByCreatedByIdAndCompetitionInfoIdFinal(String CreatedById,String competitionInfoId);


    @Transactional
    @Modifying
    @Query(value = "update  d_s_result set scoreb = ?1,final_submit = 1 where id =?2",nativeQuery = true)
    void updateScoreB(String scoreb,String id);

    @Transactional
    @Modifying
    @Query(value = "update  d_s_result set scoreb = -1,final_submit = 1 where id =?1",nativeQuery = true)
    void deleteScoreB(String id);

}
