package com.bonc.turing.cms.user.service;

import com.bonc.turing.cms.user.bean.OperateLog;

import java.util.List;

/**
 * 用户操作日志以及积分相关
 */
public interface OperateLogService {
    /**
     * 通过guid查找日志信息
     * @param guid
     * @param beginTime
     * @param endTime
     * @return
     */
    List<OperateLog> findByGuid(String guid, long beginTime, long endTime);


    /**
     * 更新操作日志信息
     * @param operateType
     * @param guid
     */
    void updateOperateLogByOperateType(String operateType,String guid);





}
