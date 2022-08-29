package com.bonc.turing.cms.exercise.dao.repository;

import com.bonc.turing.cms.exercise.domain.CosExercisesAnswerRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;
import java.util.Set;

public interface CosExercisesAnswerRecordRepository  extends JpaRepository<CosExercisesAnswerRecord,String>, JpaSpecificationExecutor<CosExercisesAnswerRecord> {

    void deleteBySectionId(String sectionId);

    List<CosExercisesAnswerRecord> findBySectionIdIn(Set sectionIds);
}
