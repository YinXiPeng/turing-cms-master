package com.bonc.turing.cms.manage.controller.competition;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bonc.turing.cms.common.utils.StringUtils;
import com.bonc.turing.cms.manage.ResultEntity;
import com.bonc.turing.cms.manage.entity.Judge;
import com.bonc.turing.cms.manage.entity.competition.CmsCompetitionInfo;
import com.bonc.turing.cms.manage.service.competition.ICompetitionManageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping({"/cmsCompetitionInfo/"})
public class CompetitionManageController {

    private static Logger logger = LoggerFactory.getLogger(CompetitionManageController.class);

    @Autowired
    ICompetitionManageService iCompetitionManageService;

    /*
     * 方法说明:   竞赛列表查询
     * Method:   getCompetitionList
     * @param params
     * @return org.springframework.http.ResponseEntity<?>
     * @date 2019/7/13 16:40
     * @author bxt
     * @version v 1.0.0
     */
    @RequestMapping(value = "get/competitionList", method = RequestMethod.POST)
    public ResponseEntity<?> getCompetitionList(@RequestBody(required = true) JSONObject params) {
        try {
            logger.info("begin to request competitionInfo interface");
            JSONObject jo = new JSONObject();
            // 讨论列表
            List<CmsCompetitionInfo> list = iCompetitionManageService.getCompetitionList(params);
            // 总条数
            int totalElements = iCompetitionManageService.getCompetitionListCount(params);
            jo.put("list", list);
            jo.put("totalElements", totalElements);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, jo));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("message ==> {}" + e.getMessage());
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, null));
        }
    }

    /*
     * 方法说明:   根据竞赛ID查询竞赛详细信息
     * Method:   getCompetitionDesc
     * @param competitionId
     * @return org.springframework.http.ResponseEntity<?>
     * @date 2019/7/13 16:41
     * @author bxt
     * @version v 1.0.0
     */
    @RequestMapping(value = "get/competitionInfo", method = RequestMethod.GET)
    public ResponseEntity<?> getCompetitionInfo(HttpServletRequest request) throws ParseException {
        JSONObject jo = new JSONObject();
        try {
            logger.info("begin to request competitionInfo interface");
            jo = iCompetitionManageService.getCompetitionInfo(request.getParameter("competitionId").toString());
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, jo));
        } catch (Exception e) {
            logger.error("message ==> {}" + e.getMessage());
            logger.error("get competition:{} info fail!", request.getParameter("competitionId").toString());
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, jo));
        }
    }

    /*
     * 方法说明:  根据竞赛ID显示/隐藏竞赛信息
     * Method:   deleteCompetition
     * @param competitionId
     * @return org.springframework.http.ResponseEntity<?>
     * @date 2019/7/13 16:42
     * @author bxt
     * @version v 1.0.0
     */
    @RequestMapping(value = "showAndHide/competition", method = RequestMethod.GET)
    public ResponseEntity<?> showAndHideCompetition(HttpServletRequest request) {
        try {
            logger.info("begin to request delete_competition interface");
            iCompetitionManageService.deleteCompetition(request.getParameter("competitionId").toString(), Integer.valueOf(request.getParameter("showHide").toString()));
            logger.info("delete competition: {} success", request.getParameter("competitionId").toString());
            if (Integer.valueOf(request.getParameter("showHide").toString()) == 0) {
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, null, "显示竞赛信息成功!"));
            } else {
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, null, "隐藏竞赛信息成功!"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("message ==> {}" + e.getMessage());
            logger.error("delete competition: {} failed", request.getParameter("competitionId").toString());
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, null));
        }

    }

    /*
     * 方法说明:   创建竞赛
     * Method:   saveCompetitionInfo
     * @param request
     * @param params
     * @return org.springframework.http.ResponseEntity<?>
     * @date 2019/7/18 10:23
     * @author bxt
     * @version v 1.0.0
     */

    @RequestMapping(value = "save/competitionInfo", method = RequestMethod.POST)
    public ResponseEntity<?> saveCompetitionInfo(HttpServletRequest request, @RequestBody(required = true) JSONObject params) {
        logger.info("begin to create competition-baseinfo by user:{}", request.getParameter("guid").toString());
        try {
            //获取竞赛实体
            CmsCompetitionInfo competitionInfo = JSONObject.parseObject(params.getJSONObject("competitionInfoParams").getJSONObject("competitionInfo").toJSONString(), CmsCompetitionInfo.class);
            if ("0".equals(competitionInfo.getIsNeedJudges())) {
                JSONArray judgeArray = params.getJSONObject("competitionInfoParams").getJSONArray("competitionJudges");
                JSONArray judgeArrayFinal = params.getJSONObject("competitionInfoParams").getJSONArray("competitionJudgesFinal");
                if (judgeArray.size() <= 0 && judgeArrayFinal.size() <= 0) {
                    return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, null, "未选择评委！"));
                }

            }

            JSONObject jo = iCompetitionManageService.saveCompetitionInfo(request, params);
            if (null == jo) {
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, null, "竞赛名称重复"));
            }
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, jo));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("message ==> {}" + e.getMessage());
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, e.getMessage()));
        }
    }


    /*
     * 方法说明:   创建竞赛-查询当前竞赛已选评委
     * Method:   getJudges
     * @param request
     * @return org.springframework.http.ResponseEntity<?>
     * @date 2019/7/13 15:28
     * @author bxt
     * @version v 1.0.0
     */
    @RequestMapping(value = "get/Judges", method = RequestMethod.GET)
    public ResponseEntity<?> getJudges(HttpServletRequest request) {
        logger.info("begin to request queryJudges interface");
        if (StringUtils.isEmpty(request.getParameter("competitionId"))) {
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, null));
        }
        List<Judge> judge = null;
        try {
            judge = iCompetitionManageService.queryJudges(request.getParameter("competitionId"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, e.getMessage()));
        }
        if (judge.size() > 0) {
            logger.info("queryJudges Judges:{} success!", judge);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, judge, "评委查询成功！"));
        }
        return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, null));
    }

    /*
     * 方法说明:   创建竞赛-当前竞赛已选评委删除接口
     * Method:   deleteJudges
     * @param request
     * @return org.springframework.http.ResponseEntity<?>
     * @date 2019/7/18 12:04
     * @author bxt
     * @version v 1.0.0
     */

    @RequestMapping(value = "delete/Judges", method = RequestMethod.GET)
    public ResponseEntity<?> deleteJudges(HttpServletRequest request) {
        logger.info("begin to request deleteJudges interface");
        boolean judge = false;
        try {
            judge = iCompetitionManageService.deleteJudges(request.getParameter("judgeId"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, e.getMessage()));
        }
        if (judge) {
            logger.info("deleteJudges Judge:{} success!", judge);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, "删除评委成功！"));
        }
        return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, null));
    }

    /*
     * 方法说明:   创建竞赛-当前竞赛已选评委删除接口
     * Method:   updateSorting
     * @param request
     * @return org.springframework.http.ResponseEntity<?>
     * @date 2019/7/18 12:04
     * @author bxt
     * @version v 1.0.0
     */
    @RequestMapping(value = "update/Sorting", method = RequestMethod.GET)
    public ResponseEntity<?> updateSorting(HttpServletRequest request) {
        try {
            iCompetitionManageService.updateSorting(request.getParameter("competitionId").toString());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("message ==> {}" + e.getMessage());
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, e.getMessage()));
        }
        return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, null));
    }

    /*
     * 方法说明:   置顶
     * Method:   updateTopping
     * @param request
     * @return org.springframework.http.ResponseEntity<?>
     * @date 2019/7/16 15:06
     * @author bxt
     * @version v 1.0.0
     */
    @RequestMapping(value = "update/topping", method = RequestMethod.GET)
    public ResponseEntity<?> updateTopping(HttpServletRequest request) {
        try {
            if (null == request.getParameter("competitionId")) {
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "竞赛编号不能为空"));
            }
            iCompetitionManageService.updateTopping(request.getParameter("competitionId").toString());
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, null));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("message ==> {}" + e.getMessage());
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, e.getMessage()));
        }
    }

    /*
     * 方法说明:   下沉
     * Method:   updateSinkg
     * @param request
     * @return org.springframework.http.ResponseEntity<?>
     * @date 2019/7/16 15:15
     * @author bxt
     * @version v 1.0.0
     */
    @RequestMapping(value = "update/sink", method = RequestMethod.GET)
    public ResponseEntity<?> updateSinkg(HttpServletRequest request) {
        try {
            if (null == request.getParameter("competitionId")) {
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "竞赛编号不能为空"));
            }
            iCompetitionManageService.updateSinkg(request.getParameter("competitionId").toString());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("message ==> {}" + e.getMessage());
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, e.getMessage()));
        }
        return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, null));
    }

    /*
     * 方法说明:   删除
     * Method:   deleteCompetition
     * @param request
     * @return org.springframework.http.ResponseEntity<?>
     * @date 2019/7/16 15:53
     * @author bxt
     * @version v 1.0.0
     */
    @RequestMapping(value = "delete/competitionInfo", method = RequestMethod.GET)
    public ResponseEntity<?> deleteCompetition(HttpServletRequest request) {
        try {
            if (null == request.getParameter("competitionId")) {
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "竞赛编号不能为空"));
            }
            iCompetitionManageService.deleteCompetition(request.getParameter("competitionId").toString());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("message ==> {}" + e.getMessage());
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, e.getMessage()));
        }
        return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, null));
    }

    /*
     * 方法说明:   提交
     * Method:   updateStatus
     * @param request
     * @return org.springframework.http.ResponseEntity<?>
     * @date 2019/7/16 15:53
     * @author bxt
     * @version v 1.0.0
     */
    @RequestMapping(value = "update/submit", method = RequestMethod.GET)
    public ResponseEntity<?> updateSubmit(HttpServletRequest request) {

        try {
            if (null == request.getParameter("competitionId")) {
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "竞赛编号不能为空"));
            }
            int competitionId = iCompetitionManageService.updateSubmit(request.getParameter("competitionId").toString());
            if (1==competitionId){
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "数据集未上传"));
            }
            if (2==competitionId){
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "结果数据集未上传"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("message ==> {}" + e.getMessage());
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "状态修改失败"));
        }
        return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, ""));
    }

    /*
     * 方法说明:   撤回
     * Method:   updateCacle
     * @param request
     * @return org.springframework.http.ResponseEntity<?>
     * @date 2019/7/16 15:55
     * @author bxt
     * @version v 1.0.0
     */
    @RequestMapping(value = "update/cacle", method = RequestMethod.GET)
    public ResponseEntity<?> updateCacle(HttpServletRequest request) {
        try {
            if (null == request.getParameter("competitionId")) {
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "竞赛编号不能为空"));
            }
            iCompetitionManageService.updateCacle(request.getParameter("competitionId").toString());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("message ==> {}" + e.getMessage());
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, e.getMessage()));
        }
        return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, null));
    }


    /*
     * 方法说明:   更新参赛人数
     * Method:   updateCompeteNum
     * @param request
     * @return org.springframework.http.ResponseEntity<?>
     * @date 2019/7/16 17:12
     * @author bxt
     * @version v 1.0.0
     */
    @RequestMapping(value = "update/competeNum", method = RequestMethod.GET)
    public ResponseEntity<?> updateCompeteNum(HttpServletRequest request) {
        try {
            if (null == request.getParameter("competitionId")) {
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "竞赛编号不能为空"));
            }
            iCompetitionManageService.updateCompeteNum(request.getParameter("competitionId").toString());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("message ==> {}" + e.getMessage());
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, e.getMessage()));
        }
        return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, null));
    }

    /*
     * 方法说明:   更新提交数
     * Method:   updateSubmitNum
     * @param request
     * @return org.springframework.http.ResponseEntity<?>
     * @date 2019/7/16 17:25
     * @author bxt
     * @version v 1.0.0
     */
    @RequestMapping(value = "update/submitNum", method = RequestMethod.GET)
    public ResponseEntity<?> updateSubmitNum(HttpServletRequest request) {
        try {
            if (null == request.getParameter("competitionId")) {
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "竞赛编号不能为空"));
            }
            iCompetitionManageService.updateSubmitNum(request.getParameter("competitionId").toString());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("message ==> {}" + e.getMessage());
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, e.getMessage()));
        }
        return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, null));
    }

    /*
     * 方法说明:   评委列表
     * Method:   getJudges
     * @param params
     * @return org.springframework.http.ResponseEntity<?>
     * @date 2019/7/20 15:17
     * @author bxt
     * @version v 1.0.0
     */
    @RequestMapping(value = "getJudges", method = RequestMethod.POST)
    public ResponseEntity<?> getJudges(@RequestBody(required = true) JSONObject params) {
        try {
            JSONObject jo = new JSONObject();
            List<Map<String, Object>> judges = iCompetitionManageService.getCompetitionJudges(params);
            int count = iCompetitionManageService.getCompetitionJudgesCount(params);
            jo.put("judges", judges);
            jo.put("totalElements", count);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, jo));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, null));
        }
    }

    @RequestMapping(value = "get/enterpriseList", method = RequestMethod.GET)
    public ResponseEntity<?> queryEnterprise() {
        try {
            JSONObject jo = new JSONObject();
            List<Map<String, Object>> enterpriseInfoDTOList = iCompetitionManageService.getEnterprise();
            jo.put("enterpriseInfoDTOList", enterpriseInfoDTOList);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, jo));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, null));
        }
    }

}
