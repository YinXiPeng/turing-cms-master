package com.bonc.turing.cms.exercise.vo;

import lombok.Data;

/**
 * description:
 *
 * @author lxh
 * @date 2020/4/2 10:17
 */
@Data
public class SearchHrUserInfoVO {

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 用户名
     */
    private String username;

    /**
     * 手机号
     */
    private String phone;

    /**
     * true通多校验  false待校验
     */
    private Boolean isPassVerified;

    /**
     * 当前页
     */
    private Long page;

    /**
     * 每页显示记录数
     */
    private Integer size;

}
