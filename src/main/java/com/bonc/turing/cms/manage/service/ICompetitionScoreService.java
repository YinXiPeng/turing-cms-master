package com.bonc.turing.cms.manage.service;

import com.alibaba.fastjson.JSONObject;
import com.bonc.turing.cms.manage.dto.CompetitionScoreDTO;

public interface ICompetitionScoreService {

    Object findCompetitionInfo(JSONObject params);

    Object judgeJoinCompetitionList(String guid,JSONObject params);

    String submitScore(String guid ,String scoreId,JSONObject params);

    Object findTeam(String guid,String competitionId,JSONObject params);

    Object submitScores(String scoreId , JSONObject params);

    String deleteByScoreId(String scoreId);

    CompetitionScoreDTO findScore(String scoreId);
}


