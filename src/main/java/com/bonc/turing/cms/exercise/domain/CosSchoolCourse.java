package com.bonc.turing.cms.exercise.domain;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "Cos_school_course")
public class CosSchoolCourse {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid",strategy="uuid")
    private String id; //主键id

    //课程id
    private String courseId;

    //镜像
    private String image;

    //学校id
    private String schoolId;

    //限制cpu核数
    private Long cpu;

    //限制内存大小
    private Long memory;

    //壁纸路径
    private String wallPaperPath;

    //1显示2隐藏
    private int state;


}
