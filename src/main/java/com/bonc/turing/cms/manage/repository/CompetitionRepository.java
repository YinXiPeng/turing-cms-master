package com.bonc.turing.cms.manage.repository;

import com.bonc.turing.cms.manage.entity.CmsCompetitionScore;
import com.bonc.turing.cms.manage.entity.competition.CmsCompetitionInfo;
import org.hibernate.engine.spi.Mapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;


public interface CompetitionRepository extends JpaRepository<CmsCompetitionInfo, Long>, JpaSpecificationExecutor<CmsCompetitionInfo> {

    @Override
    Page<CmsCompetitionInfo> findAll(Pageable pageable);

    /*@Query(value = "SELECT\n" +
            "\tcompetition_id AS competitionId,\n" +
            "\tcompetition_name AS competitionName,\n" +
            "\tbanner AS banner,\n" +
            "\tlogo AS logo,\n" +
            "\tcompetition_type AS competitionType,\n" +
            "\tis_need_team AS isNeedTeam,\n" +
            "\tteam_person_up AS teamPersonUp,\n" +
            "\tteam_person_down AS teamPersonDown,\n" +
            "\tcompetition_merge AS competitionMerge,\n" +
            "\tenterprise_id AS enterpriseId,\n" +
            "\tenterprise_name AS enterpriseName,\n" +
            "\tcompetition_grade AS competitionGrade,\n" +
            "\taward_money AS awardMoney,\n" +
            "\tcurrency_type AS currencyType,\n" +
            "\tcompetition_system AS competitionSystem,\n" +
            "\tintroduction AS introduction,\n" +
            "\tprize AS prize,\n" +
            "\tproblem_common AS problemCommon,\n" +
            "\tproblem_preliminary AS problemPreliminary,\n" +
            "\tproblem_final AS problemFinal,\n" +
            "\trace_schedule_time AS raceScheduleTime,\n" +
            "\tteam_start_time AS teamStartTime,\n" +
            "\tteam_end_time AS teamEndTime,\n" +
            "\tcommon_rlease_start_date AS commonRleaseStartDate,\n" +
            "\tcommon_rlease_end_date AS commonRleaseEndDate,\n" +
            "\tcommon_begin_date AS commonBeginDate,\n" +
            "\tcommon_end_date AS commonEndDate,\n" +
            "\tcommon_publish_date AS commonPublishDate,\n" +
            "\tpreliminary_rlease_start_date AS preliminaryRleaseStartDate,\n" +
            "\tpreliminary_rlease_end_date AS preliminaryRleaseEndDate,\n" +
            "\tpreliminary_begin_date AS preliminaryBeginDate,\n" +
            "\tpreliminary_end_date AS preliminaryEndDate,\n" +
            "\tpreliminary_publish_date AS preliminaryPublishDate,\n" +
            "\tscoring_type AS scoringType,\n" +
            "\trules AS rules,\n" +
            "\tlabel AS label,\n" +
            "\tdatasource_id AS datasourceId,\n" +
            "\tresult_id AS resultId,\n" +
            "\tcreate_user AS createUser,\n" +
            "\tcreate_time AS createTime,\n" +
            "\tlast_mdify_time AS lastMdifyTime,\n" +
            "\tcompetition_status AS competitionStatus,\n" +
            "\ttenant_id AS tenantId,\n" +
            "\tsubmit_type AS submitType,\n" +
            "\tis_need_judges AS isNeedJudges,\n" +
            "\tfinal_begin_date AS finalBeginDate,\n" +
            "\tfinal_end_date AS finalEndDate,\n" +
            "\tfinal_publish_date AS finalPublishDate,\n" +
            "\tcompetition_level AS competitionLevel,\n" +
            "\tindustry_id AS industryId,\n" +
            "\tteams AS teams,\n" +
            "\tnovice_begin_date AS noviceBeginDate,\n" +
            "\tnovice_cycle AS noviceCycle,\n" +
            "\tnovice_end_date AS noviceEndDate,\n" +
            "\tnovice_issue AS noviceIssue,\n" +
            "\tnovice_publish_date AS novicePublishDate,\n" +
            "\tindustry AS industry,\n" +
            "\tgold_medal_pnumber AS goldMedalPnumber,\n" +
            "\tgold_medal_amount_rate AS goldMedalAmountRate,\n" +
            "\tsilver_medal_pnumber AS silverMedalPnumber,\n" +
            "\tsilver_medal_amount_rate AS silverMedalAmountRate,\n" +
            "\tbronze_medal_pnumber AS bronzeMedalPnumber,\n" +
            "\tbronze_medal_amount_rate AS bronzeMedalAmountRate,\n" +
            "\tscore AS score,\n" +
            "\trank_no AS rankNo,\n" +
            "\talgorithm AS algorithm,\n" +
            "\tdataset_id AS datasetId,\n" +
            "\tcompeting_teams AS competingTeams,\n" +
            "\this_competing_teams AS hisCompetingTeams,\n" +
            "\tis_scale_number AS isScaleNumber,\n" +
            "\tb_score AS bScore,\n" +
            "\tb_rank_no AS bRankNo,\n" +
            "\tis_done_scorea AS isDoneScorea,\n" +
            "\tis_done_scoreb AS isDoneScoreb,\n" +
            "\tis_top AS isTop,\n" +
            "\tsorting AS sorting,\n" +
            "\tis_top_time AS isTopTime,\n" +
            "\tis_join AS isJoin,\n" +
            "\tshow_hide AS showHide,\n" +
            "\tcommon_evaluation_type AS commonEvaluationType,\n" +
            "\tpreliminary_evaluation_type AS preliminaryEvaluationType,\n" +
            "\tfinal_evaluation_type AS finalEvaluationType,\n" +
            "\tparticipants AS participants,\n" +
            "\talternating_group AS alternatingGroup,\n" +
            "\torganizational_unit AS organizationalUnit,\n" +
            "\tcommon_submit_type AS commonSubmitType,\n" +
            "\tpreliminary_submit_type AS preliminarySubmitType,\n" +
            "\tfinal_submit_type AS finalSubmitType \n" +
            "FROM\n" +
            "\tcompetition_info \n" +
            "WHERE\n" +
            "\tcompetition_id = ?1", nativeQuery = true)
    CmsCompetitionInfo findCompetitionById(String competitionId);*/
    @Query(value = "select * from  competition_info where competition_id = :competitionId", nativeQuery = true)
    Optional<CmsCompetitionInfo> getCompetitionInfoByCompetitionId(@Param("competitionId") String competitionId);

