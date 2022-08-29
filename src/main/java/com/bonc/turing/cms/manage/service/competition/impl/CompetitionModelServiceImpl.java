package com.bonc.turing.cms.manage.service.competition.impl;

import com.alibaba.fastjson.JSONObject;
import com.bonc.turing.cms.manage.entity.competition.CompetitionModel;
import com.bonc.turing.cms.manage.entity.competition.CompetitionModelSort;
import com.bonc.turing.cms.manage.entity.competition.Discuss;
import com.bonc.turing.cms.manage.repository.CompetitionModelRepository;
import com.bonc.turing.cms.manage.repository.CompetitionModelSortRepository;
import com.bonc.turing.cms.manage.service.competition.CompetitionModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @ProjectName: cms
 * @Package: com.bonc.turing.cms.manage.service.competition.impl
 * @ClassName: CompetitionModelServiceImpl
 * @Author: bxt
 * @Description: 竞赛-模型
 * @Date: 2019/7/19 10:39
 * @Version: 1.0
 */
@Service
public class CompetitionModelServiceImpl implements CompetitionModelService {

    @Autowired
    CompetitionModelRepository competitionModelRepository;
    @Autowired
    CompetitionModelSortRepository competitionModelSortRepository;

    /*
     * 方法说明:   竞赛-模型列表
     * Method:   getModelList
     * @param pageable
     * @param map
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     * @date 2019/7/19 11:02
     * @author bxt
     * @version v 1.0.0
     */
    @Override
    public List<Map<String, Object>> getModelList(Pageable pageable, Map<String, Object> map) {
        String competitionId = (String) map.get("competitionId");
        int isPublic = (int) map.get("isPublic");
        List<Map<String, Object>> list = competitionModelRepository.getModelList(pageable, competitionId, isPublic);
        return list;
    }

    /*
     * 方法说明:   竞赛-模型总条数
     * Method:   getModelListCount
     * @param map
     * @return int
     * @date 2019/7/19 11:02
     * @author bxt
     * @version v 1.0.0
     */
    @Override
    public int getModelListCount(Map<String, Object> map) {
        String competitionId = (String) map.get("competitionId");
        int isPublic = (int) map.get("isPublic");
        return competitionModelRepository.getModelListCount(competitionId, isPublic);
    }

    /*
     * 方法说明:   置顶
     * Method:   updateTopping
     * @param notebookId
     * @return void
     * @date 2019/7/19 11:17
     * @author bxt
     * @version v 1.0.0
     */
    @Override
    @Transactional
    public void updateTopping(String notebookId,String competitionId) {
        //模型置顶信息
        Optional<CompetitionModelSort> op = competitionModelSortRepository.getCompetitionModelSortById(notebookId);
        CompetitionModelSort competitionModelSort = new CompetitionModelSort();
        if (op.isPresent()){
            competitionModelSort = op.get();
            competitionModelSort.setIsTop(1);
            competitionModelSort.setModifyTime(new Date());

            //判断是否加精
            if (competitionModelSort.getIsRefinement().equals(1)) {
                competitionModelSort.setSort(3);
            } else {
                competitionModelSort.setSort(2);
            }
            competitionModelSortRepository.saveAndFlush(competitionModelSort);
        } else {
            // 未置顶过的情况下  所有排序为2 置顶为1 加精为0
            CompetitionModelSort competitionSort = new CompetitionModelSort();
            competitionSort.setNotebookId(notebookId);
            competitionSort.setModifyTime(new Date());
            competitionSort.setSort(2);
            competitionSort.setIsTop(1);
            competitionSort.setIsRefinement(0);
            competitionSort.setCompetitionId(competitionId);
            competitionModelSortRepository.saveAndFlush(competitionSort);
        }
    }

    /*
     * 方法说明:   取消置顶
     * Method:   updateCacleTopping
     * @param notebookId
     * @return void
     * @date 2019/7/19 11:21
     * @author bxt
     * @version v 1.0.0
     */
    @Override
    @Transactional
    public void updateCacleTopping(String notebookId,String competitionId) {
        //模型置顶信息
        Optional<CompetitionModelSort> op = competitionModelSortRepository.getCompetitionModelSortById(notebookId);
        CompetitionModelSort competitionModelSort = new CompetitionModelSort();
        if (op.isPresent()){
            competitionModelSort = op.get();
            competitionModelSort.setIsTop(0);
            competitionModelSort.setModifyTime(new Date());
            //判断是否加精
            if (competitionModelSort.getIsRefinement().equals(1)) {
                competitionModelSort.setSort(1);
            } else {
                competitionModelSort.setSort(0);
            }
            competitionModelSortRepository.saveAndFlush(competitionModelSort);
        } else {
            // 未置顶过的情况下  所有值全部赋值为0
            CompetitionModelSort competitionSort = new CompetitionModelSort();
            competitionSort.setNotebookId(notebookId);
            competitionSort.setModifyTime(new Date());
            competitionSort.setSort(0);
            competitionSort.setIsTop(0);
            competitionSort.setIsRefinement(0);
            competitionModelSortRepository.saveAndFlush(competitionSort);
        }
    }

