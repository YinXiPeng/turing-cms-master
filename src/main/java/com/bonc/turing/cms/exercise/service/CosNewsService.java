package com.bonc.turing.cms.exercise.service;

import com.bonc.turing.cms.exercise.domain.CosNews;

/**
 * @desc 高校资讯
 * @author  yh
 * @date 2020.06.28
 */
public interface CosNewsService {
    /**
     * 创建/编辑资讯
     * @param cosNews
     * @return
     */
    Object addOrEditNews(CosNews cosNews);

    /**
     * 删除资讯
     * @param id
     * @return
     */
    Object deleteNewsById(String id);

    /**
     * 查询资讯详情
     * @param id
     * @return
     */
    Object findNewsById(String id);


    /**
     *  隐藏/显示
     * @param id
     * @param isHide
     * @return
     */
    Object changeNewsStatus(String id,Integer isHide);

    /**
     * 后台资讯列表
     * @param schoolId
     * @param pageNumber
     * @param pageSize
     * @return
     */
    Object showCmsNewsList(String schoolId,Integer newsType,Integer pageNumber,Integer pageSize);
}
