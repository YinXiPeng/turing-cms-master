package com.bonc.turing.cms.manage.service.competition;

import com.alibaba.fastjson.JSONObject;

public interface CompetitionRankService {

    /**
     *
     * @param id 竞赛id
     * @return
     * @author yanggang
     * @description: 获取b榜排行
     * @date 2019/7/12 17:08
     */
    Object getRanking(String id, Integer offsetPage, Integer pageSize);

    /**
     *
     * @param id notebookId
     * @return
     * @author yanggang
     * @description: 返显Python文件内容
     * @date 2019/7/12 17:08
     */
    JSONObject getPythonFile(String id);

    /**
     *
     * @param content Python文件内容
     * @param id notebookId
     * @return
     * @author yanggang
     * @description: 保存Python文件
     * @date 2019/7/12 17:12
     */
    void savePythonFile(String id,JSONObject content) throws Exception;

}
