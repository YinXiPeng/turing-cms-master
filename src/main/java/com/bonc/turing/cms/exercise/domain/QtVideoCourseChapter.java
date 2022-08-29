package com.bonc.turing.cms.exercise.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "qt_video_course_chapter")
public class QtVideoCourseChapter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chapter_name")
    private String chapterName;

    @Column(name = "qt_text_book_id")
    private String textBookId;

    @Column(name = "video_url")
    private String videoUrl;

    @Column(name = "video_name")
    private String videoName;

    @Column(name = "video_time")
    private Integer videoTime;

    @Column(name = "sort")
    private Integer sort;

    /**
     * 章节类别（0免费 1付费）
     */
    private Integer type;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "create_time")
    private Date createTime;

}
