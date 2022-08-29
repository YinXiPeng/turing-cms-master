package com.bonc.turing.cms.exercise.controller;

import com.alibaba.fastjson.JSONObject;
import com.bonc.turing.cms.exercise.dto.HrCompanyDTO;
import com.bonc.turing.cms.exercise.dto.HrUserCompanyDTO;
import com.bonc.turing.cms.exercise.dto.HrUserInfoListDTO;
import com.bonc.turing.cms.exercise.service.HrUserInfoService;
import com.bonc.turing.cms.exercise.utils.GeneralException;
import com.bonc.turing.cms.exercise.utils.ValidationResult;
import com.bonc.turing.cms.exercise.utils.ValidationUtils;
import com.bonc.turing.cms.exercise.vo.HrUserCompanyVO;
import com.bonc.turing.cms.exercise.vo.SearchHrUserInfoVO;
import com.bonc.turing.cms.exercise.vo.UpdateUserInfoStatus;
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
 * @date 2020/4/2 10:12
 */
@RequestMapping("/hrUserInfo")
@RestController
@Slf4j
public class HrUserInfoController {

    @Autowired
    private HrUserInfoService hrUserInfoService;

    /**
     * 账号公司列表
     *
     * @param pageable
     * @param searchHrUserInfoVO
     * @return
     */
    @GetMapping("/list")
    public Object list(@PageableDefault Pageable pageable, SearchHrUserInfoVO searchHrUserInfoVO) {
        try {
            Page<HrUserInfoListDTO> hrUserInfoListDtoPage = hrUserInfoService.list(pageable, searchHrUserInfoVO);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, hrUserInfoListDtoPage, "查询账号公司列表成功"));
        } catch (GeneralException ge) {
            log.error("listPaper is error:{}", ge.toString());
            return ResponseEntity.ok().body(new ResultEntity(ge.getStatus(), "", ge.getMsg()));
        } catch (Exception e) {
            log.error("listPaper is error", e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "查询账号公司列表失败"));
        }
    }

    /**
     * @param
     * @return
     * @desc hr和公司进行绑定
     * @auth lky
     * @date 2020/2/21 10:52
     */
    @PostMapping("/update")
    public Object bindCompany(@RequestBody HrUserCompanyVO hrUserCompanyVO) {
        try {
            ValidationResult validationResult = ValidationUtils.validateEntity(hrUserCompanyVO);
            if (validationResult.isHasErrors()) {
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.ERROR_PARAMETER, validationResult.getKeyErrors(), "参数错误"));
            }
            String guid = hrUserCompanyVO.getGuid();
            hrUserInfoService.bindCompany(guid, hrUserCompanyVO);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, "", "编辑账号公司信息成功"));
        } catch (GeneralException ge) {
            log.error("bindCompany is error:{}", ge.toString());
            return ResponseEntity.ok().body(new ResultEntity(ge.getStatus(), "", ge.getMsg()));
        } catch (Exception e) {
            log.error("bindCompany is failed ", e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "编辑账号公司信息失败"));
        }
    }

    /**
     * @param
     * @return
     * @desc 根据guid查询hr绑定的公司
     * @auth lky
     * @date 2020/2/21 12:22
     */
    @GetMapping("/getBindCompany")
    public Object getBindCompany(@RequestParam String hrGuid) {
        try {
            HrUserCompanyDTO hrUserCompanyDTO = hrUserInfoService.getBindCompany(hrGuid);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, hrUserCompanyDTO, "查询公司成功"));
        } catch (GeneralException ge) {
            log.error("getBindCompany is error:{}", ge.toString());
            return ResponseEntity.ok().body(new ResultEntity(ge.getStatus(), "", ge.getMsg()));
        } catch (Exception e) {
            log.error("getBindCompany is failed ", e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "查询绑定公司失败"));
        }
    }


    /**
     * @param
     * @return
     * @desc 公司重名校验
     * @auth lky
     * @date 2020/2/25 9:24
     */
    @PostMapping("/judgeName")
    public Object judgeName(@RequestBody JSONObject jsonObject) {
        try {
            boolean flag = hrUserInfoService.judgeName(jsonObject);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, flag, "公司重名校验成功"));
        } catch (GeneralException ge) {
            log.error("getBindCompany is error:{}", ge.toString());
            return ResponseEntity.ok().body(new ResultEntity(ge.getStatus(), "", ge.getMsg()));
        } catch (Exception e) {
            log.error("judgeName is failed");
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "公司重名校验失败,请稍后再试"));
        }
    }

    /**
     * @param
     * @return
     * @desc 根据名字模糊查询公司标签
     * @auth lky
     * @date 2020/2/25 18:10
     */
    @PostMapping("/getTags")
    public Object getTags(@RequestBody JSONObject jsonObject) {
        try {
            List tags = hrUserInfoService.getTags(jsonObject);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, tags, "查询成功"));
        } catch (Exception e) {
            log.error("getTags is failed");
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "查询失败,请稍后再试"));
        }
    }

    /**
     * @param
     * @return
     * @desc 查询公司列表, 公司规模, 公司类型列表
     * @auth lky
     * @date 2020/2/21 13:50
     */
    @GetMapping("/getCompanyList")
    public Object getCompanyList() {
        try {
            JSONObject jsonObject = hrUserInfoService.getCompanyList();
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, jsonObject, "查询公司列表成功"));
        } catch (Exception e) {
            log.error("getCompanyList is failed ", e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "查询公司列表失败"));
        }
    }

    /**
     * 查询公司列表
     *
     * @param companyName
     * @return
     */
    @GetMapping("/company/list")
    public Object getCompanyListLikeName(String companyName) {
        try {
            List<HrCompanyDTO> hrCompanyDTOList = hrUserInfoService.getCompanyListLikeName(companyName);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, hrCompanyDTOList, "查询公司列表成功"));
        } catch (Exception e) {
            log.error("getCompanyList is failed ", e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "查询公司列表失败"));
        }
    }

    /**
     * 更新账号状态
     *
     * @param updateUserInfoStatus
     * @return
     */
    @PostMapping("/updateStatus")
    public Object updateStatus(@RequestBody UpdateUserInfoStatus updateUserInfoStatus) {
        try {
            if (StringUtils.isBlank(updateUserInfoStatus.getGuid()) || Objects.isNull(updateUserInfoStatus.getStatus())) {
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.ERROR_PARAMETER, "", "参数错误"));
            }
            hrUserInfoService.updateStatus(updateUserInfoStatus);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, "", "更新账号状态成功"));
        } catch (Exception e) {
            log.error("getCompanyList is failed ", e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "更新账号状态失败"));
        }
    }


}
