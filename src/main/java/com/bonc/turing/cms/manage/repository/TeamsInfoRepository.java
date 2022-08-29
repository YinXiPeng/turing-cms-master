package com.bonc.turing.cms.manage.repository;

import com.bonc.turing.cms.manage.entity.TeamsInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TeamsInfoRepository extends JpaRepository<TeamsInfo, String> {

    @Query(value = "select * from teams_info where id=?1" , nativeQuery = true)
    TeamsInfo findByTeamId(String teamId);

}
