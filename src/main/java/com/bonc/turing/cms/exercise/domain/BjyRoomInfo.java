package com.bonc.turing.cms.exercise.domain;

import lombok.Data;

import javax.persistence.*;

/**
 * @author lxm
 */
@Data
@Entity
@Table(name = "bjy_room_info")
public class BjyRoomInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 房间id
     */
    private String roomId;

    /**
     * 课程名称
     */
    private String title;

    /**
     * 创建时间
     */
    private Long createTime;
    /**
     * 直播开始时间
     */
    private Long startTime;
    /**
     * 直播结束时间
     */
    private Long endTime;
    /**
     * 课程时间
     */
    private Long duration;
    /**
     * 创建人
     */
    private String guid;

    /**
     * 直播URL
     */
    @Column(name = "room_url")
    private String roomUrl;

    /**
     * 是否需要登录0否 1是
     */
    @Column(name = "need_login")
    private Integer needLogin;

    /**
     * 是否需要填写个人信息0否 1是
     */
    @Column(name = "need_fill_info")
    private Integer needFillInfo;
    /**
     * 直播类型 1 腾讯 2 janus
     */
    private Integer type;
    /**
     * 回放地址
     */
    private String recordUrl;
    /**
     * 白板id
     */
    private String whiteboardId;
}
