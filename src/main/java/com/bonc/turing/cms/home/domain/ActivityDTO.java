package com.bonc.turing.cms.home.domain;

import lombok.Data;


@Data
public class ActivityDTO {

    private String id;

    private String title; //活动标题

    private String contentUrl; //内容链接

    private String location; //活动地点

    private Integer status; //活动状态 0：下线 1：上线

    private Long time; //活动举办时间

}
