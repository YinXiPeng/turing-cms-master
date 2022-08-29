package com.bonc.turing.cms.exercise.domain;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * @desc 用户(教师或学生)-课程
 * @author yh
 * @date 2019.12.25
 */
@Entity
@Data
public class QtUserAndTextBook {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid",strategy="uuid")
    private String id; //主键id
    private String guid; //用户guid
    private String textBookId;//教材id
    private int role; //用户角色（0:导师 1:学生）
    private int num; //已学习页数 默认1
    private Date createTime; //创建时间
    private Date updateTime; //修改时间
    private Long memoryTime; //记忆时间 可以需要频繁更新改字段
    private Integer type; // 0为教材 1为视频 2为直播课
}
