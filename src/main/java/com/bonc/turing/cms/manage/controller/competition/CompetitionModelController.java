package com.bonc.turing.cms.manage.controller.competition;

import com.alibaba.fastjson.JSONObject;
import com.bonc.turing.cms.manage.ResultEntity;
import com.bonc.turing.cms.manage.service.competition.CompetitionModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @ProjectName: cms
 * @Package: com.bonc.turing.cms.manage.controller.competition
 * @ClassName: CompetitionModelController
 * @Author: bxt
 * @Description: 竞赛-模型
 * @Date: 2019/7/19 10:38
 * @Version: 1.0
 */
@RestController
@RequestMapping({"/competitionModel/"})
public class CompetitionModelController {

    private static Logger logger = LoggerFactory.getLogger(CompetitionManageController.class);

    @Autowired
    CompetitionModelService competitionModelService;

    /*
     * 方法说明:   竞赛-模型列表
     * Method:   getmodelList
     * @param map
     * @param request
     * @return java.lang.Object
     * @date 2019/7/19 11:01
     * @author bxt
     * @version v 1.0.0
     */
    @RequestMapping(value = "get/modelList", method = RequestMethod.POST)
    public Object getmodelList(@RequestBody Map<String, Object> map, HttpServletRequest request) {

        try {
            if (null == request.getParameter("guid")) {
                logger.error("guid doesn't allow empty");
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, null));
            }
            JSONObject jo = new JSONObject();
            map.put("guid", request.getParameter("guid").toString());
            Pageable pageable = new PageRequest((int) map.get("page"), (int) map.get("size")/*, new Sort(Sort.Direction.DESC, "sort", "modifyTime", "createTime")*/);
            // 竞赛-模型列表
            List<Map<String, Object>> list = competitionModelService.getModelList(pageable, map);
            // 竞赛-模型总条数
//            int totalElements = competitionModelService.getModelListCount(map);
            jo.put("modelList", list);
            jo.put("totalElements", list.size());
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, jo));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("get modelList exception :guid == > {} "+request.getParameter("guid").toString());
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "获取列表失败"));
        }
    }

    /*
     * 方法说明:   置顶
     * Method:   updateTopping
     * @param request
     * @return org.springframework.http.ResponseEntity<?>
     * @date 2019/7/19 11:17
     * @author bxt
     * @version v 1.0.0
     */
    @RequestMapping(value = "update/topping", method = RequestMethod.GET)
    public ResponseEntity<?> updateTopping(HttpServletRequest request) {

        try {
            if (null == request.getParameter("notebookId")) {
                logger.error("notebookId  doesn't allow empty");
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "notebookId不能为空"));
            }
            if (null == request.getParameter("competitionId")) {
                logger.error("competitionId doesn't allow empty");
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "competitionId不能为空"));
            }
            competitionModelService.updateTopping(request.getParameter("notebookId").toString(),request.getParameter("competitionId").toString());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("topping exception : notebookId ==> {}"+request.getParameter("notebookId").toString());
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, e.getMessage()));
        }
        return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, null));
    }

    /*
     * 方法说明:   取消置顶
     * Method:   updateCacleTopping
     * @param request
     * @return org.springframework.http.ResponseEntity<?>
     * @date 2019/7/19 11:21
     * @author bxt
     * @version v 1.0.0
     */
    @RequestMapping(value = "update/cacleTopping", method = RequestMethod.GET)
    public ResponseEntity<?> updateCacleTopping(HttpServletRequest request) {

        try {
            if (null == request.getParameter("notebookId")) {
                logger.error("notebookId doesn't allow empty");
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "notebookId不能为空"));
            }
            if (null == request.getParameter("competitionId")) {
                logger.error("competitionId doesn't allow empty");
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "competitionId不能为空"));
            }
            competitionModelService.updateCacleTopping(request.getParameter("notebookId").toString(),request.getParameter("competitionId").toString());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("CacleTopping exception:notebookId ==> {}"+request.getParameter("notebookId").toString());
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, e.getMessage()));
        }
        return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, null));
    }

    /*
     * 方法说明:   加精
     * Method:   updateRefinement
     * @param request
     * @return org.springframework.http.ResponseEntity<?>
     * @date 2019/7/19 11:25
     * @author bxt
     * @version v 1.0.0
     */
    @RequestMapping(value = "update/refinement", method = RequestMethod.GET)
    public ResponseEntity<?> updateRefinement(HttpServletRequest request) {
        try {
            if (null == request.getParameter("notebookId")) {
                logger.error("notebookId doesn't allow empty");
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "notebookId不能为空"));
            }
            if (null == request.getParameter("competitionId")) {
                logger.error("competitionId doesn't allow empty");
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "competitionId不能为空"));
            }
            competitionModelService.updateRefinement(request.getParameter("notebookId").toString(),request.getParameter("competitionId").toString());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Refinement exception notebookId ==> {}"+request.getParameter("notebookId").toString());
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, e.getMessage()));
        }
        return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, null));
    }

    /*
     * 方法说明:   取消加精
     * Method:   updateCacleRefinement
     * @param request
     * @return org.springframework.http.ResponseEntity<?>
     * @date 2019/7/19 11:28
     * @author bxt
     * @version v 1.0.0
     */
    @RequestMapping(value = "update/cacleRefinement", method = RequestMethod.GET)
    public ResponseEntity<?> updateCacleRefinement(HttpServletRequest request) {
        try {
            if (null == request.getParameter("notebookId")) {
                logger.error("notebookId doesn't allow empty");
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "notebookId不能为空"));
            }
            if (null == request.getParameter("competitionId")) {
                logger.error("competitionId doesn't allow empty");
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "competitionId不能为空"));
            }
            competitionModelService.updateCacleRefinement(request.getParameter("notebookId").toString(),request.getParameter("competitionId").toString());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("CacleRefinement exception notebookId ==> {}"+request.getParameter("notebookId").toString());
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, e.getMessage()));
        }
        return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, null));
    }

    /*
     * 方法说明:   公开
     * Method:   updatePublic
     * @param request
     * @return org.springframework.http.ResponseEntity<?>
     * @date 2019/7/19 11:33
     * @author bxt
     * @version v 1.0.0
     */
    @RequestMapping(value = "update/public", method = RequestMethod.GET)
    public ResponseEntity<?> updatePublic(HttpServletRequest request) {
        try {
            if (null == request.getParameter("notebookId")) {
                logger.error("notebookId doesn't allow empty");
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "notebookId不能为空"));
            }
            competitionModelService.updatePublic(request.getParameter("notebookId").toString());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("public exception: notebookId  ==> {}"+request.getParameter("notebookId").toString());
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, e.getMessage()));
        }
        return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, null));
    }

    /*
     * 方法说明:   私有
     * Method:   updatePrivate
     * @param request
     * @return org.springframework.http.ResponseEntity<?>
     * @date 2019/7/19 11:34
     * @author bxt
     * @version v 1.0.0
     */
    @RequestMapping(value = "update/private", method = RequestMethod.GET)
    public ResponseEntity<?> updatePrivate(HttpServletRequest request) {
        try {
            if (null == request.getParameter("notebookId")) {
                logger.error("notebookId doesn't allow empty");
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "notebookId不能为空"));
            }
            competitionModelService.updatePrivate(request.getParameter("notebookId").toString());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Private  exception: notebookId ==> {}"+request.getParameter("notebookId").toString());
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, e.getMessage()));
        }
        return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, null));
    }
    /*
     * 方法说明:   删除
     * Method:   updateIsDelete
     * @param request
     * @return org.springframework.http.ResponseEntity<?>
     * @date 2019/7/19 11:35
     * @author bxt
     * @version v 1.0.0
     */
    @RequestMapping(value = "update/isDelete", method = RequestMethod.GET)
    public ResponseEntity<?> updateIsDelete(HttpServletRequest request) {

        try {
            if (null == request.getParameter("notebookId")) {
                logger.error("notebookId doesn't allow empty");
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "notebookId不能为空"));
            }
            competitionModelService.updateIsDelete(request.getParameter("notebookId").toString());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("updateIsDelete exception notebookId ==> {}"+request.getParameter("notebookId").toString());
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, e.getMessage()));
        }
        return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, null));
    }

    /*
     * 方法说明:   修改标签
     * Method:   updatemodelSort
     * @param request
     * @param params
     * @return org.springframework.http.ResponseEntity<?>
     * @date 2019/7/20 12:10
     * @author bxt
     * @version v 1.0.0
     */
    @RequestMapping(value = "update/modelSort", method = RequestMethod.POST)
    public ResponseEntity<?> updatemodelSort(HttpServletRequest request,@RequestBody(required = true) JSONObject params) {
        try {
            if (null == request.getParameter("notebookId")) {
                logger.error("notebookId doesn't allow empty");
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "notebookId不能为空"));
            }
            competitionModelService.updatemodelSort(request.getParameter("notebookId").toString(),params);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("updatemodelSort Exception: notebookId ==> {}"+request.getParameter("notebookId").toString());
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, e.getMessage()));
        }
        return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, null));
    }

}

