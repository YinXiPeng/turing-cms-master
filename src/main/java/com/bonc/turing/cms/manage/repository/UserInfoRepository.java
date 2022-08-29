package com.bonc.turing.cms.manage.repository;


import com.bonc.turing.cms.manage.entity.user.SysUserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * UserInfo持久化类;
 *
 * @author liuyunkai
 * @date 2018-11-6 10:40
 */
@Repository
public interface UserInfoRepository extends JpaRepository<SysUserInfo, String>, JpaSpecificationExecutor<SysUserInfo> {

    /**
     * 通过username查找用户信息;
     */
    SysUserInfo findByUsername(String username);

    /**
     * 通过phone查找用户信息;
     */
    @Query(value = "select * from  sys_user_info where phone = ?", nativeQuery = true)
    SysUserInfo findByPhone(String phone);

    @Query(value = "select * from  sys_user_info where is_admin=0 and  (phone LIKE CONCAT('%',?1,'%') or username LIKE CONCAT('%',?1,'%'))", nativeQuery = true)
    List<SysUserInfo> findByUsernameOrPhone(String username);

    @Query(value = "select * from  sys_user_info where is_admin=0 and (phone LIKE CONCAT('%',?1,'%') or username LIKE CONCAT('%',?1,'%'))", nativeQuery = true)
    Page<SysUserInfo> findByUsernameOrPhone(String username,Pageable pageable);

    @Query(value = "select * from  sys_user_info where guid = ?", nativeQuery = true)
    Optional<SysUserInfo> findByGuid(String guid);

    @Query(value = "select * from  sys_user_info where openid = ?", nativeQuery = true)
    Optional<SysUserInfo> findByOpenid(String openid);


    @Transactional
    @Modifying
    @Query(value = "update sys_user_info set professiontag =? where guid =?", nativeQuery = true)
    void updataProfessionTagByGuid(String professiontag, String guid);

    @Transactional
    @Modifying
    @Query(value = "update sys_user_info set persontag =? where guid =?", nativeQuery = true)
    void updataPersonTagByGuid(String persontag, String guid);


    @Query(value = "select COUNT(*) from `salon` where creator_id=?", nativeQuery = true)
    int findAllByUserId(String userid);


    @Query(value = "select COUNT(*) from `d_s_data_source_def_info` where created_by_id=? and newest = 1 and creator = 1", nativeQuery = true)
    int findAllByCreateId(String userid);

    List<SysUserInfo> findByPhoneAndGuidNot(String phone, String guid);

    List<SysUserInfo> findByUsernameAndGuidNot(String username, String guid);

    Page<SysUserInfo> findAll(Specification<SysUserInfo> specification, Pageable pageable);

    @Query(value = "SELECT count(*) FROM judge where judge_id=?", nativeQuery = true)
    int findJudgeByUserId(String userid);


    @Modifying
    @Query(value = "update sys_user_info set username=?4, password=?2, salt=?3 where guid=?1  ", nativeQuery = true)
    int updateUserInfo(String guid, String password,String salt, String username);


    @Query(value = "SELECT a.guid guid, c.headimgurl headimgurl,c.manifesto manifesto, c.username username, a.score  score,ifnull(b.num , '0') num , '1' as  ismaster     FROM new_user_task a LEFT JOIN ( SELECT guid, count( guid ) AS num FROM notebook GROUP BY guid ) b ON a.guid = b.guid LEFT JOIN sys_user_info c ON a.guid = c.guid " +
            "WHERE a.guid IS NOT NULL  AND a.guid !=?1  AND  c.headimgurl IS NOT NULL  AND c.username IS NOT NULL  ORDER BY a.score DESC,b.num DESC", nativeQuery = true)
    List<Map<String, Object>> getUserNounList(String guid, Pageable pageable);

    @Query(value = "select count(*) as num from (SELECT a.guid guid, c.headimgurl headimgurl,c.manifesto manifesto, c.username username, a.score  score,ifnull(b.num , '0') num , '1' as  ismaster     FROM new_user_task a LEFT JOIN ( SELECT guid, count( guid ) AS num FROM notebook GROUP BY guid ) b ON a.guid = b.guid LEFT JOIN sys_user_info c ON a.guid = c.guid " +
            "WHERE a.guid IS NOT NULL  AND a.guid !=?1   AND c.headimgurl IS NOT NULL  AND c.username IS NOT NULL  ORDER BY a.score DESC,b.num DESC) a", nativeQuery = true)
    int getUserNounListCount(String guid);

    @Modifying
    @Query(value ="UPDATE sys_user_info SET is_deleted=1 where guid=?1",nativeQuery = true)
    void deleteByGuid(String guid);

    SysUserInfo findByNicknameAndIsDeleted(String nickName, int i);
}

