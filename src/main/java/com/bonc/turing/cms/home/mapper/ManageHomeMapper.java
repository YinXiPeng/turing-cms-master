package com.bonc.turing.cms.home.mapper;

import com.bonc.turing.cms.home.domain.ActivityDTO;
import com.bonc.turing.cms.home.domain.DSLikeUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ManageHomeMapper {

    List<ActivityDTO> findActivities(@Param("title") String title);

    String findDsLikeUser(@Param("dataSourceDefId")String dataSourceDefId,@Param("guid") String guid);

    void insertDsLikeUser(DSLikeUser dsLikeUser);

    void updateDataSourceDef(@Param("id") String id);

    String findDataSourceDefId(@Param("id")String id);

    void deleteSourceDefInfo(@Param("id")String id);

    void updateDataSourceDefDiscussNum(@Param("id")String id);

    String findHistoryVersionId(@Param("id") String id);

    List<Map> findByDictionaryName(@Param("name")String name);

    Map findDataSetMsgById(@Param("id")String id);

}
