package com.bonc.turing.cms.manage.service.competition;

import com.alibaba.fastjson.JSONObject;
import com.bonc.turing.cms.manage.entity.Judge;
import com.bonc.turing.cms.manage.entity.competition.CmsCompetitionInfo;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface ICompetitionManageService {

    /**
     * 方法说明:  创建竞赛
     *
     * @date 2019/7/18 12:03
     * @author bxt
     * @version v 1.0.0
     */
    JSONObject saveCompetitionInfo(HttpServletRequest request, JSONObject params) throws ParseException;

    /**
     * 方法说明:  创建竞赛-查询当前竞赛已选评委
     *
     * @date 2019/7/18 12:03
     * @author bxt
     * @version v 1.0.0
     */
    List<Judge> queryJudges(String competitionId);

    /**
     * 方法说明:  创建竞赛-当前竞赛已选评委删除接口
     *
     * @date 2019/7/18 12:04
     * @author bxt
     * @version v 1.0.0
     */
    boolean deleteJudges(String judgeId);

    /**
     * 方法说明:  根据竞赛ID显示/隐藏竞赛信息
     *
     * @date 2019/7/18 11:44
     * @author bxt
     * @version v 1.0.0
     */
    void deleteCompetition(String competitionId, int showHide);

    /**
     * 方法说明:  创建竞赛-当前竞赛已选评委删除接口
     *
     * @date 2019/7/18 12:04
     * @author bxt
     * @version v 1.0.0
     */
    void updateSorting(String competitionId);

    /**
     * 方法说明:  置顶
     *
     * @date 2019/7/18 12:04
     * @author bxt
     * @version v 1.0.0
     */
    void updateTopping(String competitionId);

    /**
     * 方法说明:   下沉
     *
     * @date 2019/7/18 12:05
     * @author bxt
     * @version v 1.0.0
     */
    void updateSinkg(String competitionId);

    /**
     * 方法说明:  删除
     *
     * @date 2019/7/18 12:05
     * @author bxt
     * @version v 1.0.0
     */
    void deleteCompetition(String competitionId);

    /**
     * 方法说明:  提交
     *
     * @date 2019/7/18 12:05
     * @author bxt
     * @version v 1.0.0
     */
    int updateSubmit(String competitionId);

    /**
     * 方法说明:   撤回
     *
     * @date 2019/7/18 12:05
     * @author bxt
     * @version v 1.0.0
     */
    void updateCacle(String competitionId);

    /**
     * 方法说明:  更新参赛人数
     *
     * @date 2019/7/18 12:05
     * @author bxt
     * @version v 1.0.0
     */
    void updateCompeteNum(String competitionId);

    /**
     * 方法说明:  更新提交数
     *
     * @date 2019/7/18 12:05
     * @author bxt
     * @version v 1.0.0
     */
    void updateSubmitNum(String competitionId);

    /**
     * 方法说明: 讨论列表
     *
     * @date 2019/7/18 11:38
     * @author bxt
     * @version v 1.0.0
     */
    List<CmsCompetitionInfo> getCompetitionList(JSONObject params);

    /**
     * 方法说明:  总条数
     *
     * @date 2019/7/18 11:38
     * @author bxt
     * @version v 1.0.0
     */
    int getCompetitionListCount(JSONObject params);

    /**
     * 方法说明:  根据竞赛ID查询竞赛详细信息
     *
     * @date 2019/7/18 11:39
     * @author bxt
     * @version v 1.0.0
     */
    JSONObject getCompetitionInfo(String competitionId) throws ParseException;

    List<Map<String, Object>> getCompetitionJudges(JSONObject params);

    int getCompetitionJudgesCount(JSONObject params);

    List<Map<String, Object>> getEnterprise();

}
