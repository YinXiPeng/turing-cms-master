package com.bonc.turing.cms.manage.repository;

import com.bonc.turing.cms.manage.entity.CmsCompetitionScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CompetitionScoreRepository extends JpaRepository<CmsCompetitionScore,String> {

//    @Query(value = "select new com.bonc.turing.cms.manage.dto.CompetitionScoreDTO(tc.) from TeamsCompetitionTest tc , UserPaticipateCompetition uc where tc.competitionId =?1 and uc.competitionId =?1")
//    Page<CompetitionScoreDTO> findByCompetitionId(String competitionId , Pageable pageable);


        String deleteByScoreId(String scoreId);

        @Query(value = "select c from CmsCompetitionScore  c where c.competitionId=?1 and c.teamId=?2 and c.finalFlag=0")
        CmsCompetitionScore findPreByCompetitionIdAndTeamId(String competitionId,String teamId);

        @Query(value = "select c from CmsCompetitionScore  c where c.competitionId=?1 and c.teamId=?2 and c.finalFlag=1")
        CmsCompetitionScore findFinalByCompetitionIdAndTeamId(String competitionId,String teamId);

        @Query(value = "select c from CmsCompetitionScore  c where c.competitionId=?1 and c.teamId=?2 and c.finalFlag=2")
        CmsCompetitionScore findCommonByCompetitionIdAndTeamId(String competitionId,String teamId);


        @Query(value = "select c from CmsCompetitionScore  c where c.competitionId=?1 and c.finalFlag=1")
        List<CmsCompetitionScore> findFinalByCompetitionId(String competitionId);

        @Query(value = "select c from CmsCompetitionScore  c where c.competitionId=?1 and c.finalFlag<>1")
        List<CmsCompetitionScore> findOtherByCompetitionId(String competitionId);

        CmsCompetitionScore findByScoreId(String scoreId);
}
