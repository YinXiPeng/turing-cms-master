package com.bonc.turing.cms.exercise.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bonc.turing.cms.exercise.dao.mapper.QtTeacherMapper;
import com.bonc.turing.cms.exercise.dao.mapper.QtTextBookMapper;
import com.bonc.turing.cms.exercise.dao.repository.*;
import com.bonc.turing.cms.exercise.domain.*;
import com.bonc.turing.cms.exercise.dto.BackStageTextBookDTO;
import com.bonc.turing.cms.exercise.dto.VideoCourseListDTO;
import com.bonc.turing.cms.exercise.service.TextBookService;
import com.bonc.turing.cms.manage.entity.user.SysUserInfo;
import com.bonc.turing.cms.manage.repository.SysUserInfoRepository;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itextpdf.text.pdf.PdfReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * @author yh
 * @desc 教材相关
 * @date 2019.12.26
 */
@Service
public class TextBookServiceImpl implements TextBookService {

    private static Logger logger = LoggerFactory.getLogger(TextBookService.class);

    @Autowired
    private QtTextBookRepository qtTextBookRepository;

    @Autowired
    private QtUserAndTextBookRepository qtUserAndTextBookTRepository;

    @Autowired
    private QtChapterRepository qtChapterRepository;

    @Autowired
    private QtTextBookBindingRepository qtTextBookBindingRepository;

    @Autowired
    private SysUserInfoRepository sysUserInfoRepository;

    @Autowired
    private QtTeacherRepository qtTeacherRepository;

    @Autowired
    private QtTextBookPagingRepository qtTextBookPagingRepository;

    @Autowired
    private QtVideoCourseChapterRepository qtVideoCourseChapterRepository;

    @Resource
    private QtTextBookMapper qtTextBookMapper;


    @Autowired
    private QtAnswerRecordRepository qtAnswerRecordRepository;

    @Resource
    private QtTeacherMapper qtTeacherMapper;

