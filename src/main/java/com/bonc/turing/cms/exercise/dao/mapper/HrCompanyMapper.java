package com.bonc.turing.cms.exercise.dao.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

/**
 * @author lky
 * @desc
 * @date 2020/2/21 13:55
 */
@Mapper
public interface HrCompanyMapper {
    List<HashMap> findAllCompany();
}
