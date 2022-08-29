package com.bonc.turing.cms.exercise.dto;

import lombok.Data;

/**
 * description:
 *
 * @author lxh
 * @date 2020/4/20 20:25
 */
@Data
public class BjLiveUseTimeDTO {

    /**
     * 编号
     */
    private String number;

    /**
     * 直播用户ID
     */
    private String liveUserId;

    /**
     * 用户名
     */
    private String name;

    /**
     * turing昵称
     */
    private String nickname;

    /**
     * 用户访问ip
     */
    private String ip;

    /**
     * 公司
     */
    private String company;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 行业
     */
    private String industry;

    /**
     * 职位
     */
    private String position;

    /**
     * turing用户guid
     */
    private String guid;

    /**
     * 观看时长/秒
     */
    private Long watchTime;

}
