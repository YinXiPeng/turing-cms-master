package com.bonc.turing.cms.manage.mapper.result;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ResultMapper {

    List<Map<String,Object>> getRankOfIndividualCompetitionByCompetitionId(@Param("competitionId") String competitionId,@Param("offsetPage")Integer offsetPage,@Param("pageSize") Integer pageSize);

    List<Map<String,Object>> getRankOfTeamCompetitionByCompetitionId(@Param("competitionId") String competitionId,@Param("offsetPage")Integer offsetPage,@Param("pageSize") Integer pageSize);

    List<Map> getTotalScoreRankOfTeamMsg(@Param("competitionId") String competitionId,@Param("competitionStatus")String competitionStatus);

    List<Map> getTotalScoreRankMsg(String competitionId, String competitionStatus);
}
