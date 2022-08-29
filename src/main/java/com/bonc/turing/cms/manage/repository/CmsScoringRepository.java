package com.bonc.turing.cms.manage.repository;

import com.bonc.turing.cms.manage.entity.competition.CmsScoring;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * @ProjectName: cms
 * @Package: com.bonc.turing.cms.manage.repository
 * @ClassName: CmsScoringRepository
 * @Author: bxt
 * @Description: 评分项
 * @Date: 2019/7/13 11:00
 * @Version: 1.0
 */
public interface CmsScoringRepository extends JpaRepository<CmsScoring, String> {
    @Modifying
    @Query(value = "delete from cms_scoring where competition_info_id = :competitionId", nativeQuery = true)
    void deleteByCompetitionId(@Param("competitionId") String competitionId);

    @Query(value = "select s from CmsScoring s where s.competitionInfoId = ?1 and s.competitionInfoType=0")
    List<CmsScoring> findCommonByCompetitionInfoId(String competitionInfoId);

    @Query(value = "select s from CmsScoring s where s.competitionInfoId = ?1 and s.competitionInfoType=1")
    List<CmsScoring> findPreByCompetitionInfoId(String competitionInfoId);

    @Query(value = "select s from CmsScoring s where s.competitionInfoId = ?1 and s.competitionInfoType=2")
    List<CmsScoring> findFinalByCompetitionInfoId(String competitionInfoId);

    @Query(value = "SELECT s from CmsScoring s  WHERE s.competitionInfoId = ?1 and s.scoringItem=?2 and s.competitionInfoType=?3")
    CmsScoring findByCompetitionInfoIdAndScoringItemAndCompetitionInfoType(String competitionId , String scoringItem,int competitionInfoType);


    @Query(value = "select * from  cms_scoring where competition_info_id = :competitionId and competition_info_type = :competitionInfoType", nativeQuery = true)
    List<CmsScoring> getCmsScoringListByCompetitionIdAndCompetitionInfoType(@Param("competitionId") String competitionId, @Param("competitionInfoType") int competitionInfoType);
}
