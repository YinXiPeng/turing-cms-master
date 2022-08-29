package com.bonc.turing.cms.exercise.domain;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "cos_section")
public class CosSection {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid",strategy="uuid")
    private String id;

    @Column(name = "chapter_id")
    private Long chapterId;

    @Column(name = "name")
    private String name;

    @Column(name = "sort")
    private Integer sort;

    @Column(name = "section_type")
    private CosSectionType sectionType;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "total_number")
    private int totalNumber;

    //0：免费 1：付费
    @Column(name = "type")
    private Integer type;

    @Column(name = "create_time")
    private Date createTime;
}
