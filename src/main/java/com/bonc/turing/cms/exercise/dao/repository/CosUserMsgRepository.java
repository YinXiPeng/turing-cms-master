package com.bonc.turing.cms.exercise.dao.repository;

import com.bonc.turing.cms.exercise.domain.CosUserMsg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

/**
 * @Author lky
 * @Description 学生老师信息表
 * @Date 17:44 2020/3/23
 * @Param
 * @return
 **/
public interface CosUserMsgRepository extends JpaRepository<CosUserMsg,String>, JpaSpecificationExecutor<CosUserMsg> {

    List<CosUserMsg> findByPhone(String phone);

    List<CosUserMsg> findBySchoolName(String schoolName);

    /**
     * 根据guid 获取对应的人学生信息
     * @param guid
     * @return
     */
    Optional<CosUserMsg> findByGuid(String guid);

    CosUserMsg findByGuidAndSchoolId(String guid,String schoolId);
}
