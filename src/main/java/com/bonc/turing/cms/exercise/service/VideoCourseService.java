package com.bonc.turing.cms.exercise.service;

import com.bonc.turing.cms.exercise.dto.VideoCourseChapterDTO;
import com.bonc.turing.cms.exercise.vo.VideoCourseChapterAddVO;

import java.util.List;

public interface VideoCourseService {
    /**
     * 添加视频课程章节
     *
     * @param videoCourseChapterAddVO
     */
    void addOrUpdateChapter(VideoCourseChapterAddVO videoCourseChapterAddVO);

    /**
     * 查询视频课程章节
     *
     * @param textBookId
     * @return
     */
    List<VideoCourseChapterDTO> findChapterByTextBookId(String textBookId);
}
