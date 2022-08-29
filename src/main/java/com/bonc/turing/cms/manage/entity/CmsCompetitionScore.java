package com.bonc.turing.cms.manage.entity;

import com.alibaba.fastjson.JSONObject;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;
import java.util.List;

@Entity
public class CmsCompetitionScore {

    private static final long serialVersionUID = -7610507011609179180L;

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid",strategy="uuid")
    private String scoreId;

    private String competitionId;

    private String competitionName;

    private String teamName;

    private String submission;

    private String description;

    private String submitItem;

    private float totalScore;

    private String comments;

    private Date submitTime;

    private int status;

    private int finalFlag;

    private int isNeedTeam;

    private String teamId;

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public int getIsNeedTeam() {
        return isNeedTeam;
    }

    public void setIsNeedTeam(int isNeedTeam) {
        this.isNeedTeam = isNeedTeam;
    }

    public int getFinalFlag() {
        return finalFlag;
    }

    public void setFinalFlag(int finalFlag) {
        this.finalFlag = finalFlag;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getScoreId() {
        return scoreId;
    }

    public void setScoreId(String scoreId) {
        this.scoreId = scoreId;
    }

    public String getCompetitionId() {
        return competitionId;
    }

    public void setCompetitionId(String competitionId) {
        this.competitionId = competitionId;
    }

    public String getCompetitionName() {
        return competitionName;
    }

    public void setCompetitionName(String competitionName) {
        this.competitionName = competitionName;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getSubmission() {
        return submission;
    }

    public void setSubmission(String submission) {
        this.submission = submission;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSubmitItem() {
        return submitItem;
    }

    public void setSubmitItem(String submitItem) {
        this.submitItem = submitItem;
    }

    public float getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(float totalScore) {
        this.totalScore = totalScore;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
