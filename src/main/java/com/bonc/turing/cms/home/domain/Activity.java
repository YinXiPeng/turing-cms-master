package com.bonc.turing.cms.home.domain;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
public class Activity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid",strategy = "uuid")
    private String id; //主键id

    private String title; //活动标题

    private String contentUrl; //内容链接

    private String location; //活动地点

    private Integer status; //活动状态 0：下线 1：上线

    private Date time; //活动举办时间

    private Date createTime; //创建时间

}
