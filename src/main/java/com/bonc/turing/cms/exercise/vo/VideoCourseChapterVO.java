package com.bonc.turing.cms.exercise.vo;

import lombok.Data;

import java.util.List;

@Data
public class VideoCourseChapterVO {

    private String chapterName;

    private String videoUrl;

    private String videoName;

    private Integer videoTime;

    /**
     * 章节类别（0免费 1付费）
     */
    private Integer type;

    private List<VideoCourseChapterVO> children;

}
