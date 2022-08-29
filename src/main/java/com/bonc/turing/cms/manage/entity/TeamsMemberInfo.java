package com.bonc.turing.cms.manage.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class TeamsMemberInfo {

    @Id
    private String id;

    private String teamsInfoId;

    private String guid;

    private String userName;

    private String isAdmin;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTeamsInfoId() {
        return teamsInfoId;
    }

    public void setTeamsInfoId(String teamsInfoId) {
        this.teamsInfoId = teamsInfoId;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }
}
