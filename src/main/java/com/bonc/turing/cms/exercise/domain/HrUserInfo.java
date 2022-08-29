package com.bonc.turing.cms.exercise.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * description:
 *
 * @author lxh
 * @date 2020/4/2 10:02
 */
@Data
@Entity
@Table(name = "hr_user_info")
public class HrUserInfo {

    @Id
    private String guid;

    private String username;

    private String password;

    private String salt;

    private String phone;

    private String header;

    private String email;

    @Column(name = "register_time")
    private Long registerTime;

    @Column(name = "modify_time")
    private Long modifyTime;

    @Column(name = "company_id")
    private String companyId;

    @Column(name = "novice_guidance_steps")
    private Integer noviceGuidanceSteps;

    @Column(name = "contact_name")
    private String contactName;

    private Integer status;

    @Column(name = "company_license")
    private String companyLicense;

    @Column(name = "open_id")
    private String openId;

    @Column(name = "register_phone")
    private String registerPhone;

}
