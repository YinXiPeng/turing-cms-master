package com.bonc.turing.cms.exercise.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.bonc.turing.cms.exercise.domain.CosPaper;
import com.bonc.turing.cms.exercise.dto.CosPaperListDTO;
import com.bonc.turing.cms.exercise.dto.CosSchoolCourseListDTO;
import com.bonc.turing.cms.exercise.service.CosPaperService;
import com.bonc.turing.cms.exercise.utils.GeneralException;
import com.bonc.turing.cms.exercise.vo.PaperAddUpdateVO;
import com.bonc.turing.cms.exercise.vo.SearchPaperVO;
import com.bonc.turing.cms.manage.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * description:
 *
 * @author lxh
 * @date 2020/2/21 12:20
 */
@RequestMapping("/cosPaper")
@RestController
@Slf4j
public class CosPaperController {

    @Autowired
    private CosPaperService cosPaperService;

    /**
     * 添加试卷
     *
     * @param paperAddUpdateVo
     * @return
     * @throws Exception
     */
    @PostMapping("/add")
    public Object addPaper(@RequestBody PaperAddUpdateVO paperAddUpdateVo) {
        try {
            if (CollectionUtils.isEmpty(paperAddUpdateVo.getQuestionList()) || StringUtils.isBlank(paperAddUpdateVo.getName())
                    || Objects.isNull(paperAddUpdateVo.getPaperOrder()) || StringUtils.isBlank(paperAddUpdateVo.getSchoolId())
                    || Objects.isNull(paperAddUpdateVo.getFinishTime()) || StringUtils.isBlank(paperAddUpdateVo.getCreateById())
                    || StringUtils.isBlank(paperAddUpdateVo.getCourseId()) || Objects.isNull(paperAddUpdateVo.getExamTime())) {
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.ERROR_PARAMETER, "", "参数错误"));
            }
            CosPaper cosPaper = cosPaperService.addOrUpdatePaper(paperAddUpdateVo, true);
            cosPaper.setFinishTime(paperAddUpdateVo.getFinishTime());
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, cosPaper, "添加试卷成功"));
        } catch (GeneralException ge) {
            log.error("addPaper is error:{}", ge.toString());
            return ResponseEntity.ok().body(new ResultEntity(ge.getStatus(), "", ge.getMsg()));
        } catch (Exception e) {
            log.error("addPaper is error", e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "添加试卷失败"));
        }
    }

    /**
     * 更新试卷内容
     *
     * @param paperAddUpdateVo
     * @return
     * @throws Exception
     */
    @PostMapping("/update")
    public Object updatePaper(@RequestBody PaperAddUpdateVO paperAddUpdateVo) {
        try {
            if (CollectionUtils.isEmpty(paperAddUpdateVo.getQuestionList()) || StringUtils.isBlank(paperAddUpdateVo.getName())
                    || StringUtils.isBlank(paperAddUpdateVo.getId()) || StringUtils.isBlank(paperAddUpdateVo.getSchoolId())
                    || Objects.isNull(paperAddUpdateVo.getPaperOrder()) || Objects.isNull(paperAddUpdateVo.getFinishTime())
                    || StringUtils.isBlank(paperAddUpdateVo.getCreateById()) || StringUtils.isBlank(paperAddUpdateVo.getCourseId())
                    || Objects.isNull(paperAddUpdateVo.getExamTime())) {
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.ERROR_PARAMETER, "", "参数错误"));
            }
            CosPaper cosPaper = cosPaperService.addOrUpdatePaper(paperAddUpdateVo, false);
            cosPaper.setFinishTime(paperAddUpdateVo.getFinishTime());
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, cosPaper, "更新试卷成功"));
        } catch (GeneralException ge) {
            log.error("updatePaper is error:{}", ge.toString());
            return ResponseEntity.ok().body(new ResultEntity(ge.getStatus(), "", ge.getMsg()));
        } catch (Exception e) {
            log.error("updatePaper is error", e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "更新试卷失败"));
        }
    }

    /**
     * 返显试卷详情
     *
     * @param isUpdate
     * @param id
     * @return
     * @throws Exception
     */
    @GetMapping("/paperInfo")
    public Object paperInfo(@RequestParam Boolean isUpdate, @RequestParam String id) {
        try {
            JSONObject paperInfo = cosPaperService.paperInfo(isUpdate, id);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, paperInfo, "查询试卷详情成功"));
        } catch (GeneralException ge) {
            log.error("paperInfo is error:{}", ge.toString());
            return ResponseEntity.ok().body(new ResultEntity(ge.getStatus(), "", ge.getMsg()));
        } catch (Exception e) {
            log.error("paperInfo is error", e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "查询试卷详情失败"));
        }
    }

    /**
     * 试卷列表
     *
     * @param pageable
     * @param searchPaperVo
     * @return
     */
    @GetMapping("/list")
    public Object listPaper(@PageableDefault Pageable pageable, SearchPaperVO searchPaperVo) {
        try {
            if (StringUtils.isBlank(searchPaperVo.getCreateById())) {
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.ERROR_PARAMETER, "", "参数错误"));
            }
            Page<CosPaperListDTO> paperPage = cosPaperService.listPaper(pageable, searchPaperVo);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, paperPage, "查询试卷列表成功"));
        } catch (GeneralException ge) {
            log.error("listPaper is error:{}", ge.toString());
            return ResponseEntity.ok().body(new ResultEntity(ge.getStatus(), "", ge.getMsg()));
        } catch (Exception e) {
            log.error("listPaper is error", e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "查询试卷列表失败"));
        }
    }

    /**
     * 删除试卷
     *
     * @param id
     * @return
     */
    @GetMapping("/delete")
    public Object delete(@RequestParam String id) {
        try {
            cosPaperService.delete(id);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, "", "删除试卷成功"));
        } catch (GeneralException ge) {
            log.error("delete is error:{}", ge.toString());
            return ResponseEntity.ok().body(new ResultEntity(ge.getStatus(), "", ge.getMsg()));
        } catch (Exception e) {
            log.error("delete is error", e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "删除试卷失败"));
        }
    }

    /**
     * 校验试卷名称唯一性
     *
     * @param name
     * @param schoolId
     * @param id
     * @return
     */
    @GetMapping("/checkUniqueName")
    public Object checkUniqueName(@RequestParam String name, @RequestParam String schoolId, String id) {
        try {
            Boolean result = cosPaperService.checkUniqueName(name, schoolId, id);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, result, "校验试卷名称唯一性成功"));
        } catch (GeneralException ge) {
            log.error("checkUniqueName is error:{}", ge.toString());
            return ResponseEntity.ok().body(new ResultEntity(ge.getStatus(), "", ge.getMsg()));
        } catch (Exception e) {
            log.error("checkUniqueName is error", e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "校验试卷名称唯一性失败"));
        }
    }

    /**
     * 新增或编辑试卷绑定课程列表信息
     *
     * @param schoolId
     * @return
     */
    @GetMapping("/schoolCourses")
    public Object checkUniqueName(@RequestParam String schoolId) {
        try {
            List<CosSchoolCourseListDTO> courseList = cosPaperService.schoolCourseList(schoolId);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, courseList, "查询学校课程列表成功"));
        } catch (GeneralException ge) {
            log.error("schoolCourses is error:{}", ge.toString());
            return ResponseEntity.ok().body(new ResultEntity(ge.getStatus(), "", ge.getMsg()));
        } catch (Exception e) {
            log.error("schoolCourses is error", e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "查询学校课程列表成功失败"));
        }
    }

}
