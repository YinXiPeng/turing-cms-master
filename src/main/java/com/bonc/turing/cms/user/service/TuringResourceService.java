package com.bonc.turing.cms.user.service;

import com.alibaba.fastjson.JSONObject;
import com.bonc.turing.cms.user.param.CmsResourceListParam;

/**
 * @desc 上传、下载资源相关
 * @author yh
 * @date 2020.07.08
 */
public interface TuringResourceService {

    /**
     *后台-资源列表
     * @param params
     * @return
     */
    Object cmsResourceList(CmsResourceListParam params);




}
