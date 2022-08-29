package com.bonc.turing.cms.manage.mapper;

import com.bonc.turing.cms.manage.entity.Judge;
import com.bonc.turing.cms.manage.entity.TeamsCompetition;
import com.bonc.turing.cms.manage.entity.competition.CmsCompetitionInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author:nyb
 * @DESC:
 * @Date: Created in 20:46 2019/8/10 0010
 * @Modified By:
 */
@Mapper
public interface CompetitionInfoMapper {

    List<CmsCompetitionInfo> findCompetition(String guid);

    List<CmsCompetitionInfo> findFinalCompetition(String guid);

    Judge findFinalJudge(String judgeId, String competitionId);
}
