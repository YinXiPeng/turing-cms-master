package com.bonc.turing.cms.manage.repository;



import com.bonc.turing.cms.manage.entity.notify.Notify;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotifyRepository extends JpaRepository<Notify, String>, JpaSpecificationExecutor {


    @Query(value = "select * from  notify where if(:guid != '' ,notify_user_guid = :guid , 1=1)  order by notifyTime desc", nativeQuery = true)
    public List<Notify> getByNotifyId(@Param("guid") String guid);

    /*@Query(value = "select * from  notify where  competition_name is  null AND  if(:guid != '' ,notify_user_guid = :guid , 1=1)  ", nativeQuery = true)
    public Page<Notify> getByGuid(@Param("guid") String guid, Pageable pageable);

    @Query(value = "select * from  notify where competition_name is not null AND  if(:guid != '' ,notify_user_guid = :guid , 1=1) AND if(:competitionName != '' ,competition_name = :competitionName , 1=1)", nativeQuery = true)
    List<Notify> getByGuidAndCompetitionName(@Param("guid") String guid, @Param("competitionName") String competitionName, Pageable pageable);*/

    @Query(value = "select * from  notify where notify_id = :notifyId", nativeQuery = true)
    public Optional<Notify> getById(@Param("notifyId") String notifyId);

    @Query(value = "select user_id as notifyUserGuid  from  user_participate_competition where competition_id = :competitionId", nativeQuery = true)
    List<String> queryCompetitionListBycompetitionId(@Param("competitionId") String competitionId);

    @Query(value = "SELECT guid FROM sys_user_info WHERE guid NOT IN ( SELECT user_id AS guid FROM user_participate_competition WHERE competition_id = :competitionId )", nativeQuery = true)
    List<String> queryCompetitionAontherListBycompetitionId(@Param("competitionId") String competitionId);

    @Query(value = "select phone from  sys_user_info where guid = :guid", nativeQuery = true)
    String getPhoneByGuid(@Param("guid") String guid);

    @Query(value = "select email from  apply_form where guid = :guid", nativeQuery = true)
    String getMailByGuid(@Param("guid") String guid);

    @Query(value = "select guid as notifyUserGuid  from  sys_user_info where 1=1", nativeQuery = true)
    List<String> queryAllUser();

    @Query(value = "select guid as notifyUserGuid  from  sys_user_info where guid = '4dbf6c40ee9448d69aaba7fe5857c9e5'", nativeQuery = true)
    List<String> queryAllUser1();

    @Query(value = "select *  from  notify where issend = '0' and id is null and notify_time = :nowtime", nativeQuery = true)
    List<Notify> queryAll(@Param("nowtime") Long nowtime);

}
