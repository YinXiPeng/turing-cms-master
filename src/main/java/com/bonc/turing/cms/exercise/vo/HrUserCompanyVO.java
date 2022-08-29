package com.bonc.turing.cms.exercise.vo;

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
public class HrUserCompanyVO {

    /**
     * 公司ID
     */
    @NotBlank(message = "公司ID不能为空")
    private String companyId;
    /**
     * 公司名称
     */
    @NotBlank(message = "公司名称不能为空")
    private String companyName;

    /**
     * 公司规模
     */
    @NotNull(message = "公司规模不能为空")
    private Integer companySize;

    /**
     * 公司类型
     */
    @NotNull(message = "公司类型不能为空")
    private Integer companyType;

    /**
     * 公司简介
     */
    @NotBlank(message = "公司简介不能为空")
    private String companyIntroduction;

    /**
     * 公司标签
     */
    @NotBlank(message = "公司标签不能为空")
    private String companyTags;
    /**
     * logo
     */
    @NotBlank(message = "公司Logo不能为空")
    private String logo;

    /**
     * 公司营业执照
     */
    private String companyLicense;

    /**
     * 联系人
     */
    @NotBlank(message = "联系人不能为空")
    private String contactName;

    /**
     * 联系人手机号
     */
    @NotBlank(message = "联系人手机号不能为空")
    private String contactPhone;

    /**
     * 联系人email
     */
    @NotBlank(message = "Email不能为空")
    @Email(message = "Email格式不正确")
    private String contactEmail;

    /**
     * 账号状态
     */
    private Integer status;

    /**
     * 用户guid
     */
    @NotBlank(message = "用户guid不能为空")
    private String guid;
}
