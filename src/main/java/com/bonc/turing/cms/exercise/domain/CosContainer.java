package com.bonc.turing.cms.exercise.domain;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "cos_container")
public class CosContainer {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid",strategy="uuid")
    private String id;

    //docker宿主机ip
    private String ip;

    //端口
    private String port;

    //容器id
    private String containerId;

    //实训课id
    private String schoolCourseId;

    //教师id
    private String teacherId;

    //用户id
    private String guid;

    //1运行中2挂起
    private int state;

    //附件路径
    private String filePath;

    //附件名称
    private String fileName;

    //附件上传时间
    private long fileUploadTime;

    //得分
    private Integer score;

    private long createTime = System.currentTimeMillis();

    private long updateTime = System.currentTimeMillis();
}
