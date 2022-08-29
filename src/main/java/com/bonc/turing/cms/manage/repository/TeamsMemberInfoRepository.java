package com.bonc.turing.cms.manage.repository;

import com.bonc.turing.cms.manage.entity.TeamsMemberInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TeamsMemberInfoRepository extends JpaRepository<TeamsMemberInfo,String> {

    @Query(value = "select t from TeamsMemberInfo t where t.teamsInfoId=?1 and t.isAdmin=1")
    TeamsMemberInfo findByTeamsInfoId(String teamsInfoId);
}
