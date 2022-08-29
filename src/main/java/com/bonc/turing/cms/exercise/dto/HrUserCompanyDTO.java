package com.bonc.turing.cms.exercise.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * description:
 *
 * @author lxh
 * @date 2020/4/10 16:23
 */
@Data
public class HrUserCompanyDTO {

    /**
     * 公司ID
     */
    private String companyId;
    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 公司规模
     */
    private Integer companySize;

    /**
     * 公司类型
     */
    private Integer companyType;

    /**
     * 公司简介
     */
    private String companyIntroduction;

    /**
     * 公司标签
     */
    private String companyTags;
    /**
     * logo
     */
    private String logo;

    /**
     * 公司营业执照
     */
    private String companyLicense;

    /**
     * 联系人
     */
    private String contactName;

    /**
     * 联系人手机号
     */
    private String contactPhone;

    /**
     * 联系人email
     */
    private String contactEmail;

    /**
     * 账号状态
     */
    private Integer status;

    /**
     * 用户guid
     */
    private String guid;
}
