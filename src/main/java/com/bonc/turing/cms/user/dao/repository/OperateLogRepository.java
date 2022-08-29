package com.bonc.turing.cms.user.dao.repository;

import com.bonc.turing.cms.user.bean.OperateLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OperateLogRepository extends JpaRepository<OperateLog,Long> {

    @Query(value="select * from  operate_log where user_guid = ? and operate_timestamp between ? and ? order by operate_timestamp DESC",nativeQuery=true)
    List<OperateLog> findByUserGuid(String guid, long beginTime, long endTime);

    @Query(value="select count(*) from  operate_log where user_guid = ? and operate_timestamp between ? and ? order by operate_timestamp DESC",nativeQuery=true)
    Integer countByUserGuid(String guid, long beginTime, long endTime);

    OperateLog findByUserGuidAndOperateTimestampIsBetween(String guid, long beginTime, long endTime);
}
