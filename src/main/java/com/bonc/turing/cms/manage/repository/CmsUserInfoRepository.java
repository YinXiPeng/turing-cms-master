package com.bonc.turing.cms.manage.repository;

import com.bonc.turing.cms.manage.entity.user.CmsUserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CmsUserInfoRepository extends JpaRepository<CmsUserInfo,String>, JpaSpecificationExecutor<CmsUserInfo> {

    @Query(value = "SELECT * from cms_user_info where user_name=:userName and is_deleted=0",nativeQuery = true)
    List<CmsUserInfo> findByUserName(@Param(value ="userName" )String userName);

    @Query(value = "select * from cms_user_info where role = :role and is_deleted = 0 and (phone LIKE CONCAT('%',:keyWord,'%') OR user_name LIKE CONCAT('%',:keyWord,'%'))",nativeQuery = true)
    Page<CmsUserInfo> findByKeyWordAndRole(@Param(value ="keyWord" ) String keyWord, @Param(value = "role") int role, Pageable pageable);

    @Query(value = "select * from cms_user_info where role = :role and is_deleted = 0 and (phone LIKE CONCAT('%',:keyWord,'%') OR user_name LIKE CONCAT('%',:keyWord,'%'))",nativeQuery = true)
    List<CmsUserInfo> findByKeyWordAndRole(@Param(value ="keyWord" )String keyWord, @Param(value = "role") int role);

    CmsUserInfo findByNicknameAndIsDeleted(String nickname,int isDeleted);
}
