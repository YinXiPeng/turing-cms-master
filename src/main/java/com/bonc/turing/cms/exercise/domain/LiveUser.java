package com.bonc.turing.cms.exercise.domain;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * description:直播用户持久化映射
 *
 * @author lxh
 * @date 2020/4/20 16:13
 */
@Data
@Entity
@Table(name = "live_user")
public class LiveUser {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "live_user_id")
    private String liveUserId;

    /**
     * turing用户guid
     */
    @Column(name = "guid")
    private String guid;

    /**
     * 姓名
     */
    @Column(name = "name")
    private String name;

    /**
     * 公司
     */
    @Column(name = "company")
    private String company;

    /**
     * 手机号
     */
    @Column(name = "phone")
    private String phone;

    /**
     * 邮箱
     */
    @Column(name = "email")
    private String email;

    /**
     * 行业
     */
    @Column(name = "industry")
    private String industry;

    /**
     * 职位
     */
    @Column(name = "position")
    private String position;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Long createTime;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    private Long updateTime;


}
