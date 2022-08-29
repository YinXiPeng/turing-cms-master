package com.bonc.turing.cms.exercise.service;

import com.alibaba.fastjson.JSONObject;
import com.bonc.turing.cms.exercise.domain.CosPaper;
import com.bonc.turing.cms.exercise.dto.CosPaperListDTO;
import com.bonc.turing.cms.exercise.dto.CosSchoolCourseListDTO;
import com.bonc.turing.cms.exercise.vo.PaperAddUpdateVO;
import com.bonc.turing.cms.exercise.vo.SearchPaperVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * description:
 *
 * @author lxh
 * @date 2020/3/23 12:22
 */
public interface CosPaperService {
    /**
     * 添加/更新试卷
     *
     * @param paperAddUpdateVo
     * @param isAdd
     * @return
     */
    CosPaper addOrUpdatePaper(PaperAddUpdateVO paperAddUpdateVo, boolean isAdd);

    /**
     * 查询试卷列表
     *
     * @param pageable
     * @param searchPaperVo
     * @return
     */
    Page<CosPaperListDTO> listPaper(Pageable pageable, SearchPaperVO searchPaperVo);

    /**
     * 查询试卷详情
     *
     * @param isUpdate
     * @param paperId
     * @return
     */
    JSONObject paperInfo(Boolean isUpdate, String paperId);

    /**
     * 校验试卷名称唯一性
     *
     * @param paperName
     * @param schoolId
     * @param paperId
     * @return
     */
    Boolean checkUniqueName(String paperName, String schoolId, String paperId);

    /**
     * 删除试卷
     *
     * @param paperId
     */
    void delete(String paperId);

    /**
     * 查询学校课程列表信息
     *
     * @param schoolId
     * @return
     */
    List<CosSchoolCourseListDTO> schoolCourseList(String schoolId);
}
