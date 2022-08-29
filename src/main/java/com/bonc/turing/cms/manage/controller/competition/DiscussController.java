package com.bonc.turing.cms.manage.controller.competition;

import com.alibaba.fastjson.JSONObject;
import com.bonc.turing.cms.manage.ResultEntity;
import com.bonc.turing.cms.manage.service.competition.DiscussService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @ProjectName: cms
 * @Package: com.bonc.turing.cms.manage.controller.competition
 * @ClassName: DiscussController
 * @Author: bxt
 * @Description: 竞赛-讨论
 * @Date: 2019/7/17 15:07
 * @Version: 1.0
 */
@RestController
@RequestMapping("/discuss")
public class DiscussController {
    private static Logger logger = LoggerFactory.getLogger(CompetitionManageController.class);
    @Autowired
    DiscussService discussService;


    /*
     * 方法说明:   讨论列表
     * Method:   getDiscussList
     * @param map
     * @param request
     * @return java.lang.Object
     * @date 2019/7/17 16:21
     * @author bxt
     * @version v 1.0.0
     */
    @RequestMapping(value = "get/discussList", method = RequestMethod.POST)
    public Object getDiscussList(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        try {
            if (null == request.getParameter("guid")) {
                logger.error("guid doesn't allow empty");
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, null));
            }
            JSONObject jo = new JSONObject();
            map.put("guid", request.getParameter("guid").toString());
            Pageable pageable = new PageRequest((int) map.get("page"), (int) map.get("size"), new Sort(Sort.Direction.DESC, "sorting", "sort_time", "create_time"));
            // 讨论列表
            List<Map<String, Object>> list = discussService.getDiscussList(pageable, map);
            // 讨论列表总条数
            int totalElements = discussService.getRequirementListCount(map);
            jo.put("requirementList", list);
            jo.put("totalElements", totalElements);
            logger.info("fetch discussList success: guid == > {} "+request.getParameter("guid").toString());
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, jo));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("fetch discussList failure: guid == > {} "+request.getParameter("guid").toString());
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "获取列表失败"));
        }
    }

    /*
     * 方法说明:   置顶
     * Method:   updateTopping
     * @param request
     * @return org.springframework.http.ResponseEntity<?>
     * @date 2019/7/17 15:18
     * @author bxt
     * @version v 1.0.0
     */
    @RequestMapping(value = "update/topping", method = RequestMethod.GET)
    public ResponseEntity<?> updateTopping(HttpServletRequest request) {
        try {
            if (null == request.getParameter("id")) {
                logger.error("id doesn't allow empty");
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "id不能为空"));
            }
            discussService.updateTopping(request.getParameter("id").toString());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("updateTopping exception:id ==> {}"+request.getParameter("id").toString());
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, e.getMessage()));
        }
        return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, null));
    }

    /*
     * 方法说明:   取消置顶
     * Method:   updateCacleTopping
     * @param request
     * @return org.springframework.http.ResponseEntity<?>
     * @date 2019/7/17 15:23
     * @author bxt
     * @version v 1.0.0
     */
    @RequestMapping(value = "update/cacleTopping", method = RequestMethod.GET)
    public ResponseEntity<?> updateCacleTopping(HttpServletRequest request) {
        try {
            if (null == request.getParameter("id")) {
                logger.error("id doesn't allow empty");
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "id不能为空"));
            }
            discussService.updateCacleTopping(request.getParameter("id").toString());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("cancel topping exception :id ==> {}"+request.getParameter("id").toString());
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, e.getMessage()));
        }
        return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, null));
    }

    /*
     * 方法说明:   加精
     * Method:   updateRefinement
     * @param request
     * @return org.springframework.http.ResponseEntity<?>
     * @date 2019/7/17 15:27
     * @author bxt
     * @version v 1.0.0
     */
    @RequestMapping(value = "update/refinement", method = RequestMethod.GET)
    public ResponseEntity<?> updateRefinement(HttpServletRequest request) {
        try {
            if (null == request.getParameter("id")) {
                logger.error("id doesn't allow empty");
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "id不能为空"));
            }
            discussService.updateRefinement(request.getParameter("id").toString());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("refinement exception id ==> {}"+request.getParameter("id").toString());
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, e.getMessage()));
        }
        return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, null));
    }

    /*
     * 方法说明:   取消加精
     * Method:   updateRefinement
     * @param request
     * @return org.springframework.http.ResponseEntity<?>
     * @date 2019/7/17 15:27
     * @author bxt
     * @version v 1.0.0
     */
    @RequestMapping(value = "update/cacleRefinement", method = RequestMethod.GET)
    public ResponseEntity<?> updateCacleRefinement(HttpServletRequest request) {

        try {
            if (null == request.getParameter("id")) {
                logger.error("id doesn't allow empty");
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "id不能为空"));
            }
            discussService.updateCacleRefinement(request.getParameter("id").toString());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("cacel  Refinement failure id ==> {}"+request.getParameter("id").toString());
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, e.getMessage()));
        }
        return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, null));
    }

    /*
     * 方法说明:   显示
     * Method:   updateShow
     * @param request
     * @return org.springframework.http.ResponseEntity<?>
     * @date 2019/7/17 15:30
     * @author bxt
     * @version v 1.0.0
     */
    @RequestMapping(value = "update/show", method = RequestMethod.GET)
    public ResponseEntity<?> updateShow(HttpServletRequest request) {
        try {
            if (null == request.getParameter("id")) {
                logger.error("id doesn't allow empty");
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "id不能为空"));
            }
            discussService.updateShow(request.getParameter("id").toString());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("show exception :id ==> {}"+request.getParameter("id").toString());
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, e.getMessage()));
        }
        return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, null));
    }


    /*
     * 方法说明:   隐藏
     * Method:   updateShow
     * @param request
     * @return org.springframework.http.ResponseEntity<?>
     * @date 2019/7/17 15:31
     * @author bxt
     * @version v 1.0.0
     */
    @RequestMapping(value = "update/hide", method = RequestMethod.GET)
    public ResponseEntity<?> updateHide(HttpServletRequest request) {

        try {
            if (null == request.getParameter("id")) {
                logger.error("id doesn't allow empty");
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "id不能为空"));
            }
            discussService.updateHide(request.getParameter("id").toString());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("hide failure: id ==> {}"+request.getParameter("id").toString());
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, e.getMessage()));
        }
        return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, null));
    }

    /*
     * 方法说明:   删除
     * Method:   deleteDiscuss
     * @param request
     * @return org.springframework.http.ResponseEntity<?>
     * @date 2019/7/17 16:19
     * @author bxt
     * @version v 1.0.0
     */

    @RequestMapping(value = "delete/discuss", method = RequestMethod.GET)
    public ResponseEntity<?> deleteDiscuss(HttpServletRequest request) {

        try {
            if (null == request.getParameter("id")) {
                logger.error("id doesn't allow empty");
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "id不能为空"));
            }
            discussService.deleteDiscuss(request.getParameter("id").toString());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("deleteDiscuss failure: id ==> {}"+request.getParameter("id").toString());
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, e.getMessage()));
        }
        return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, null));
    }


    @RequestMapping(value = "update/remark", method = RequestMethod.POST)
    public ResponseEntity<?> updateRemark(HttpServletRequest request,@RequestBody(required = true) JSONObject params) {
        try {
            if (null == request.getParameter("notebookId")) {
                logger.error("notebookId doesn't allow empty");
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "notebookId不能为空"));
            }
            discussService.updateRemark(request.getParameter("notebookId").toString(),params);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("updateRemark exception : notebookId ==> {}"+request.getParameter("notebookId").toString());
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, e.getMessage()));
        }
        return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, null));
    }
}
