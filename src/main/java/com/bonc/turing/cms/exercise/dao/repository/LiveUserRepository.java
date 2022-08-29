package com.bonc.turing.cms.exercise.dao.repository;

import com.bonc.turing.cms.exercise.domain.LiveUser;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * description:
 *
 * @author lxh
 * @date 2020/4/20 16:41
 */
public interface LiveUserRepository extends PagingAndSortingRepository<LiveUser, String> {
    /**
     * 根据guid查询直播用户信息
     *
     * @param guid
     * @return
     */
    LiveUser findByGuid(String guid);
}
