package com.bonc.turing.cms.manage.service.competition;

import com.alibaba.fastjson.JSONObject;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * @ProjectName: cms
 * @Package: com.bonc.turing.cms.manage.service.competition
 * @ClassName: CompetitionModelService
 * @Author: bxt
 * @Description: 竞赛-模型
 * @Date: 2019/7/19 10:39
 * @Version: 1.0
 */
public interface CompetitionModelService {

    /**
     * 方法说明:  竞赛-模型列表
     *
     * @date 2019/7/19 11:01
     * @author bxt
     * @version v 1.0.0
     */
    List<Map<String, Object>> getModelList(Pageable pageable, Map<String, Object> map);

    /**
     * 方法说明:  竞赛-模型总条数
     *
     * @date 2019/7/19 11:02
     * @author bxt
     * @version v 1.0.0
     */
    int getModelListCount(Map<String, Object> map);

    /**
     * 方法说明:  置顶
     *
     * @date 2019/7/19 11:17
     * @author bxt
     * @version v 1.0.0
     */
    void updateTopping(String notebookId,String competitionId);

    /**
     * 方法说明:  取消置顶
     *
     * @date 2019/7/19 11:21
     * @author bxt
     * @version v 1.0.0
     */
    void updateCacleTopping(String notebookId,String competitionId);

    /**
     * 方法说明:   加精
     *
     * @date 2019/7/19 11:26
     * @author bxt
     * @version v 1.0.0
     */
    void updateRefinement(String notebookId,String competitionId);

    /**
     * 方法说明:  取消加精
     *
     * @date 2019/7/19 11:28
     * @author bxt
     * @version v 1.0.0
     */
    void updateCacleRefinement(String notebookId,String competitionId);

    /**
     * 方法说明:  公开
     *
     * @date 2019/7/19 11:33
     * @author bxt
     * @version v 1.0.0
     */
    void updatePublic(String notebookId);

     /**
      * 方法说明:  私有
      * @date 2019/7/19 11:34
      * @author bxt
      * @version v 1.0.0
      */
    void updatePrivate(String notebookId);
     /**
      * 方法说明:  删除
      * @date 2019/7/19 11:36
      * @author bxt
      * @version v 1.0.0
      */
    void updateIsDelete(String notebookId);

     /**
      * 方法说明:  修改标签
      * @date 2019/7/20 12:10
      * @author bxt
      * @version v 1.0.0
      */
    void updatemodelSort(String notebookId,JSONObject params);

}
