package com.bonc.turing.cms.common.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;

/**
 * @author yh
 * @desc 统一封装分页返回对象(mybatis与jpa两种方式 ， 其他依次类推)
 * @date 2020.07.21
 */
@Data
public class PageInfoEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private Object list;
    private JSONObject pageInfo;

    public PageInfoEntity(Object list) {
        pageInfo = new JSONObject();
        if (list instanceof com.github.pagehelper.PageInfo) {
            //mybatis 分页对象
            com.github.pagehelper.PageInfo page = (com.github.pagehelper.PageInfo) list;
            pageInfo.put("pageNum", page.getPageNum());
            pageInfo.put("pageSize", page.getPageSize());
            pageInfo.put("totalPages", page.getPages());
            pageInfo.put("totalElements", page.getTotal());
            this.list = page.getList();
        } else if (list instanceof org.springframework.data.domain.Page) {
            //jpa 分页对象
            org.springframework.data.domain.Page page = (org.springframework.data.domain.Page) list;
            pageInfo.put("pageNum", page.getNumber());
            pageInfo.put("pageSize", page.getNumberOfElements());
            pageInfo.put("totalPages", page.getTotalPages());
            pageInfo.put("totalElements", page.getTotalElements());
            this.list= page.getContent();
        }
    }
}
