package com.bonc.turing.cms.manage.repository;

import com.bonc.turing.cms.manage.entity.user.UserVisit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserVisitRepository extends JpaRepository<UserVisit,Long> {
    Optional<UserVisit> findByGuid(String guid);

    UserVisit findByGuidOrderById(String guid);


}
