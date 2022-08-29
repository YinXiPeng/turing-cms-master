package com.bonc.turing.cms.manage.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class UserParticipateCompetition {

    @Id
    private String participateId;
    private String competitionId;
    private String userId;
    private Date participateDate;
    private int sequence;
    private String description;
    private String datasetId;
    private String gitUrl;
    private String datesetId;
    private String awards;

    public String getParticipateId() {
        return participateId;
    }

    public void setParticipateId(String participateId) {
        this.participateId = participateId;
    }

    public String getCompetitionId() {
        return competitionId;
    }

    public void setCompetitionId(String competitionId) {
        this.competitionId = competitionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getParticipateDate() {
        return participateDate;
    }

    public void setParticipateDate(Date participateDate) {
        this.participateDate = participateDate;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(String datasetId) {
        this.datasetId = datasetId;
    }

    public String getGitUrl() {
        return gitUrl;
    }

    public void setGitUrl(String gitUrl) {
        this.gitUrl = gitUrl;
    }

    public String getDatesetId() {
        return datesetId;
    }

    public void setDatesetId(String datesetId) {
        this.datesetId = datesetId;
    }

    public String getAwards() {
        return awards;
    }

    public void setAwards(String awards) {
        this.awards = awards;
    }

}
