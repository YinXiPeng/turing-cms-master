package com.bonc.turing.cms.exercise.dao.mapper;


import com.bonc.turing.cms.exercise.dto.BackStageTextBookDTO;
import com.bonc.turing.cms.exercise.dto.VideoCourseListDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface QtTextBookMapper {

    List<BackStageTextBookDTO> getBackStageTextBookList();

    List<VideoCourseListDTO> getVideoCourseList();

}
