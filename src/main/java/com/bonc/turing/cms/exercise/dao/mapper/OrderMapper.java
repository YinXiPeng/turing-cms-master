package com.bonc.turing.cms.exercise.dao.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {

    List<Map> getTextBookOrder(@Param("textBookId") String textBookId,@Param("keyWord") String keyWord,@Param("type") Integer type);

    List<Map> getOrderByProductId(@Param("productId") String productId,@Param("type") int type,@Param("keyWord") String keyWord);

    List<Map> getLiveRecord(@Param("keyWord") String keyWord);

    Integer updateLiveRecordRemark(@Param("liveRecordId") String liveRecordId,@Param("remarks") String remarks);
}
