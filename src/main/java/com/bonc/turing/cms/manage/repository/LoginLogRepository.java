package com.bonc.turing.cms.manage.repository;

import com.bonc.turing.cms.manage.entity.LoginLog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * LoginLog持久化类;
 * @author liuyunkai
 * @date 2018-11-27 20:40
 */
public interface LoginLogRepository extends JpaRepository<LoginLog,Long> {
}
