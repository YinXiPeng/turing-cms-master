package com.bonc.turing.cms.manage.repository;

import com.bonc.turing.cms.manage.entity.TeamsCompetition;
import com.bonc.turing.cms.manage.entity.TeamsCompetitionTest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamsCompetitionRepository extends JpaRepository<TeamsCompetition,String> {

    List<TeamsCompetition> findByCompetitionId(String competitionId);
}
