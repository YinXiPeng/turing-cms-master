package com.bonc.turing.cms.manage.mapper.model;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.HashMap;
import java.util.List;
@Mapper
public interface NotebookMapper {

    //通过id查询notebook
    HashMap<String,Object> getNotebook(@Param("notebookId") String notebookId);

    //新建notebook临时表
    void saveNotebookTemp(@Param("param") HashMap<String, Object> param);

    //通过id查询notebook临时表
    HashMap<String,Object> getNotebookTemp(@Param("notebookId") String notebookId);


    //编辑更新notebook临时表
    void updateNotebookTemp(@Param("param") HashMap<String, Object> param);

    //竞赛模块添加模型
    void saveCompetition(@Param("competitionId") String competitionId, @Param("notebookId") String notebookId);

    //删除竞赛相关的notebook
    void deleteCompetition(@Param("competitionId") String competitionId, @Param("notebookId") String notebookId);

    //查找notebook的结果集分数
    List<HashMap<String,Object>> getScore(@Param("notebookId") String notebookId);

    //版本列表
    List<HashMap<String,Object>> versionListByParentId(@Param("guid") String guid, @Param("parentId") String parentId);

    //竞赛下的数据集id
    List<HashMap<String,Object>> getCompetitionDataSet(@Param("competitionId") String competitionId);

}
