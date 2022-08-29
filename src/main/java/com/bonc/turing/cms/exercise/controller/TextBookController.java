package com.bonc.turing.cms.exercise.controller;

import com.alibaba.fastjson.JSONObject;
import com.bonc.turing.cms.exercise.dao.repository.QtTextBookRepository;
import com.bonc.turing.cms.exercise.domain.QtTextBook;
import com.bonc.turing.cms.exercise.service.TextBookService;
import com.bonc.turing.cms.manage.ResultEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

/**
 * @author yh
 * @desc 教材相关接口
 * @date 2019.12.26
 */
@RestController
@RequestMapping("/textBook")
public class TextBookController {
    private static Logger logger = LoggerFactory.getLogger(TextBookController.class);
    @Autowired
    private TextBookService textBookService;
    @Autowired
    private QtTextBookRepository qtTextBookRepository;

    /**
     * 创建或编辑教材
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "createOrEditTextBook", method = RequestMethod.POST)
    Object createOrEditTextBook(@RequestBody JSONObject params, HttpServletRequest request) {
        try {
            QtTextBook qtTextBook = JSONObject.parseObject(params.getJSONObject("textBook").toJSONString(), QtTextBook.class);
            if (("".equals(qtTextBook.getId()) || null == qtTextBook.getId())&&null != qtTextBookRepository.findByBookNameAndIsDeleted(qtTextBook.getBookName(), 0)) { //新建不能重名
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "该课程名已存在，不能重复"));
            }
            Object o = textBookService.createOrEditTextBook(params, request);
            if (o.equals("failure")) {
                logger.error("createOrEditTextBook failure");
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE,"","创建或编辑课程失败"));
            } else {
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, o, "创建或编辑课程成功"));
            }
        }
        catch (Exception e){
            e.printStackTrace();
            logger.error("createOrEditTextBook exception{}",e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE));
        }
    }

    /**
     * 获取教材信息
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "showBackStageTextBook", method = RequestMethod.GET)
    Object showBackStageTextBook(HttpServletRequest request) {
        Object o = textBookService.showBackStageTextBook(request);
        if (o.equals("failure")) {
            logger.error("showBackStageTextBook failure");
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE));
        } else {
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, o, "获取课程成功"));
        }
    }

    /**
     * 上传或编辑章节
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "createOrEditChapter", method = RequestMethod.POST)
    Object createOrEditChapter(@RequestBody JSONObject params) {
        Object o = textBookService.createOrEditChapter(params);
        if (o.equals("failure")) {
            logger.error("createOrEditChapter failure");
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE));
        } else {
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, "", "上传或编辑章节成功"));
        }
    }

    /**
     * 获取章节信息
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "showBackStageChapter", method = RequestMethod.GET)
    Object showBackStageChapter(HttpServletRequest request) {
        Object o = textBookService.showBackStageChapter(request);
        if (o.equals("failure")) {
            logger.error("showBackStageChapter failure");
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE));
        } else {
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, o, "获取章节成功"));
        }
    }

    /**
     * 后台-获取教材列表
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "showBackStageTextBookList", method = RequestMethod.POST)
    Object showBackStageTextBookList(@RequestBody JSONObject params) {
        Object o = textBookService.showBackStageTextBookList(params);
        if (o.equals("failure")) {
            logger.error("showBackStageTextBookList failure");
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE));
        } else {
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, o, "获取列表成功"));
        }
    }

    /**
     * 后台-获取视频列表
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "showVideoCourseList", method = RequestMethod.POST)
    Object showVideoCourseList(@RequestBody JSONObject params) {
        Object o = textBookService.showVideoCourseList(params);
        if (o.equals("failure")) {
            logger.error("showVideoCourseList failure");
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE));
        } else {
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, o, "获取视频课程列表成功"));
        }
    }

    /**
     * 设置教材价格
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "setTextBookPrice", method = RequestMethod.POST)
    Object setTextBookPrice(@RequestBody JSONObject params) {
        Object o = textBookService.setTextBookPrice(params);
        if (o.equals("failure")) {
            logger.error("setTextBookPrice failure");
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE));
        } else {
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, "", "设置价格成功"));
        }
    }

    /**
     * 改变教材状态
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "changeBookStatus", method = RequestMethod.POST)
    Object changeBookStatus(@RequestBody JSONObject params) {
        Object o = textBookService.changeBookStatus(params);
        if (o.equals("failure")) {
            logger.error("changeBookStatus failure");
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE));
        } else {
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, "", "改变教材状态成功"));
        }
    }

    /**
     * 获取教师列表
     *
     * @return
     */
    @RequestMapping(value = "getTeacherList", method = RequestMethod.GET)
    Object getTeacherList() {
        try {
            Object o = textBookService.getTeacherList();
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, o, "获取教师列表成功"));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("getTeacherList exception{}", e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE));
        }
    }

    /**
     * 获取章节总页数
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "chapterTotalNum", method = RequestMethod.GET)
    Object chapterTotalNum(HttpServletRequest request) {
        Object o = textBookService.chapterTotalNum(request);
        if (o.equals("failure")) {
            logger.error("chapterTotalNum failure");
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE));
        } else {
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, o, "获取章节总页数成功"));
        }
    }

    /**
     * 后台-检查教材名是否重复
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "checkDuplicate", method = RequestMethod.GET)
    Object checkDuplicate(HttpServletRequest request) {
        try {
            Object o = textBookService.checkDuplicate(request);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, o, "检查教材名是否重复成功"));
        } catch (Exception e) {
            logger.error("checkDuplicate failure");
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE));
        }
    }
    /**
     * 教师讲解范围-教材下拉框列表
     * @return
     */
    @RequestMapping(value = "bookList", method = RequestMethod.GET)
    Object bookList() {
        Object o = textBookService.bookList();
        if (null==o) {
            logger.error("bookList failure");
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE));
        } else {
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, o, "获取教材下拉列表成功"));
        }
    }


}
