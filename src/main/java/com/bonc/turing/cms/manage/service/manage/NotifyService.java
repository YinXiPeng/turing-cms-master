package com.bonc.turing.cms.manage.service.manage;

import com.alibaba.fastjson.JSONObject;
import com.bonc.turing.cms.manage.entity.notify.Notify;

import java.util.List;

/**
 * @author lky
 * @desc
 * @date 2019/8/6 16:58
 */
public interface NotifyService {
    JSONObject getNotifyList(int pageNum, int pageSize, String keyWord);
}
