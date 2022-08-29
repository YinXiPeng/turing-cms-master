package com.bonc.turing.cms.exercise.service;

import com.alibaba.fastjson.JSONObject;
import com.bonc.turing.cms.exercise.dto.HrCompanyDTO;
import com.bonc.turing.cms.exercise.dto.HrUserCompanyDTO;
import com.bonc.turing.cms.exercise.dto.HrUserInfoListDTO;
import com.bonc.turing.cms.exercise.vo.HrUserCompanyVO;
import com.bonc.turing.cms.exercise.vo.SearchHrUserInfoVO;
import com.bonc.turing.cms.exercise.vo.UpdateUserInfoStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * description:
 *
 * @author lxh
 * @date 2020/4/2 10:15
 */
public interface HrUserInfoService {
    /**
     * 查询账号公司列表
     *
     * @param pageable
     * @param searchHrUserInfoVO
     * @return
     */
    Page<HrUserInfoListDTO> list(Pageable pageable, SearchHrUserInfoVO searchHrUserInfoVO);

    /**
     * 校验公司名称
     *
     * @param jsonObject
     * @return
     */
    boolean judgeName(JSONObject jsonObject);

    /**
     * 获取公司标签
     *
     * @param jsonObject
     * @return
     */
    List getTags(JSONObject jsonObject);

    /**
     * 获取公司列表
     *
     * @return
     */
    JSONObject getCompanyList();

    /**
     * 获取绑定公司信息
     *
     * @param guid
     * @return
     */
    HrUserCompanyDTO getBindCompany(String guid);

    /**
     * 更新账号状态
     *
     * @param updateUserInfoStatus
     */
    void updateStatus(UpdateUserInfoStatus updateUserInfoStatus);

    /**
     * 绑定修改人员信息
     *
     * @param guid
     * @param hrUserCompanyVO
     */
    void bindCompany(String guid, HrUserCompanyVO hrUserCompanyVO);

    /**
     * 查询公司列表信息
     *
     * @param companyName
     * @return
     */
    List<HrCompanyDTO> getCompanyListLikeName(String companyName);
}
