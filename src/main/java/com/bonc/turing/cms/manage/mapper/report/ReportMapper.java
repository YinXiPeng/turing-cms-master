package com.bonc.turing.cms.manage.mapper.report;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author lky
 * @desc
 * @date 2019/8/7 14:51
 */
@Mapper
public interface ReportMapper {
    List<Map> getReportList();

    Map getCommentReportById(@Param("reportId") String reportId);

    Map getReplyReportById(@Param("reportId")String reportId);

    int updateCommentStatusToPass(@Param("guid") String guid, @Param("reportId")String reportId);

    int updateReplyStatusToPass(@Param("guid") String guid, @Param("reportId")String reportId);

    int updateCommentStatusToRefuse(@Param("guid") String guid, @Param("reportId")String reportId);

    int updateReplyStatusToRefuse(@Param("guid") String guid, @Param("reportId")String reportId);

    Map getCommentById(@Param("reportId")String reportId);

    Map getReplyById(@Param("reportId")String reportId);

}
