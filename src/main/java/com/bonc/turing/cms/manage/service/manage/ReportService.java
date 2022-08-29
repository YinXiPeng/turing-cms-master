package com.bonc.turing.cms.manage.service.manage;

import java.util.List;
import java.util.Map;

/**
 * @author lky
 * @desc
 * @date 2019/8/7 14:46
 */
public interface ReportService {
    Map<String,Object> getReportList(int pageNum, int pageSize);

    int changeReportStatus(String guid, String type, String reportId,int status);
}
