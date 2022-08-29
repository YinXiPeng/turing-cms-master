package com.bonc.turing.cms.exercise.controller;

import com.bonc.turing.cms.exercise.dto.VideoCourseChapterDTO;
import com.bonc.turing.cms.exercise.service.VideoCourseService;
import com.bonc.turing.cms.exercise.vo.VideoCourseChapterAddVO;
import com.bonc.turing.cms.manage.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RequestMapping("/videoCourse")
@RestController
@Slf4j
public class VideoCourseController {

    @Autowired
    private VideoCourseService videoCourseService;

    /**
     * 添加或修改章节内容
     *
     * @param videoCourseChapterAddVO
     * @return
     * @throws Exception
     */
    @PostMapping("chapter/addOrUpdate")
    public Object addOrUpdateChapter(@RequestBody VideoCourseChapterAddVO videoCourseChapterAddVO) throws Exception {
        try {
            if (CollectionUtils.isEmpty(videoCourseChapterAddVO.getChapters()) || Objects.isNull(videoCourseChapterAddVO.getTextBookId())) {
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "参数错误"));
            }
            videoCourseService.addOrUpdateChapter(videoCourseChapterAddVO);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, "", "添加视频课程章节成功"));
        } catch (Exception e) {
            log.error("editRemarks is error", e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "添加视频课程章节失败"));
        }
    }

    /**
     * 根据课程ID查询所有章节
     *
     * @param textBookId
     * @return
     * @throws Exception
     */
    @GetMapping("chapter/findByTextBookId")
    public Object findChapterByTextBookId(@RequestParam String textBookId) throws Exception {
        try {
            if (Objects.isNull(textBookId)) {
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "参数错误"));
            }
            List<VideoCourseChapterDTO> videoCourseChapterDTOList = videoCourseService.findChapterByTextBookId(textBookId);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, videoCourseChapterDTOList, "查询视频课程章节成功"));
        } catch (Exception e) {
            log.error("editRemarks is error", e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "查询视频课程章节失败"));
        }
    }


}
