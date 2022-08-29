package com.bonc.turing.cms.user.dao.repository;

import com.bonc.turing.cms.user.bean.UserPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @desc 用户积分
 * @date 2020.06.18
 * @author yh
 */
@Repository
public interface UserPointRepository extends JpaRepository<UserPoint, String> {
    /**
     * 根据guid查用户积分信息
     * @param guid
     * @return
     */
    UserPoint findByUserId(String guid);
}
