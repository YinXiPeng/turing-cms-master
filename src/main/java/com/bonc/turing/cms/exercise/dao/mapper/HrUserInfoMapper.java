package com.bonc.turing.cms.exercise.dao.mapper;

import com.bonc.turing.cms.exercise.dto.HrUserInfoListDTO;
import com.bonc.turing.cms.exercise.vo.SearchHrUserInfoVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * description:
 *
 * @author lxh
 * @date 2020/4/2 11:28
 */
@Mapper
public interface HrUserInfoMapper {

    /**
     * 分页列表数量
     *
     * @param searchHrUserInfoVO
     * @return
     */
    Long pageCount(SearchHrUserInfoVO searchHrUserInfoVO);

    /**
     * 分页列表信息
     *
     * @param searchHrUserInfoVO
     * @return
     */
    List<HrUserInfoListDTO> pageList(SearchHrUserInfoVO searchHrUserInfoVO);

}
