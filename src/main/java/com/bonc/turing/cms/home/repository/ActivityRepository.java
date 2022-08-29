package com.bonc.turing.cms.home.repository;

import com.bonc.turing.cms.home.domain.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepository extends JpaRepository<Activity,String> {

}
