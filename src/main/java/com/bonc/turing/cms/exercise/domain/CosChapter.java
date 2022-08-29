package com.bonc.turing.cms.exercise.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "cos_chapter")
public class CosChapter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sort")
    private Integer sort;

    @Column(name = "course_id")
    private String courseId;

    @Column(name = "chapter_name")
    private String chapterName;

    @Column(name = "introduction")
    private String introduction;

    @Column(name = "create_time")
    private Date createTime;
}
