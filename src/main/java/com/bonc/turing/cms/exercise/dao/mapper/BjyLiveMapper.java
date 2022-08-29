package com.bonc.turing.cms.exercise.dao.mapper;

import com.bonc.turing.cms.exercise.domain.BjyRoomInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author lxm
 */
@Mapper
public interface BjyLiveMapper {


    /**
     * 判断是否存在数据
     * @param startTime
     * @param endTime
     * @return
     */
    @Select("select * from bjy_room_info where start_time<=#{endTime} and  end_time>=#{startTime} ")
    List<BjyRoomInfo> getIntersection(@Param("startTime") Long startTime, @Param("endTime") Long endTime);


}
