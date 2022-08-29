package com.bonc.turing.cms.user.controller;

import com.bonc.turing.cms.manage.ResultEntity;
import com.bonc.turing.cms.user.param.CmsResourceListParam;
import com.bonc.turing.cms.user.service.TuringResourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yh
 * @desc 上传、下载资源相关
 * @date 2020.07.08
 */
@RequestMapping("resource")
@RestController
@Slf4j
public class TuringResourceController {

    @Autowired
    private TuringResourceService turingResourceService;


    /**
     * 后台-资源列表
     * @param params
     * @return
     */
    @PostMapping("cmsResourceList")
    public Object myResourceList(@RequestBody CmsResourceListParam params) {
        try {
            Object o = turingResourceService.cmsResourceList(params);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, o, "查询资源列表成功"));
        } catch (Exception e) {
            log.error("cmsResourceList exception", e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "查询资源列表时异常"));
        }
    }

}
