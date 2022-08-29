package com.bonc.turing.cms.manage.dto;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

public class CompetitionScoreDTO {

    private String num;
    private String scoreId;
    private String competitioId;
    private String competitionName;
    private String teamName;
    private JSONArray submisson;
    private JSONObject submitItem;
    private float totalScore;
    private String comments;
    private int status;
    private String description;
    private JSONObject ScoreWeight;

    public JSONObject getScoreWeight() {
        return ScoreWeight;
    }

    public void setScoreWeight(JSONObject scoreWeight) {
        ScoreWeight = scoreWeight;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getScoreId() {
        return scoreId;
    }

    public void setScoreId(String scoreId) {
        this.scoreId = scoreId;
    }

    public String getCompetitioId() {
        return competitioId;
    }

    public void setCompetitioId(String competitioId) {
        this.competitioId = competitioId;
    }

    public String getCompetitionName() {
        return competitionName;
    }

    public void setCompetitionName(String competitionName) {
        this.competitionName = competitionName;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public JSONArray getSubmisson() {
        return submisson;
    }

    public void setSubmisson(JSONArray submisson) {
        this.submisson = submisson;
    }

    public JSONObject getSubmitItem() {
        return submitItem;
    }

    public void setSubmitItem(JSONObject submitItem) {
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
