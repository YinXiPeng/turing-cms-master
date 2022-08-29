package com.bonc.turing.cms.manage.repository;

import com.bonc.turing.cms.manage.entity.notify.NotifyEnter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author lky
 * @desc
 * @date 2019/8/6 19:14
 */
public interface NotifyEntryRepository extends JpaRepository<NotifyEnter, String>, JpaSpecificationExecutor<NotifyEnter> {
}
