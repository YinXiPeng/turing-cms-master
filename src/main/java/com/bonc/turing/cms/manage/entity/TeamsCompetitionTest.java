package com.bonc.turing.cms.manage.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class TeamsCompetitionTest {

    @Id
    private String id;

    private String teamId;

    private String competitionId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
