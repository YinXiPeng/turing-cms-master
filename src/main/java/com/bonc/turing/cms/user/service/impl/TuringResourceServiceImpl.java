package com.bonc.turing.cms.user.service.impl;


import com.bonc.turing.cms.common.utils.PageInfoEntity;
import com.bonc.turing.cms.user.dao.mapper.TuringResourceMapper;
import com.bonc.turing.cms.user.param.CmsResourceListParam;
import com.bonc.turing.cms.user.service.TuringResourceService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author yh
 * @desc 上传、下载资源相关
 * @date 2020.07.08
 */
@Slf4j
@Service
public class TuringResourceServiceImpl implements TuringResourceService {

    @Resource
    private TuringResourceMapper resourceMapper;

    /**
     *后台-资源列表
     * @param params
     * @return
     */
    @Override
    public Object cmsResourceList(CmsResourceListParam params) {
        PageHelper.startPage(params.getPageNum(),params.getPageSize());
        List<Map> resourceList = resourceMapper.findCmsResourceList(params);
        PageInfo<Map> cmsResourcePage = new PageInfo<>(resourceList);
        return new PageInfoEntity<Map>(cmsResourcePage);
    }



}
