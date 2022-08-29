package com.bonc.turing.cms.exercise.service.impl;

import com.bonc.turing.cms.exercise.dao.repository.QtTextBookRepository;
import com.bonc.turing.cms.exercise.dao.repository.QtVideoCourseChapterRepository;
import com.bonc.turing.cms.exercise.domain.QtTextBook;
import com.bonc.turing.cms.exercise.domain.QtVideoCourseChapter;
import com.bonc.turing.cms.exercise.dto.VideoCourseChapterDTO;
import com.bonc.turing.cms.exercise.service.VideoCourseService;
import com.bonc.turing.cms.exercise.vo.VideoCourseChapterAddVO;
import com.bonc.turing.cms.exercise.vo.VideoCourseChapterVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class VideoCourseServiceImpl implements VideoCourseService {

    @Autowired
    private QtVideoCourseChapterRepository qtVideoCourseChapterRepository;

    @Autowired
    private QtTextBookRepository qtTextBookRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addOrUpdateChapter(VideoCourseChapterAddVO videoCourseChapterAddVO) {
        List<VideoCourseChapterVO> chapters = videoCourseChapterAddVO.getChapters();
        String textBookId = videoCourseChapterAddVO.getTextBookId();
        double price = videoCourseChapterAddVO.getPrice(); //免费时填0
        List<QtVideoCourseChapter> qtVideoCourseChapterList = qtVideoCourseChapterRepository.findByTextBookId(textBookId);

        if (CollectionUtils.isNotEmpty(qtVideoCourseChapterList)) {
            log.info("课程章节已存在，修改/删除===textBookId{}", textBookId);
            qtVideoCourseChapterRepository.deleteByTextBookId(textBookId);
        }
        int index = 1;
        for (VideoCourseChapterVO chapter : chapters) {
            Date nowDate = new Date();
            QtVideoCourseChapter qtVideoCourseChapter = new QtVideoCourseChapter();
            qtVideoCourseChapter.setChapterName(chapter.getChapterName());
            qtVideoCourseChapter.setTextBookId(textBookId);
            qtVideoCourseChapter.setCreateTime(nowDate);
            qtVideoCourseChapter.setSort(index++);
            //保存章内容
            QtVideoCourseChapter saveDone = qtVideoCourseChapterRepository.save(qtVideoCourseChapter);
            //获取章对应的节内容
            List<VideoCourseChapterVO> children = chapter.getChildren();
            if (CollectionUtils.isNotEmpty(children)) {
                for (VideoCourseChapterVO child : children) {
                    QtVideoCourseChapter qtVideoCourseChapterChild = new QtVideoCourseChapter();
                    qtVideoCourseChapterChild.setChapterName(child.getChapterName());
                    qtVideoCourseChapterChild.setTextBookId(textBookId);
                    qtVideoCourseChapterChild.setType(child.getType());
                    qtVideoCourseChapterChild.setVideoUrl(child.getVideoUrl());
                    qtVideoCourseChapterChild.setVideoName(child.getVideoName());
                    qtVideoCourseChapterChild.setVideoTime(child.getVideoTime());
                    qtVideoCourseChapterChild.setParentId(saveDone.getId());
                    qtVideoCourseChapterChild.setCreateTime(nowDate);
                    qtVideoCourseChapterChild.setSort(index++);
                    //保存节内容
                    qtVideoCourseChapterRepository.save(qtVideoCourseChapterChild);
                }
            }
        }
        Optional<QtTextBook> textBook = qtTextBookRepository.findById(textBookId);
        if (textBook.isPresent()) { //有付费章节时必填价格,且>0
            if (price > 0) {
                textBook.get().setPrice(price);
                qtTextBookRepository.save(textBook.get());
            }
        }

    }

    @Override
    public List<VideoCourseChapterDTO> findChapterByTextBookId(String textBookId) {
        //获取所有的章节内容
        List<QtVideoCourseChapter> byCourseId = qtVideoCourseChapterRepository.findByTextBookId(textBookId);

        if (CollectionUtils.isNotEmpty(byCourseId)) {
            //处理章节内容，返回具有数据结构的章节内容
            //获取所有的章内容
            List<QtVideoCourseChapter> collect = byCourseId.stream().filter(qtVideoCourseChapter -> Objects.isNull(qtVideoCourseChapter.getParentId()))
                    .collect(Collectors.toList());
            List<VideoCourseChapterDTO> videoCourseChapterDTOList = new ArrayList<>();
            collect.forEach(parent -> {
                //获取章对应的节内容
                List<VideoCourseChapterDTO> children = byCourseId.stream().filter(qtVideoCourseChapter -> Objects.nonNull(qtVideoCourseChapter.getParentId()) && qtVideoCourseChapter.getParentId().equals(parent.getId()))
                        .map(qtVideoCourseChapter -> VideoCourseChapterDTO.builder().id(qtVideoCourseChapter.getId())
                                .chapterName(qtVideoCourseChapter.getChapterName())
                                .type(qtVideoCourseChapter.getType())
                                .videoUrl(qtVideoCourseChapter.getVideoUrl())
                                .videoName(qtVideoCourseChapter.getVideoName())
                                .videoTime(qtVideoCourseChapter.getVideoTime())
                                .textBookId(qtVideoCourseChapter.getTextBookId())
                                .sort(qtVideoCourseChapter.getSort())
                                .build()).collect(Collectors.toList());

                VideoCourseChapterDTO build = VideoCourseChapterDTO.builder().id(parent.getId())
                        .textBookId(parent.getTextBookId())
                        .chapterName(parent.getChapterName())
                        .sort(parent.getSort())
                        .children(children)
                        .build();
                videoCourseChapterDTOList.add(build);
            });
            return videoCourseChapterDTOList;
        }
        return null;
    }
}