    /*
     * 方法说明:    加精
     * Method:   updateRefinement
     * @param notebookId
     * @return void
     * @date 2019/7/19 11:26
     * @author bxt
     * @version v 1.0.0
     */
    @Override
    @Transactional
    public void updateRefinement(String notebookId,String competitionId) {
        //模型信息
        Optional<CompetitionModelSort> op = competitionModelSortRepository.getCompetitionModelSortById(notebookId);
        CompetitionModelSort competitionModelSort = new CompetitionModelSort();
        if (op.isPresent()){
            competitionModelSort = op.get();
            competitionModelSort.setIsRefinement(1);
            competitionModelSort.setModifyTime(new Date());
            //判断是否置顶
            if (competitionModelSort.getIsTop().equals(1)) {
                competitionModelSort.setSort(3);
            } else {
                competitionModelSort.setSort(1);
            }
            competitionModelSortRepository.saveAndFlush(competitionModelSort);
        }
        else {
            // 未置加精的情况下  所有值全部赋值为0
            CompetitionModelSort competitionSort = new CompetitionModelSort();
            competitionSort.setNotebookId(notebookId);
            competitionSort.setModifyTime(new Date());
            competitionSort.setSort(1);
            competitionSort.setIsTop(0);
            competitionSort.setIsRefinement(1);
            competitionSort.setCompetitionId(competitionId);
            competitionModelSortRepository.saveAndFlush(competitionSort);
        }
    }

    /*
     * 方法说明:   取消加精
     * Method:   updateCacleRefinement
     * @param notebookId
     * @return void
     * @date 2019/7/19 11:28
     * @author bxt
     * @version v 1.0.0
     */
    @Override
    @Transactional
    public void updateCacleRefinement(String notebookId,String competitionId) {
        //模型信息
        Optional<CompetitionModelSort> op = competitionModelSortRepository.getCompetitionModelSortById(notebookId);
        CompetitionModelSort competitionModelSort = new CompetitionModelSort();
        if (op.isPresent()){
            competitionModelSort = op.get();
            competitionModelSort.setIsRefinement(0);
            competitionModelSort.setModifyTime(new Date());
            //判断是否置顶
            if (competitionModelSort.getIsTop().equals(1)) {
                competitionModelSort.setSort(2);
            } else {
                competitionModelSort.setSort(0);
            }
            competitionModelSortRepository.saveAndFlush(competitionModelSort);
        } else {
            // 未置加精的情况下  所有值全部赋值为0
            CompetitionModelSort competitionSort = new CompetitionModelSort();
            competitionSort.setNotebookId(notebookId);
            competitionSort.setModifyTime(new Date());
            competitionSort.setSort(0);
            competitionSort.setIsTop(0);
            competitionSort.setIsRefinement(0);
            competitionModelSortRepository.saveAndFlush(competitionSort);
        }
    }

    /*
     * 方法说明:   公开
     * Method:   updatePublic
     * @param notebookId
     * @return void
     * @date 2019/7/19 11:33
     * @author bxt
     * @version v 1.0.0
     */
    @Override
    @Transactional
    public void updatePublic(String notebookId) {
        //模型信息
        Optional<CompetitionModel> op = competitionModelRepository.getCompetitionModelById(notebookId);
        CompetitionModel competitionModel = new CompetitionModel();
        if(op.isPresent()){
            competitionModel = op.get();
            String parentId = competitionModel.getParentId();
            competitionModelRepository.updateChildrenModel(parentId,0,competitionModel.getIsDelete());
            competitionModelRepository.updateParentModel(parentId,0,competitionModel.getIsDelete());
            //私有关联表
            competitionModelRepository.updateModelisPublic(parentId,0);
        }


    }

    /*
     * 方法说明:   私有
     * Method:   updatePrivate
     * @param notebookId
     * @return void
     * @date 2019/7/19 11:34
     * @author bxt
     * @version v 1.0.0
     */
    @Override
    @Transactional
    public void updatePrivate(String notebookId) {
        //模型信息
        Optional<CompetitionModel> op = competitionModelRepository.getCompetitionModelById(notebookId);
        CompetitionModel competitionModel = new CompetitionModel();
        if(op.isPresent()){
            competitionModel = op.get();
            String parentId = competitionModel.getParentId();
            competitionModelRepository.updateChildrenModel(parentId,1,competitionModel.getIsDelete());
            competitionModelRepository.updateParentModel(parentId,1,competitionModel.getIsDelete());
            //私有关联表
            competitionModelRepository.updateModelisPublic(parentId,1);
        }

    }

    /*
     * 方法说明:   删除
     * Method:   updatePrivate
     * @param notebookId
     * @return void
     * @date 2019/7/19 11:36
     * @author bxt
     * @version v 1.0.0
     */
    @Override
    @Transactional
    public void updateIsDelete(String notebookId) {
        //模型信息
        Optional<CompetitionModel> op = competitionModelRepository.getCompetitionModelById(notebookId);
        CompetitionModel competitionModel = new CompetitionModel();
        if(op.isPresent()){
            competitionModel = op.get();
            competitionModel.setIsDelete(0);
            String parentId = competitionModel.getParentId();
            competitionModelRepository.updateChildrenModel(parentId,competitionModel.getIsPublic(),0);
            competitionModelRepository.updateParentModel(parentId,competitionModel.getIsPublic(),0);
            //删除关联表
            competitionModelRepository.updateModelisDelete(parentId,0);

        }

    }

    /*
     * 方法说明:   修改标签
     * Method:   updatemodelSort
     * @param notebookId
     * @param params
     * @return void
     * @date 2019/7/20 12:10
     * @author bxt
     * @version v 1.0.0
     */
    @Override
    @Transactional
    public  void updatemodelSort(String notebookId,JSONObject params){
        Optional<CompetitionModelSort> op = competitionModelSortRepository.getCompetitionModelSortById(notebookId);
        CompetitionModelSort competitionModelSort = op.get();
        competitionModelSort.setRemark(params.get("remark").toString());
        competitionModelSortRepository.saveAndFlush(competitionModelSort);
    }

}
