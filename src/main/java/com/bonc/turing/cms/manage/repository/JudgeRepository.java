package com.bonc.turing.cms.manage.repository;

import com.bonc.turing.cms.manage.entity.Judge;
import com.bonc.turing.cms.manage.entity.competition.CmsCompetitionInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface JudgeRepository extends JpaRepository<Judge, String> {

    @Query(value = "select j from Judge j where j.judgeId = ?1")
    Page<Judge> findByJudgeId(String guid, Pageable pageable);

    @Modifying
    @Query(value = "delete from judge where competition_id= :competitionInfoId and is_final = :isFinal", nativeQuery = true)
    void deleteBycompetitionInfoIdAndIsFinal(@Param("competitionInfoId") String competitionInfoId, @Param("isFinal") int isFinal);

    @Modifying
    @Query(value = "delete from judge where competition_id= :competitionInfoId", nativeQuery = true)
    void deleteBycompetitionInfoId(@Param("competitionInfoId") String competitionInfoId);

    @Query(value = "select * from Judge where competitionId =?1", nativeQuery = true)
    List<Judge> findJudgeByCompetitionId(String competitionId);

    /*@Query(value = "select * from  judge where competition_id = :competitionId", nativeQuery = true)
    Optional<Judge> getJudgeByCompetitionId(@Param("competitionId") String competitionId);*/

    @Query(value = "select * from  judge where competition_id = :competitionId", nativeQuery = true)
    List<Judge> getJudgeByCompetitionId(@Param("competitionId") String competitionId);

    @Query(value = "select * from  judge where competition_id = :competitionId and is_final = :isFinal", nativeQuery = true)
    List<Judge> getJudgeByCompetitionIdAndIsFinal(@Param("competitionId") String competitionId, @Param("isFinal") int isFinal);

    @Modifying
    @Query("delete from Judge  where judgeId = :judgeId")
    Integer deleteJudgeByJudgeId(@Param("judgeId") String judgeId);

    @Query(value = "select j from Judge j where j.judgeId=?1 and competitionId = ?2 and j.isFinal=1")
    Judge findFinalJudgeByJudgeId(String judgeId,String competitionId);

    @Query(value = "select j from Judge j where j.judgeId=?1 and competitionId = ?2  and j.isFinal<>1")
    Judge findOtherByJudgeId(String judgeId,String competitionId);
}
