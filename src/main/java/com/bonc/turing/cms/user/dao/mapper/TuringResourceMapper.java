package com.bonc.turing.cms.user.dao.mapper;

import com.bonc.turing.cms.user.param.CmsResourceListParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @desc 上传资源dao
 * @author yh
 * @date 2020.07.10
 */
@Mapper
public interface TuringResourceMapper {

    /**
     * 查询后台上传、下载资源列表
     * @param params
     * @return
     */
    List<Map> findCmsResourceList(@Param("params") CmsResourceListParam params);
}
