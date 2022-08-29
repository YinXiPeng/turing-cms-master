package com.bonc.turing.cms.exercise.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * description:
 *
 * @author lxh
 * @date 2020/4/2 10:23
 */
@Data
public class HrUserInfoListDTO {

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 用户guid
     */
    private String guid;

    /**
     * 用户状态
     */
    private Integer status;

    /**
     * 公司表类型
     */
    @JsonIgnore
    private Integer type;

    /**
     * 公司表规模
     */
    @JsonIgnore
    private Integer size;

    /**
     * 公司标签
     */
    private Object companyTags;

    /**
     * 联系人
     */
    private String contactName;

    /**
     * 联系人手机号
     */
    private String phone;

    /**
     * 用户名
     */
    private String username;

    /**
     * 注册手机号
     */
    private String registerPhone;

    /**
     * 公司营业执照
     */
    private String companyLicense;

    /**
     * 注册时间
     */
    private Long registerTime;

    /**
     * 已使用天数
     */
    private Long useDays;

    /**
     * 是否上传营业执照
     */
    private Boolean isUploadLicense;

    /**
     * 公司类型
     */
    private String companyType;

    /**
     * 公司规模
     */
    private String companySize;

    /**
     * 公司ID
     */
    private String companyId;

}
