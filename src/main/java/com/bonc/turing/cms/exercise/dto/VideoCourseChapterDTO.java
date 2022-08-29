package com.bonc.turing.cms.exercise.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class VideoCourseChapterDTO {

    private Long id;

    private String chapterName;

    private String videoUrl;

    private String videoName;

    private Integer videoTime;

    private String textBookId;

    private Integer sort;

    /**
     * 章节类别（0免费 1付费）
     */
    private Integer type;

    private List<VideoCourseChapterDTO> children;
}
