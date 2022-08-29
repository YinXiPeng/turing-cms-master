package com.bonc.turing.cms.manage.repository;

import com.bonc.turing.cms.manage.entity.UserParticipateCompetition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserParticipateCompetitionRepository extends JpaRepository<UserParticipateCompetition ,String> {

    @Query(value = "select u from UserParticipateCompetition u where u.competitionId=?1 order by u.participateDate desc ")
    List<UserParticipateCompetition> findByCompetitionId(String competitionId);


}
