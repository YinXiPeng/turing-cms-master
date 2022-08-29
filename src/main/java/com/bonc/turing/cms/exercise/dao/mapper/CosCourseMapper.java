package com.bonc.turing.cms.exercise.dao.mapper;



import com.bonc.turing.cms.exercise.dto.CmsCourseDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CosCourseMapper {

    List<CmsCourseDTO> getCmsCourseList(@Param("guid") String guid, @Param("type") Integer type,@Param("form")Integer form, @Param("schoolId") String schoolId);



}
