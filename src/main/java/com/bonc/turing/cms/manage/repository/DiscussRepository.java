package com.bonc.turing.cms.manage.repository;

import com.bonc.turing.cms.manage.entity.competition.Discuss;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @ProjectName: cms
 * @Package: com.bonc.turing.cms.manage.repository
 * @ClassName: DiscussRepository
 * @Author: bxt
 * @Description:
 * @Date: 2019/7/17 15:11
 * @Version: 1.0
 */
public interface DiscussRepository extends JpaRepository<Discuss, String>, JpaSpecificationExecutor<Discuss> {

    @Query(value = "SELECT a.id, a.title, a.content,a.remark,a.sorting,a.excellent,a.top,a.show_hide as showHide, b.username" +
            "  FROM  discuss a LEFT JOIN sys_user_info b ON a.user_id = b.guid  where  a.refer_id = :referId AND a.del_flag = 0", nativeQuery = true)
    List<Map<String, Object>> getDiscussList(Pageable pageable, @Param("referId") String referId);

    @Query(value = "select count(*) from (SELECT a.id, a.title, a.content,a.remark,a.sorting,a.excellent,a.top,a.show_hide as showHide, b.username" +
            "  FROM  discuss a LEFT JOIN sys_user_info b ON a.user_id = b.guid  where  a.refer_id = :referId AND a.del_flag = 0) a", nativeQuery = true)
    int getRequirementListCount(@Param("referId") String referId);

    @Query(value = "select * from  discuss where id = :id", nativeQuery = true)
    Optional<Discuss> getDiscussById(@Param("id") String id);

    @Modifying
    @Query(value = "delete from discuss where id = :id", nativeQuery = true)
    void deleteDiscussById(@Param("id") String id);

    @Query(value = "select cId from comment where disId = :id", nativeQuery = true)
    List<String> getCIdsById(@Param("id") String id);

    @Modifying
    @Query(value = "delete from comment where disId = :id", nativeQuery = true)
    void deleteCommentByCId(@Param("id") String id);

    @Modifying
    @Query(value = "delete from discuss_label where discuss_id = :id", nativeQuery = true)
    void deleteLabelById(@Param("id") String id);

    @Query(value = "select rId from reply where cId=:cId", nativeQuery = true)
    List<String> getRIdsById(@Param("cId") String cId);

    @Modifying
    @Query(value = "delete from reply where cId = :cId", nativeQuery = true)
    void deleteReplyByCId(@Param("cId") String cId);
}
