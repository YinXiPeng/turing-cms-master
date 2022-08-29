package com.bonc.turing.cms.manage.entity;

import com.bonc.turing.cms.manage.entity.competition.CmsCompetitionInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.ibatis.annotations.ResultType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "D_S_RESULT")
public class Result implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    private String createdById;

    private Date createdDate;

    private String displayName;

    private String lastModifiedById;

    private Date lastModifiedDate;

    private String memo;

    private Integer ord;

    private String tenant;

    private Integer version;

    private long checkNum;

    private String fileType;

    private boolean finalSubmit;

    private String git;

    private long likeNum;

    private String listType;

    private boolean pub;

    private String ranking;

    private String result;

    private int resultType;

    private String competitionInfoId;

    private String userInfoId;

    private String fileName;

    private Integer noviceIssue;

    private long notebookNum;

    private String[] filePaths;

    private long fileSize;

    private String stageType;

    private String atRankList;

    private double scorea;

    private double scoreb;

    private String isHighestalistOfUser;

    private String isHighestblistOfUser;

    private String resultPathb;

    private String medalsTypea;

    private String medalsTypeb;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedById() {
        return createdById;
    }

    public void setCreatedById(String createdById) {
        this.createdById = createdById;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getLastModifiedById() {
        return lastModifiedById;
    }

    public void setLastModifiedById(String lastModifiedById) {
        this.lastModifiedById = lastModifiedById;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public int getOrd() {
        return ord;
    }

    public void setOrd(int ord) {
        this.ord = ord;
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public long getCheckNum() {
        return checkNum;
    }

    public void setCheckNum(long checkNum) {
        this.checkNum = checkNum;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public boolean isFinalSubmit() {
        return finalSubmit;
    }

    public void setFinalSubmit(boolean finalSubmit) {
        this.finalSubmit = finalSubmit;
    }

    public String getGit() {
        return git;
    }

    public void setGit(String git) {
        this.git = git;
    }

    public long getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(long likeNum) {
        this.likeNum = likeNum;
    }

    public String getListType() {
        return listType;
    }

    public void setListType(String listType) {
        this.listType = listType;
    }

    public boolean isPub() {
        return pub;
    }

    public void setPub(boolean pub) {
        this.pub = pub;
    }

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getResultType() {
        return resultType;
    }

    public void setResultType(int resultType) {
        this.resultType = resultType;
    }

    public String getCompetitionInfoId() {
        return competitionInfoId;
    }

    public void setCompetitionInfoId(String competitionInfoId) {
        this.competitionInfoId = competitionInfoId;
    }

    public String getUserInfoId() {
        return userInfoId;
    }

    public void setUserInfoId(String userInfoId) {
        this.userInfoId = userInfoId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getNoviceIssue() {
        return noviceIssue;
    }

    public void setNoviceIssue(int noviceIssue) {
        this.noviceIssue = noviceIssue;
    }

    public long getNotebookNum() {
        return notebookNum;
    }

    public void setNotebookNum(long notebookNum) {
        this.notebookNum = notebookNum;
    }

    public String[] getFilePaths() {
        return filePaths;
    }

    public void setFilePaths(String[] filePaths) {
        this.filePaths = filePaths;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getStageType() {
        return stageType;
    }

    public void setStageType(String stageType) {
        this.stageType = stageType;
    }

    public String getAtRankList() {
        return atRankList;
    }

    public void setAtRankList(String atRankList) {
        this.atRankList = atRankList;
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

    public String getIsHighestalistOfUser() {
        return isHighestalistOfUser;
    }

    public void setIsHighestalistOfUser(String isHighestalistOfUser) {
        this.isHighestalistOfUser = isHighestalistOfUser;
    }

    public String getIsHighestblistOfUser() {
        return isHighestblistOfUser;
    }

    public void setIsHighestblistOfUser(String isHighestblistOfUser) {
        this.isHighestblistOfUser = isHighestblistOfUser;
    }

    public String getResultPathb() {
        return resultPathb;
    }

    public void setResultPathb(String resultPathb) {
        this.resultPathb = resultPathb;
    }

    public String getMedalsTypea() {
        return medalsTypea;
    }

    public void setMedalsTypea(String medalsTypea) {
        this.medalsTypea = medalsTypea;
    }

    public String getMedalsTypeb() {
        return medalsTypeb;
    }

    public void setMedalsTypeb(String medalsTypeb) {
        this.medalsTypeb = medalsTypeb;
    }
}
