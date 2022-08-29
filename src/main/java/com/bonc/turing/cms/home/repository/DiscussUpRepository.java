package com.bonc.turing.cms.home.repository;

import com.bonc.turing.cms.home.domain.DiscussUp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscussUpRepository extends JpaRepository<DiscussUp,Long> {

    DiscussUp findByDiscussIdAndUserId(String discussId,String userId);

}
