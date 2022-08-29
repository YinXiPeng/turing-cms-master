package com.bonc.turing.cms.manage.repository;

import com.bonc.turing.cms.manage.entity.notify.NotifySetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author lky
 * @desc
 * @date 2019/8/6 10:03
 */
public interface NotifySettingRepository extends JpaRepository<NotifySetting, String>, JpaSpecificationExecutor<NotifySetting> {

    List<NotifySetting> findByGuid(String guid);
}
