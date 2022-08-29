package com.bonc.turing.cms.exercise.dao.repository;

import com.bonc.turing.cms.exercise.domain.CosPaper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * description:
 *
 * @author lxh
 * @date 2020/3/23 12:15
 */
public interface CosPaperRepository extends PagingAndSortingRepository<CosPaper, String>, JpaSpecificationExecutor<CosPaper> {

    /**
     * 试卷名称唯一性校验新增
     *
     * @param paperName
     * @param schoolId
     * @return
     */
    @Query("select count(cp.id) from CosPaper cp where cp.name = :paperName and cp.schoolId = :schoolId")
    long checkUniqueName(@Param("paperName") String paperName, @Param("schoolId") String schoolId);


    /**
     * 试卷name唯一性校验编辑
     *
     * @param paperName
     * @param schoolId
     * @param paperId
     * @return
     */
    @Query("select count(cp.id) from CosPaper cp where cp.name = :paperName and cp.id != :paperId and cp.schoolId = :schoolId")
    long checkUniqueName2(@Param("paperName") String paperName, @Param("schoolId") String schoolId, @Param("paperId") String paperId);

    /**
     * 根据courseId修改试卷isDelete逻辑删除状态
     *
     * @param isDelete
     * @param courseId
     */
    @Modifying
    @Query("update CosPaper cp set cp.isDelete = :isDelete where cp.courseId = :courseId")
    void updateIsDeleteByCourseId(@Param("isDelete") Integer isDelete, @Param("courseId") String courseId);


}
