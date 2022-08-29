package com.bonc.turing.cms.exercise.dao.repository;

import com.bonc.turing.cms.exercise.domain.CosAnswerRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author lxm
 */
@Repository
public interface CosAnswerRecordRepository extends JpaRepository<CosAnswerRecord,String> {
}
