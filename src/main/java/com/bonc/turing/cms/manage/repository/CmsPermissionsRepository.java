package com.bonc.turing.cms.manage.repository;

import com.bonc.turing.cms.manage.entity.user.CmsPermissions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CmsPermissionsRepository extends JpaRepository<CmsPermissions, String>, JpaSpecificationExecutor<CmsPermissions> {

    @Query(value = "select * from cms_permissions where parents_permissions=0",nativeQuery = true)
    List<CmsPermissions> findAllParent();

    @Query(value = "select permissions_id from cms_user_permissions where guid = ?1",nativeQuery = true)
    List<Integer> findByGuid(@Param("guid") String guid);

    @Modifying
    @Query(value = "delete from cms_user_permissions where guid=?1 and permissions_id=?2",nativeQuery = true)
    void deleteByGuidAndPermissionId(String guid,Integer permissionId);

    @Modifying
    @Query(value = "insert into cms_user_permissions(guid,permissions_id) VALUES (?1,?2)",nativeQuery = true)
    void addPermission(String userId, Integer roleCode);
}
