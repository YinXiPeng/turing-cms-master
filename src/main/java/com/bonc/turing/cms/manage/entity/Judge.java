package com.bonc.turing.cms.manage.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Judge {
    @Id
    private String id;

    private String competitionId;

    private String judgeId;

    private String judgeName;

    private byte hasScorea;

    private byte hasScoreb;

    private double scorea;

    private double scoreb;

    @Getter
    @Setter
    private Integer isFinal;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompetitionId() {
        return competitionId;
    }

    public void setCompetitionId(String competitionId) {
        this.competitionId = competitionId;
    }

    public String getJudgeId() {
        return judgeId;
    }

    public void setJudgeId(String judgeId) {
        this.judgeId = judgeId;
    }

    public String getJudgeName() {
        return judgeName;
    }

    public void setJudgeName(String judgeName) {
        this.judgeName = judgeName;
    }

    public byte getHasScorea() {
        return hasScorea;
    }

    public void setHasScorea(byte hasScorea) {
        this.hasScorea = hasScorea;
    }

    public byte getHasScoreb() {
        return hasScoreb;
    }

    public void setHasScoreb(byte hasScoreb) {
        this.hasScoreb = hasScoreb;
    }

    public double getScorea() {
        return scorea;
    }

    public void setScorea(double scorea) {
        this.scorea = scorea;
    }

    public double getScoreb() {
        return scoreb;
    }

    public void setScoreb(double scoreb) {
        this.scoreb = scoreb;
    }
}
