package com.bonc.turing.cms.manage.repository;
import com.bonc.turing.cms.manage.entity.user.SysUserInfo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


import java.util.List;

public interface SysUserInfoRepository extends JpaRepository<SysUserInfo, String> {

    SysUserInfo findByGuid(String guid);


    SysUserInfo findByPhone(String phone);

    SysUserInfo findByUsername(String username);

    @Query(value = "select * from sys_user_info where phone like CONCAT('',:prefix,'%') and is_deleted=0",nativeQuery = true)
    Page<SysUserInfo> findAllByPhonePrefixPageable(String prefix, Pageable pageable);

    @Query(value = "select * from sys_user_info where phone like CONCAT('',:prefix,'%') and is_deleted=0",nativeQuery = true)
    List<SysUserInfo> findAllByPhonePrefix(String prefix);


}
