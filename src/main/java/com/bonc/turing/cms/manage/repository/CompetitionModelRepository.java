package com.bonc.turing.cms.manage.repository;

import com.bonc.turing.cms.manage.entity.competition.CompetitionModel;
import org.apache.ibatis.annotations.Update;
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
 * @ClassName: CompetitionModelRepository
 * @Author: bxt
 * @Description: 竞赛-模型
 * @Date: 2019/7/19 10:41
 * @Version: 1.0
 */
public interface CompetitionModelRepository extends JpaRepository<CompetitionModel, Long>, JpaSpecificationExecutor<CompetitionModel> {

    @Query(value = "SELECT CASE WHEN dsr.common_medals_typeb IS NOT NULL OR dsr.pre_medals_typeb IS NOT NULL THEN 1 ELSE 0 END isAward, IFNULL(ns.is_refinement, 0) isRefinement, IFNULL(ns.is_top, 0) isTop, IFNULL(ns.sort, 0) sort, ns.remark, IFNULL(ns.modify_time, '') adminTime, sui.headimgurl, sui.username, nb.notebook_id notebookId, nb.notebook_description notebookDescription, nb.code_num codeNum, nb.img_num imageNum, nb.execute_time executeTime, nb.notebook_name notebookName, nb.version, nb.guid, nb.parent_id parentId, UNIX_TIMESTAMP(nb.create_time) * 1000 createTime, UNIX_TIMESTAMP(nb.last_modified) * 1000 lastModified FROM notebook nb LEFT JOIN ( SELECT COUNT(parent_id) version, nb.parent_id FROM competition_notebook cn LEFT JOIN notebook nb ON cn.notebook_id = nb.notebook_id WHERE cn.competition_id = :competitionId AND nb.notebook_id IS NOT NULL AND nb.is_delete = '1' AND nb.is_public = :isPublic AND parent_id != '' GROUP BY nb.parent_id ) A ON nb.parent_id = A.parent_id LEFT JOIN notebook_sort ns ON ns.notebook_id = nb.parent_id AND ns.competition_id = :competitionId LEFT JOIN sys_user_info sui ON nb.guid = sui.guid LEFT JOIN notebook_result nrt ON nb.notebook_id = nrt.notebook_id AND nrt.result_type = 'B' LEFT JOIN d_s_result dsr ON dsr.id = nrt.result_id WHERE nb.version = A.version AND nb.is_delete = '1' AND nb.is_public = :isPublic ORDER BY sort DESC, adminTime DESC, lastModified DESC", nativeQuery = true)
    List<Map<String, Object>> getModelList(Pageable pageable, @Param("competitionId") String competitionId, @Param("isPublic") int isPublic);

    @Query(value = "select count(*) as num from (SELECT b.notebookId,b.parentId, b.notebookName, b.createTime, b.isAward, b.version, b.sort, b.isTop, b.isRefinement, b.modifyTime, b.remark" +
            "  FROM ( SELECT max( th.version ) AS verson, th.parentId  FROM ( " +
            "SELECT a.notebook_id AS notebookId, a.notebook_name AS notebookName, a.create_time AS createTime," +
            " a.isAward, a.parent_id AS parentId, a.version, ifnull( b.sort, 0 ) AS sort, ifnull( b.is_top, 0 ) AS isTop," +
            " ifnull( b.is_refinement, 0 ) AS isRefinement, b.modify_time AS modifyTime, b.remark  FROM ( " +
            "SELECT nb.notebook_id, nb.notebook_name, nb.create_time, nb.parent_id, nb.version, " +
            "CASE WHEN dsr.medals_typeb IS NULL THEN '0' ELSE '1'  END isAward  FROM notebook nb " +
            "LEFT JOIN competition_notebook cn ON nb.notebook_id = cn.notebook_id " +
            "LEFT JOIN notebook_result nr ON nb.notebook_id = nr.notebook_id  " +
            "LEFT JOIN d_s_result dsr ON nr.result_id = dsr.id WHERE nb.is_delete = 1  " +
            "AND if(:isPublic != '',nb.is_public = :isPublic,1=1)  AND nb.parent_id != ''  AND cn.competition_id = :competitionId ) a " +
            "LEFT JOIN notebook_sort b ON a.parent_id = b.notebook_id  ORDER BY b.sort DESC, b.modify_time DESC, " +
            "a.parent_id DESC, a.version DESC  ) th GROUP BY th.parentId  ) a LEFT JOIN ( " +
            "SELECT a.notebook_id AS notebookId, a.notebook_name AS notebookName, a.create_time AS createTime," +
            " a.isAward, a.parent_id AS parentId, a.version,ifnull( b.sort, 0 ) AS sort, ifnull( b.is_top, 0 ) AS isTop," +
            " ifnull( b.is_refinement, 0 ) AS isRefinement, b.modify_time AS modifyTime, b.remark FROM ( " +
            "SELECT nb.notebook_id, nb.notebook_name, nb.create_time, nb.parent_id, nb.version, " +
            "CASE WHEN dsr.medals_typeb IS NULL THEN '0' ELSE '1' END isAward  FROM notebook nb " +
            "LEFT JOIN competition_notebook cn ON nb.notebook_id = cn.notebook_id LEFT JOIN notebook_result nr" +
            " ON nb.notebook_id = nr.notebook_id LEFT JOIN d_s_result dsr ON nr.result_id = dsr.id  WHERE nb.is_delete = 1  " +
            "AND if(:isPublic != '',nb.is_public = :isPublic,1=1) AND nb.parent_id != ''  AND cn.competition_id = :competitionId) a " +
            "LEFT JOIN notebook_sort b ON a.parent_id = b.notebook_id  ORDER BY b.sort DESC, b.modify_time DESC, a.parent_id DESC," +
            " a.version DESC  ) b ON a.verson = b.version  AND a.parentId = b.parentId) a", nativeQuery = true)
    int getModelListCount(@Param("competitionId") String competitionId, @Param("isPublic") int isPublic);


    @Query(value = "select * from  notebook where notebook_id = :notebookId", nativeQuery = true)
    Optional<CompetitionModel> getCompetitionModelById(@Param("notebookId") String notebookId);

    @Modifying
    @Query(value = "update notebook set is_public = :isPublic and is_delete = :isDelete where parent_id = :parentId",nativeQuery = true)
    void updateChildrenModel(@Param("parentId") String parentId,@Param("isPublic") int isPublic,@Param("isDelete") int isDelete);

    @Modifying
    @Query(value = "update  notebook set is_public = :isPublic and is_delete = :isDelete where notebook_id = :parentId", nativeQuery = true)
    void updateParentModel(@Param("parentId") String parentId,@Param("isPublic") int isPublic,@Param("isDelete") int isDelete);

    @Modifying
    @Query(value = "update  notebook_temp  set is_delete = :isDelete where notebook_id in (select notebook_id from notebook where notebook_id =:parentId or parent_id = :parentId ", nativeQuery = true)
    void updateModelisDelete(@Param("parentId") String parentId,@Param("isDelete") int isDelete);

    @Modifying
    @Query(value = "update  notebook_temp  set is_public =:isPublic where notebook_id in (select notebook_id from notebook where notebook_id =:parentId or parent_id = :parentId   )", nativeQuery = true)
    void updateModelisPublic(@Param("parentId") String parentId,@Param("isPublic") int isPublic);
}
