package com.bonc.turing.cms.exercise.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bonc.turing.cms.exercise.dao.mapper.QtTeacherMapper;
import com.bonc.turing.cms.exercise.dao.repository.*;
import com.bonc.turing.cms.exercise.domain.*;
import com.bonc.turing.cms.exercise.dto.TeacherDetailDTO;
import com.bonc.turing.cms.exercise.dto.TeacherListDTO;
import com.bonc.turing.cms.exercise.service.QtTeacherService;
import com.bonc.turing.cms.manage.repository.SysUserInfoRepository;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class QtTeacherServiceImpl implements QtTeacherService {

    private static Logger logger = LoggerFactory.getLogger(QtTeacherServiceImpl.class);


    @Autowired
    CosProgressRepository cosProgressRepository;


    @Autowired
    QtTeacherRepository qtTeacherRepository;

    @Autowired
    SysUserInfoRepository sysUserInfoRepository;


    @Autowired
    CosCourseRepository cosCourseRepository;

    @Autowired
    QtTextTeacherVideoRepository qtTextTeacherVideoRepository;

    @Autowired
    private QtTeacherMapper qtTeacherMapper;

    /**
     * 上、下线导师
     *
     * @param isOnline
     * @return
     */
    @Override
    public Object changeStatus(int isOnline, String guid) {
        QtTeacherInfo teacherInfo = qtTeacherRepository.findByGuidAndFlag(guid, 1);
        if (null == teacherInfo) {
            return "不存在该guid对应的老师";
        }
        try {
            teacherInfo.setIsOnline(isOnline);
            qtTeacherRepository.save(teacherInfo);
            return "success";
        } catch (Exception e) {
            return "failure";
        }
    }

    /**
     * 审核导师是否通过,如果通过，则需要建立导师-教材关系
     *
     * @param isPass,guid
     * @return
     */
    @Transactional
    @Override
    public Object verifyTeacher(int isPass, String guid) {
        try {
            QtTeacherInfo teacherInfo = qtTeacherRepository.findByGuidAndFlag(guid, 1);
            if (1 == isPass) {
                teacherInfo.setState("1");
                //如果审核通过，绑定教师与教材（讲课范围）
                bindingTeacherAndTextbook(teacherInfo);
            } else {
                teacherInfo.setState("2");
            }
            return "success";
        } catch (Exception e) {
            return "failure";
        }
    }

    /**
     * 编辑教师信息
     *
     * @param params
     * @return
     */
    @Transactional
    @Override
    public Object editTeacherInfo(JSONObject params) {
        try {
            JSONArray videos = params.getJSONArray("videos");
            JSONObject jsonObject = params.getJSONObject("teacherInfo");
            QtTeacherInfo teacherInfo = JSON.parseObject(jsonObject.toJSONString(), QtTeacherInfo.class);
            QtTeacherInfo oldTeacherInfo = qtTeacherRepository.findByGuidAndFlag(teacherInfo.getGuid(), 1);
            //未修改字段保留旧数据
            if (null != oldTeacherInfo) {
                teacherInfo.setStars(oldTeacherInfo.getStars());
                teacherInfo.setState(oldTeacherInfo.getState());
                teacherInfo.setFlag(oldTeacherInfo.getFlag());
                teacherInfo.setIsOnline(oldTeacherInfo.getIsOnline());
            }
            qtTeacherRepository.save(teacherInfo);
            //删除已有的试讲视频
            qtTextTeacherVideoRepository.deleteAllByGuid(teacherInfo.getGuid());
            if (videos.size() > 0) {
                for (int i = 0; i < videos.size(); i++) {
                    QtTextTeacherVideo textTeacherVideo = JSON.parseObject(videos.get(i).toString(), QtTextTeacherVideo.class);
                    qtTextTeacherVideoRepository.save(textTeacherVideo);
                }
            }
            //如果当前导师为审核通过状态，建立导师-教材联系
            if (oldTeacherInfo.getState().equals("1")) {
                bindingTeacherAndTextbook(teacherInfo);
            }
            return "success";
        } catch (Exception e) {
            logger.error("editTeacherInfo exception{}", e);
            return "failure";
        }
    }

    /**
     * 绑定教师-课程（审核教师资格、编辑教师信息时使用）
     *
     * @param teacherInfo
     */
    @Transactional
    void bindingTeacherAndTextbook(QtTeacherInfo teacherInfo) {
        String textBookIds = teacherInfo.getTextBookIds();
        //先删除该导师与课程的绑定关系(平台课程，复合型形式)
        cosProgressRepository.deleteAllByGuid(teacherInfo.getGuid(),2,1);
        if (null != textBookIds && !textBookIds.equals("")) {
            String[] ids = textBookIds.split(",");
            if (ids.length > 0) {
                for (int i = 0; i < ids.length; i++) {
                    String id = ids[i];
                    CosProgress cosProgress = cosProgressRepository.findByCourseIdAndGuid(id,teacherInfo.getGuid());
                    if (null == cosProgress) {
                        CosProgress progress = new CosProgress();
                        progress.setUpdateTime(new Date());
                        progress.setCreateTime(new Date());
                        progress.setGuid(teacherInfo.getGuid());
                        progress.setSectionId("");
                        progress.setCourseId(id);
                        cosProgressRepository.save(progress);
                    }
                }
            }
        }
    }

    /**
     * 显示导师信息详情
     *
     * @param guid
     * @return
     */
    @Override
    public Object showTeacherDetail(String guid) {
        QtTeacherInfo teacherInfo = qtTeacherRepository.findByGuidAndFlag(guid, 1);
        if (null == teacherInfo) {
            return "failure";
        }
        try {
            TeacherDetailDTO teacherDetail = new TeacherDetailDTO();
            teacherDetail.setGuid(teacherInfo.getGuid());
            teacherDetail.setName(teacherInfo.getName());
            teacherDetail.setRealName(teacherInfo.getRealName());
            teacherDetail.setIntroduction(teacherInfo.getIntroduction());
            teacherDetail.setTags(teacherInfo.getTags());
            teacherDetail.setPhotoUrls(teacherInfo.getPhotoUrls());
            teacherDetail.setMajor(teacherInfo.getMajor());
            teacherDetail.setAvailableTime(teacherInfo.getAvailableTime());
            teacherDetail.setLevel(teacherInfo.getLevel());
            teacherDetail.setIdCard(teacherInfo.getIdCard());
            teacherDetail.setPhone(teacherInfo.getPhone());
            List<QtTextTeacherVideo> videos = qtTextTeacherVideoRepository.findAllByGuid(guid);
            teacherDetail.setVideos(videos);
            List<QtTeacherTryCourse> textBooks = new ArrayList<>();
            String textBookIds = teacherInfo.getTextBookIds();
            if (null != textBookIds && !textBookIds.equals("")) {
                //以“，”分割字符串
                String[] ids = textBookIds.split(",");
                for (int i = 0; i < ids.length; i++) {
                    String id = ids[i];
                    QtTeacherTryCourse course = qtTeacherMapper.getTryCourse(id);
                    if (null!=course) {
                        textBooks.add(course);
                    }
                }
            }
            teacherDetail.setTextBooks(textBooks);
            return teacherDetail;
        } catch (Exception e) {
            logger.error("showTeacherDetail exception{}", e);
            return "failure";
        }
    }

    /**
     * 显示待审核导师列表
     *
     * @param pageNum
     * @param pageSize
     * @param state
     * @return
     */
    @Override
    public Object showPendingTeacherList(int pageNum, int pageSize, String state) {
        try {
            PageHelper.startPage(pageNum, pageSize);
            List<QtTeacherInfo> pendingTeacherList = new ArrayList<>();
            if (state.equals("-1")) { //显示全部
                pendingTeacherList = qtTeacherMapper.getPendingOrFailureTeacherList();
            } else {
                pendingTeacherList = qtTeacherMapper.getPendingTeacherList(state);
            }

            PageInfo<QtTeacherInfo> pageInfo = new PageInfo<>(pendingTeacherList);
            return pageInfo;
        } catch (Exception e) {
            logger.error("showPendingTeacherList exception{}", e);
            return "failure";
        }
    }

    /**
     * 显示导师列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public Object showTeacherList(int pageNum, int pageSize) {
        try {
            PageHelper.startPage(pageNum, pageSize);
            List<TeacherListDTO> teacherList = qtTeacherMapper.getTeacherList();
            PageInfo<TeacherListDTO> pageInfo = new PageInfo<>(teacherList);
            return pageInfo;
        } catch (Exception e) {
            logger.error("showTeacherList exception{}", e);
            return "failure";
        }
    }
}