    /**
     * @param params
     * @return
     * @desc 创建或编辑课程
     */
    @Transactional
    @Override
    public Object createOrEditTextBook(JSONObject params, HttpServletRequest request) {
        try {
            JSONObject textBook = params.getJSONObject("textBook");
            int type = params.getInteger("type");
            String guid = request.getParameter("guid");
            String jsonStr = textBook.toJSONString();
            JSONObject result = new JSONObject();
            QtTextBook qtTextBook = JSONObject.parseObject(jsonStr, QtTextBook.class);
            if ("".equals(qtTextBook.getId()) || null == qtTextBook.getId()) { //如果是新建
                qtTextBook.setCreateTime(new Date());
                qtTextBook.setCreateById(guid);
                qtTextBook.setIsDeleted(0);
                qtTextBook.setPrice(0);
                qtTextBook.setStatus(0);
                qtTextBook.setDiscount(0);
                qtTextBook.setType(type);
                qtTextBook.setRandomNum(new Random().nextInt(51) + 150);
            } else { //如果是编辑
                Optional<QtTextBook> oldTextBook = qtTextBookRepository.findById(qtTextBook.getId());
                if (oldTextBook.isPresent()) {
                    qtTextBook.setCreateTime(oldTextBook.get().getCreateTime());
                    qtTextBook.setCreateById(oldTextBook.get().getCreateById());
                    qtTextBook.setIsDeleted(oldTextBook.get().getIsDeleted());
                    qtTextBook.setPrice(oldTextBook.get().getPrice());
                    qtTextBook.setStatus(oldTextBook.get().getStatus());
                    qtTextBook.setDiscount(oldTextBook.get().getDiscount());
                    qtTextBook.setType(type);
                    qtTextBook.setRandomNum(oldTextBook.get().getRandomNum());
                }
            }
            QtTextBook save = qtTextBookRepository.save(qtTextBook);
            if (null == save) {
                return "failure";
            } else {
                if (0 == type) { //是教材课程
                    JSONArray teacherIdList = params.getJSONArray("teacherIdList");
                    qtUserAndTextBookTRepository.deleteAllByRoleAndTextBookId(0, save.getId());//删除旧的导师
                    if (teacherIdList.size() > 0) {
                        for (int i = 0; i < teacherIdList.size(); i++) {
                            QtUserAndTextBook qtUserAndTextBook = new QtUserAndTextBook();
                            if (null != qtUserAndTextBookTRepository.findByTextBookIdAndGuidAndRole(save.getId(), teacherIdList.getString(i), 1)) {   //如果添加为老师之前是学生，则更改身份为老师
                                qtUserAndTextBook = qtUserAndTextBookTRepository.findByTextBookIdAndGuidAndRole(save.getId(), teacherIdList.getString(i), 1);
                                qtUserAndTextBook.setRole(0); //设置为导师
                                qtUserAndTextBook.setUpdateTime(new Date());
                            } else {
                                qtUserAndTextBook.setGuid(teacherIdList.getString(i));
                                qtUserAndTextBook.setNum(0);
                                qtUserAndTextBook.setCreateTime(new Date());
                                qtUserAndTextBook.setUpdateTime(new Date());
                                qtUserAndTextBook.setTextBookId(save.getId());
                                qtUserAndTextBook.setType(0);
                            }
                            qtUserAndTextBookTRepository.save(qtUserAndTextBook);
                        }
                    }
                }
                result.put("textBookId", save.getId()); //成功返回课程id
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("createOrEditTextBook exception{}" + e.getMessage());
            return "failure";
        }
    }

    /**
     * @param params
     * @return
     * @desc 上传或编辑章节
     */
    @Transactional
    @Override
    public Object createOrEditChapter(JSONObject params) {
        try {
            JSONArray chapterArray = params.getJSONArray("chapterArray");
            JSONArray deleteIdArray = params.getJSONArray("deleteIdArray"); //需要删除的章节id
            String textBookId = params.getString("textBookId");
            double price = params.getDouble("price");//免费时传0
            if (deleteIdArray.size() > 0) {
                for (int i = 0; i < deleteIdArray.size(); i++) {
                    qtChapterRepository.deleteById(deleteIdArray.getString(i));
                    List<QtTextBookPaging> byChapterId = qtTextBookPagingRepository.findByChapterId(deleteIdArray.getString(i));
                    for (QtTextBookPaging qtTextBookPaging : byChapterId) {
                        qtTextBookBindingRepository.deleteByPagingId(qtTextBookPaging.getId()); //删除与章节关联的题
                    }
                    qtTextBookPagingRepository.deleteByChapterId(deleteIdArray.getString(i)); //删除章节
                    qtAnswerRecordRepository.deleteAllByChapterId(deleteIdArray.getString(i)); //删除与章节关联的答题记录
                }
            }
            if (chapterArray.size() > 0) {
                for (int i = 0; i < chapterArray.size(); i++) {
                    String jsonStr = chapterArray.getString(i);
                    QtChapter chapter = JSONObject.parseObject(jsonStr, QtChapter.class);
                    if ("".equals(chapter.getId()) || null == chapter.getId()) { //新建
                        chapter.setCreateTime(new Date());
                    } else {//编辑
                        Optional<QtChapter> oldChapter = qtChapterRepository.findById(chapter.getId());
                        if (oldChapter.isPresent()) {
                            chapter.setCreateTime(oldChapter.get().getCreateTime());
                        }
                    }
                    qtChapterRepository.saveAndFlush(chapter);
                }
                QtChapter qtChapter = JSONObject.parseObject(chapterArray.getString(0), QtChapter.class);
                if (1 == getPayType(qtChapter.getTextBookId())) { //如果教材的付费类型为全免费，将价格置0
                    Optional<QtTextBook> textBook = qtTextBookRepository.findById(qtChapter.getTextBookId());
                    if (textBook.isPresent()) {
                        textBook.get().setPrice(0);
                        qtTextBookRepository.save(textBook.get());
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
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("createOrEditChapter exception{}" + e.getMessage());
            return "failure";
        }
    }

    /**
     * @return
     * @desc 后台-教材列表
     */
    @Override
    public Object showBackStageTextBookList(JSONObject params) {
        PageHelper.startPage(params.getInteger("pageNum"), params.getInteger("pageSize"));
        List<BackStageTextBookDTO> textBookDTOList = qtTextBookMapper.getBackStageTextBookList();
        textBookDTOList.stream().filter(Objects::nonNull).forEach(book -> book.setPayType(getPayType(book.getId())));
        PageInfo<BackStageTextBookDTO> pageInfo = new PageInfo<>(textBookDTOList);
        return pageInfo;
    }

    /**
     * @param params
     * @return
     * @desc 后台-视频课程列表
     */
    @Override
    public Object showVideoCourseList(JSONObject params) {
        PageHelper.startPage(params.getInteger("pageNum"), params.getInteger("pageSize"));
        List<VideoCourseListDTO> videoCourseList = qtTextBookMapper.getVideoCourseList();
        videoCourseList.stream().filter(Objects::nonNull).forEach(course -> course.setPayType(getPayType(course.getId())));
        PageInfo<VideoCourseListDTO> pageInfo = new PageInfo<>(videoCourseList);
        return pageInfo;
    }

    /**
     * @param textBookId
     * @return
     * @desc 获取课程的付费类型
     */
    public int getPayType(String textBookId) {
        int payType = 0;
        Optional<QtTextBook> book = qtTextBookRepository.findById(textBookId);
        int freeSize = 0; //免费章节数
        int paySize = 0; //付费章节数
        if (book.isPresent() && book.get().getType() == 0) { //教材课程
            List<QtChapter> freeChapterList = qtChapterRepository.findAllByTextBookIdAndTypeOrderBySort(textBookId, 0);
            List<QtChapter> payChapterList = qtChapterRepository.findAllByTextBookIdAndTypeOrderBySort(textBookId, 1);
            freeSize = freeChapterList.size();
            paySize = payChapterList.size();
        } else if (book.isPresent() && book.get().getType() == 1) { //视频课程
            List<QtVideoCourseChapter> freeChapterList = qtVideoCourseChapterRepository.findAllByTextBookIdAndType(textBookId, 0);
            List<QtVideoCourseChapter> payChapterList = qtVideoCourseChapterRepository.findAllByTextBookIdAndType(textBookId, 1);
            freeSize = freeChapterList.size();
            paySize = payChapterList.size();
        }
        if (freeSize > 0 && paySize > 0) {//部分免费
            payType = 2;
        } else if (freeSize > 0) { //全部免费
            payType = 1;
        } else if (paySize > 0) { //全部收费
            payType = 3;
        }
        return payType;
    }

    /**
     * @param request
     * @return
     * @desc 后台-返显教材信息
     */
    @Override
    public Object showBackStageTextBook(HttpServletRequest request) {
        String textBookId = request.getParameter("textBookId");
        Optional<QtTextBook> textBook = qtTextBookRepository.findById(textBookId);
        List<Map<String, String>> teacherList = new ArrayList<>();
        JSONObject result = new JSONObject();
        if (!textBook.isPresent()) {
            return "failure";
        }
        if (textBook.get().getType() == 0) {//是教材课程，返回老师列表，否则不返回老师列表
            List<QtUserAndTextBook> userAndTextBookList = qtUserAndTextBookTRepository.findAllByTextBookIdAndRole(textBookId, 0);
            for (int i = 0; i < userAndTextBookList.size(); i++) {
                Map<String, String> teacher = new HashMap<>();
                String guid = userAndTextBookList.get(i).getGuid();
                teacher.put("guid", guid);
                Optional<SysUserInfo> sysUserInfo = sysUserInfoRepository.findById(guid);
                if (!sysUserInfo.isPresent()) {
                    return "failure";
                } else {
                    teacher.put("name", sysUserInfo.get().getUsername());
                }
                teacherList.add(teacher);
            }
            result.put("teacherList", teacherList);
        }
        result.put("textBook", textBook);
        return result;
    }

    /**
     * @param request
     * @return
     * @desc 后台-返显章节信息
     */
    @Override
    public Object showBackStageChapter(HttpServletRequest request) {
        String textBookId = request.getParameter("textBookId");
        List<QtChapter> chapterList = qtChapterRepository.findAllByTextBookId(textBookId);
        JSONObject result = new JSONObject();
        if (chapterList.size() <= 0) {
            return "failure";
        }
        result.put("chapterList", chapterList);
        return result;
    }

    /**
     * @param params
     * @return
     * @desc 设置教材价格
     */
    @Transactional
    @Override
    public Object setTextBookPrice(JSONObject params) {
        try {
            double price = params.getDouble("price");
            String textBookId = params.getString("textBookId");
            Optional<QtTextBook> textBook = qtTextBookRepository.findById(textBookId);
            if (textBook.isPresent()) {
                textBook.get().setPrice(new BigDecimal(price).setScale(2, RoundingMode.HALF_UP).doubleValue());
                if (price <= 0) { //价格为免费时，将课程对应的章节改为免费
                    if (0 == textBook.get().getType()) { //为教材课程
                        List<QtChapter> chapterList = qtChapterRepository.findAllByTextBookId(textBookId);
                        chapterList.stream().filter(chapter -> chapter.getType() == 1).forEach(chapter -> {  //将付费章节置为免费
                            chapter.setType(0);
                            qtChapterRepository.save(chapter);
                        });
                    } else if (1 == textBook.get().getType()) { //为视频课程
                        List<QtVideoCourseChapter> chapterList = qtVideoCourseChapterRepository.findByTextBookId(textBookId);
                        chapterList.stream().filter(chapter -> null != chapter.getType() && 1 == chapter.getType()).forEach(chapter -> {  //将付费章节置为免费
                            chapter.setType(0);
                            qtVideoCourseChapterRepository.save(chapter);
                        });

                    }
                }
                qtTextBookRepository.save(textBook.get());
            }
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("setTextBookPrice exception");
            return "failure";
        }
    }

    /**
     * @param params
     * @return
     * @desc 改变书籍状态
     */
    @Override
    public Object changeBookStatus(JSONObject params) {
        try {
            int status = params.getInteger("status");
            String textBookId = params.getString("textBookId");
            Optional<QtTextBook> textBook = qtTextBookRepository.findById(textBookId);
            if (textBook.isPresent()) {
                textBook.get().setStatus(status);
                qtTextBookRepository.save(textBook.get());
            }
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("changeBookStatus exception");
            return "failure";
        }
    }

    /**
     * @return
     * @desc 章节上传-获取导师列表
     */
    @Override
    public Object getTeacherList() {
        List<Map<String, String>> teacherList = new ArrayList<>();
        JSONObject result = new JSONObject();
        List<QtTeacherInfo> teacherInfoList = qtTeacherRepository.findAllByFlagAndState(1, "1");
        if (teacherInfoList.size() <= 0) {
            logger.error("no teacher");
            result.put("teacherList", new ArrayList<>());
        } else {
            for (int i = 0; i < teacherInfoList.size(); i++) {
                Map<String, String> teacherMap = new HashMap<>();
                teacherMap.put("guid", teacherInfoList.get(i).getGuid());
                teacherMap.put("name", teacherInfoList.get(i).getName());
                teacherList.add(teacherMap);
            }
            result.put("teacherList", teacherList);
        }
        return result;
    }

    /**
     * @param request
     * @return
     * @desc 获取章节总页数
     */
    @Override
    public Object chapterTotalNum(HttpServletRequest request) {
        String path = request.getParameter("pdfPath");
        int pdfPageNum = 0;
        JSONObject result = new JSONObject();
        try {
            PdfReader pdfReader = new PdfReader(path);
            pdfPageNum = pdfReader.getNumberOfPages();
            result.put("pdfPageNum", pdfPageNum);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("pdfReader exception{}" + e.getMessage());
            return "failure";
        }
    }


    /**
     * @param request
     * @return
     * @desc 避免教材名重复
     */
    @Override
    public Object checkDuplicate(HttpServletRequest request) {
        String bookName = request.getParameter("bookName");
        int isDuplicate = 0; //是否重复
        JSONObject result = new JSONObject();
        if (null != qtTextBookRepository.findByBookNameAndIsDeleted(bookName, 0)) {
            isDuplicate = 1;
        }
        result.put("isDuplicate", isDuplicate);
        return result;
    }


    /**
     * 讲师讲解范围-书籍下拉框
     *
     * @return
     */
    @Override
    public Object bookList() {
        List<QtTeacherTryCourse> bookList = qtTeacherMapper.getTryCourseByRange();
        return bookList;
    }
}