    @Modifying
    @Query(value = "update competition_info set show_hide = :showHide where competition_id = :competitionId", nativeQuery = true)
    void updateCompetition(@Param("competitionId")String competitionId,@Param("showHide") int showHide);

    @Modifying
    @Query(value = "delete from competition_info where competition_id = :competitionId", nativeQuery = true)
    void deleteCompetition(String competitionId);

    @Query(value = "select u from CmsCompetitionInfo u where u.competitionId =?1", nativeQuery = true)
    CmsCompetitionInfo getOne(String competitionId);

    @Query(value = "select count(*)+1 as num from competition_info", nativeQuery = true)
    int getSorting();

    @Query(value = "select count(*) as individualNum from user_participate_competition where competition_id = :competitionId", nativeQuery = true)
    int getOneCompeteNumByCompetitionId(@Param("competitionId") String competitionId);

    @Query(value = "select count(*) as teams from teams_competition where competition_id = :competitionId", nativeQuery = true)
    int getTeamCompeteNumByCompetitionId(@Param("competitionId") String competitionId);

    @Query(value = "select count(*)  as num from d_s_result where competition_info_id = :competitionId and(stage_type = 'COMMON' or stage_type = 'OTHER')", nativeQuery = true)
    int getOneCommonSubmitNumByCompetitionId(@Param("competitionId") String competitionId);

    @Query(value = "select count(throws.team_id) from (SELECT distinct a.competition_info_id,b.team_id FROM( SELECT created_by_id AS guid, competition_info_id FROM d_s_result WHERE ( stage_type = 'COMMON' OR stage_type = 'OTHER' ) AND competition_info_id = :competitionId ORDER BY created_by_id ) a LEFT JOIN (SELECT tmp.guid, tc.*  FROM ( SELECT a.team_id, a.competition_id FROM teams_competition a WHERE a.competition_id = :competitionId ) tc LEFT JOIN teams_member_info tmp ON tc.team_id = tmp.teams_info_id  ORDER BY tc.team_id DESC  ) b ON a.guid = b.guid  AND a.competition_info_id = b.competition_id) throws group by throws.competition_info_id", nativeQuery = true)
    int getTeamCommonSubmitNumByCompetitionId(@Param("competitionId") String competitionId);

    @Query(value = "select count(*)  as num from d_s_result where competition_info_id = :competitionId and stage_type = :type", nativeQuery = true)
    int getOneCommonSubmitNumByCompetitionId(@Param("competitionId") String competitionId, @Param("type") String type);

    @Query(value = "select count(throws.team_id) from (SELECT distinct a.competition_info_id,b.team_id FROM( SELECT created_by_id AS guid, competition_info_id FROM d_s_result WHERE stage_type = :type AND competition_info_id = :competitionId ORDER BY created_by_id ) a LEFT JOIN (SELECT tmp.guid, tc.*  FROM ( SELECT a.team_id, a.competition_id FROM teams_competition a WHERE a.competition_id = :competitionId ) tc LEFT JOIN teams_member_info tmp ON tc.team_id = tmp.teams_info_id  ORDER BY tc.team_id DESC  ) b ON a.guid = b.guid  AND a.competition_info_id = b.competition_id) throws group by throws.competition_info_id", nativeQuery = true)
    int getTeamCommonSubmitNumByCompetitionId(@Param("competitionId") String competitionId, @Param("type") String type);

