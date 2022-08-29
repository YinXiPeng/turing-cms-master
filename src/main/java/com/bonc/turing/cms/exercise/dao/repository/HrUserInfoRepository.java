package com.bonc.turing.cms.exercise.dao.repository;

import com.bonc.turing.cms.exercise.domain.HrUserInfo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

/**
 * description:
 *
 * @author lxh
 * @date 2020/4/2 10:11
 */
public interface HrUserInfoRepository extends PagingAndSortingRepository<HrUserInfo, String> {

    /**
     * 根据guid修改账号状态
     *
     * @param status
     * @param guid
     */
    @Modifying
    @Query("update HrUserInfo hui set hui.status = :status where hui.guid = :guid")
    void updateStatusByGuid(@Param("status") Integer status, @Param("guid") String guid);
}
