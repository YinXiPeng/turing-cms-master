package com.bonc.turing.cms.exercise.domain;

import lombok.Data;

import javax.persistence.*;

/**
 * description:直播用户持久化映射
 *
 * @author lxh
 * @date 2020/4/20 16:13
 */
@Data
@Entity
@Table(name = "live_watch_record")
public class LiveWatchRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 房间id
     */
    @Column(name = "room_id")
    private String roomId;

    /**
     * 开始时间
     */
    @Column(name = "begin_time")
    private Long beginTime;

    /**
     * 关闭结束时间
     */
    @Column(name = "end_time")
    private Long endTime;

    /**
     * 直播用户关联ID
     */
    @Column(name = "live_user_id")
    private String liveUserId;

    /**
     * 一次记录总时长/秒
     */
    @Column(name = "total")
    private Long total;


}
