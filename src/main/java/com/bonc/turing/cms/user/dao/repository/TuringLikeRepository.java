package com.bonc.turing.cms.user.dao.repository;

import com.bonc.turing.cms.user.bean.TuringLike;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * @desc 图灵平台点赞表
 * @author yh
 * @date 2020.07.10
 */
public interface TuringLikeRepository extends JpaRepository<TuringLike,String> {


    /**
     * 查询用户点赞
     * @param userId
     * @param referId
     * @param type
     * @return
     */
    TuringLike findByUserIdAndReferIdAndType(String userId,String referId,Integer type);
}