    @Query(value = "select * from competition_info where 1=1 and (if(:competitionOrEnterprise != '',competition_name like CONCAT('%',:competitionOrEnterprise,'%'),1=1) or if(:competitionOrEnterprise != '',enterprise_name like CONCAT('%',:competitionOrEnterprise,'%'),1=1) )and if(:competitionStatus != '',competition_status =:competitionStatus ,1=1)", nativeQuery = true)
    List<CmsCompetitionInfo> getCompetitionList(Pageable pageable, @Param("competitionOrEnterprise") String competitionOrEnterprise, @Param("competitionStatus") String competitionStatus);

    @Query(value = "select count(*) as num from (select competition_id as  competitionId , competition_name as competitionName,enterprise_name as enterpriseName,create_time as   createTime,common_submit_num as commonSubmitNum,preliminary_submit_num as preliminarySubmitNum ,final_submit_num as finalSubmitNum  ,individual_num as individualNum   ,teams from competition_info where 1=1 and (if(:competitionOrEnterprise != '',competition_name like CONCAT('%',:competitionOrEnterprise,'%'),1=1) or if(:competitionOrEnterprise != '',enterprise_name like CONCAT('%',:competitionOrEnterprise,'%'),1=1) )and if(:competitionStatus != '',competition_status =:competitionStatus ,1=1)) a", nativeQuery = true)
    int getCompetitionListCount(@Param("competitionOrEnterprise") String competitionOrEnterprise, @Param("competitionStatus") String competitionStatus);

    @Modifying
    @Query(value = "delete from d_s_data_source_def_info where competition_info_id = :competitionId", nativeQuery = true)
    void deleteDataSourceDefInfo(@Param("competitionId") String competitionId);

   /* @Modifying
    @Query(value = "delete from d_s_data_source_def where competition_info_id = :competitionId", nativeQuery = true)
    void deleteDataSourceDef(@Param("competitionId") String competitionId);*/

    @Modifying
    @Query(value = "delete from judge where competition_id = :competitionId", nativeQuery = true)
    void deleteJudge(@Param("competitionId") String competitionId);

    @Modifying
    @Query(value = "delete from user_participate_competition where competition_id = :competitionId", nativeQuery = true)
    void deleteUserParticipateCompetition(@Param("competitionId") String competitionId);

    @Modifying
    @Query(value = "delete from d_s_result where competition_info_id = :competitionId", nativeQuery = true)
    void deletResult(@Param("competitionId") String competitionId);
 @Modifying
    @Query(value = "delete from d_s_result_copy where competition_info_id = :competitionId", nativeQuery = true)
    void deletResultCopy(@Param("competitionId") String competitionId);

    @Query(value = "SELECT cei.job_experience_id as jobExperienceId, cei.enterprise_id as enterpriseId, " +
            "cei.job_number as jobNumber, cei.email  as mail, cei.real_name as realName, sui.username, " +
            "sui.guid, sui.phone FROM crd_enterprise_info cei LEFT JOIN sys_user_info sui " +
            "ON cei.guid = sui.guid where sui.guid is not null " +
            "and ( if(:judgeDesc != '',sui.username like CONCAT('%',:judgeDesc,'%'),1=1)" +
            "or if(:judgeDesc != '',sui.phone like CONCAT('%',:judgeDesc,'%'),1=1)" +
            "or if(:judgeDesc != '',cei.real_name like CONCAT('%',:judgeDesc,'%'),1=1)" +
            "or if(:judgeDesc != '',cei.email like CONCAT('%',:judgeDesc,'%'),1=1) )", nativeQuery = true)
    List<Map<String, Object>> getCompetitionJudges(Pageable pageable, @Param("judgeDesc") String judgeDesc);

    @Query(value = "select count(*) from (SELECT cei.job_experience_id as jobExperienceId, cei.enterprise_id as enterpriseId, " +
            "cei.job_number as jobNumber, cei.email  as mail, cei.real_name as realName, sui.username, " +
            "sui.guid, sui.phone FROM crd_enterprise_info cei LEFT JOIN sys_user_info sui " +
            "ON cei.guid = sui.guid where sui.guid is not null " +
            "and ( if(:judgeDesc != '',sui.username like CONCAT('%',:judgeDesc,'%'),1=1)" +
            "or if(:judgeDesc != '',sui.phone like CONCAT('%',:judgeDesc,'%'),1=1)" +
            "or if(:judgeDesc != '',cei.real_name like CONCAT('%',:judgeDesc,'%'),1=1)" +
            "or if(:judgeDesc != '',cei.email like CONCAT('%',:judgeDesc,'%'),1=1) ))a", nativeQuery = true)
    int getCompetitionJudgesCount(String judgeDesc);

    @Query(value = "select enterprise_id as enterpriseId,enterprise_name as enterpriseName from enterprise_info ", nativeQuery = true)
    List<Map<String, Object>> getEnterprise();
}
