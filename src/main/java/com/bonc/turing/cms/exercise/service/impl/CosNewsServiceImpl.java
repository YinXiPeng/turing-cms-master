package com.bonc.turing.cms.exercise.service.impl;

import com.bonc.turing.cms.exercise.dao.repository.CosNewsRepository;
import com.bonc.turing.cms.exercise.domain.CosNews;
import com.bonc.turing.cms.exercise.service.CosNewsService;
import com.bonc.turing.cms.exercise.utils.HtmlUtil;
import com.bonc.turing.cms.manage.ResultEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

/**
 * @author yh
 * @desc 高校资讯
 * @date 2020.06.28
 */
@Service
public class CosNewsServiceImpl implements CosNewsService {

    private static Logger logger = LoggerFactory.getLogger(CosNewsService.class);

    @Autowired
    private CosNewsRepository cosNewsRepository;

    /**
     * 创建/编辑资讯
     *
     * @param cosNews
     * @return
     */
    @Override
    public Object addOrEditNews(CosNews cosNews) {
        try {
            if (null == cosNews.getId() || "".equals(cosNews.getId())) {
                //新建
                cosNews.setCreateTime(new Date());
                cosNews.setUpdateTime(new Date());
            } else {
                //编辑
                Optional<CosNews> news = cosNewsRepository.findById(cosNews.getId());
                if (news.isPresent()) {
                    cosNews.setCreateTime(news.get().getCreateTime());
                }
                cosNews.setUpdateTime(new Date());
            }
            //截取内容前50字作为概述
            if (null != cosNews.getHtmlContent()) {
                if (HtmlUtil.cleanAllHtmlTag(cosNews.getHtmlContent()).length() < 50) {
                    cosNews.setOverview(HtmlUtil.cleanAllHtmlTag(cosNews.getHtmlContent()));
                } else {
                    cosNews.setOverview(HtmlUtil.cleanAllHtmlTag(cosNews.getHtmlContent()).substring(0, 50));
                }
            }
            //文本内容与url地址二选一
            if (cosNews.getType() == 1) {
                //内容文本
                cosNews.setMarkdownContent(cosNews.getMarkdownContent());
                cosNews.setHtmlContent(cosNews.getHtmlContent());
                cosNews.setContentUrl("");
            } else {
                //url地址
                cosNews.setMarkdownContent("");
                cosNews.setHtmlContent("");
                cosNews.setContentUrl(cosNews.getContentUrl());
            }
            cosNewsRepository.save(cosNews);
            return ResultEntity.ResultStatus.OK.getMsg();
        } catch (Exception e) {
            logger.error("addOrEditNews exception", e);
            return ResultEntity.ResultStatus.FAILURE.getMsg();
        }
    }

    /**
     * 删除资讯
     *
     * @param id
     * @return
     */
    @Override
    public Object deleteNewsById(String id) {
        try {
            cosNewsRepository.deleteById(id);
            return ResultEntity.ResultStatus.OK.getMsg();
        } catch (Exception e) {
            logger.error("deleteNewsById exception", e);
            return ResultEntity.ResultStatus.FAILURE.getMsg();
        }
    }

    /**
     * 查询资讯详情
     *
     * @param id
     * @return
     */
    @Override
    public Object findNewsById(String id) {
        Optional<CosNews> news = cosNewsRepository.findById(id);
        if (news.isPresent()) {
            return news;
        } else {
            return ResultEntity.ResultStatus.FAILURE.getMsg();
        }
    }

    /**
     * 隐藏/显示
     *
     * @param id     资讯id
     * @param isHide 0:显示 1：隐藏
     * @return
     */
    @Override
    public Object changeNewsStatus(String id, Integer isHide) {
        Optional<CosNews> news = cosNewsRepository.findById(id);
        if (news.isPresent()) {
            news.get().setUpdateTime(new Date());
            news.get().setIsHide(isHide);
            cosNewsRepository.save(news.get());
            return ResultEntity.ResultStatus.OK.getMsg();
        } else {
            return ResultEntity.ResultStatus.FAILURE.getMsg();
        }
    }


    /**
     * 后台资讯列表
     *
     * @param schoolId
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @Override
    public Object showCmsNewsList(String schoolId,Integer newsType, Integer pageNumber, Integer pageSize) {
        Sort sort = new Sort(Sort.Direction.ASC, "isHide").and(new Sort(Sort.Direction.DESC, "updateTime"));

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        return cosNewsRepository.findAllBySchoolIdAndNewsType(schoolId,newsType, pageable);
    }
}
