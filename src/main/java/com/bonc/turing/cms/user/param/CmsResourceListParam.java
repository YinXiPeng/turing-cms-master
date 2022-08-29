package com.bonc.turing.cms.user.param;

import lombok.Data;

import java.util.Date;

/**
 * @desc 后台上传下载资源列表请求参数
 * @author yh
 * @date 2020.07.10
 */
@Data
public class CmsResourceListParam {
    /**
     * 用户名搜索
     */
    private String nameSearch;

    /**
     *标题搜索
     */
    private String titleSearch;

    /**
     *展示排序方式 -1：全部  1：最新 2：最热
     */
    private Integer sortType;

    /**
     *开始时间
     */
    private Long startTime;

    /**
     *结束时间
     */
    private Long endTime;

    /**
     *资源分类
     */
    private String resourceType;

    /**
     * 是否隐藏
     */
    private Integer isHide;


    /**
     * 页码
     */
    private Integer pageNum;

    /**
     * 页面大小
     */
    private Integer pageSize;




}
