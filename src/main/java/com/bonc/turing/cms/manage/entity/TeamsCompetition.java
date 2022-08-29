package com.bonc.turing.cms.manage.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @Author:nyb
 * @DESC:
 * @Date: Created in 15:57 2019/8/15 0015
 * @Modified By:
 */
@Entity
public class TeamsCompetition {
    @Id
    private String teamId;
    private String competitionId;

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getCompetitionId() {
        return competitionId;
    }

    public void setCompetitionId(String competitionId) {
        this.competitionId = competitionId;
    }
}
