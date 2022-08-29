package com.bonc.turing.cms.exercise.vo;

import lombok.Data;

import java.util.List;

@Data
public class VideoCourseChapterAddVO {

  private String textBookId;

  private double price;

  private List<VideoCourseChapterVO> chapters;

}
