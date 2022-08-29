package com.bonc.turing.cms.manage.service.competition.impl;

import com.alibaba.fastjson.JSONObject;
import com.bonc.turing.cms.manage.entity.competition.Discuss;
import com.bonc.turing.cms.manage.repository.DiscussRepository;
import com.bonc.turing.cms.manage.service.competition.DiscussService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @ProjectName: cms
 * @Package: com.bonc.turing.cms.manage.service.competition.impl
 * @ClassName: DiscussServiceImpl
 * @Author: bxt
 * @Description:
 * @Date: 2019/7/17 15:09
 * @Version: 1.0
 */
@Service
public class DiscussServiceImpl implements DiscussService {
    @Autowired
    DiscussRepository discussRepository;


    /*
     * 方法说明:   讨论列表
     * Method:   getDiscussList
     * @param pageable
     * @param map
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     * @date 2019/7/17 16:24
     * @author bxt
     * @version v 1.0.0
     */
    @Override
    public List<Map<String, Object>> getDiscussList(Pageable pageable, Map<String, Object> map) {
        String referId = (String) map.get("referId");
        List<Map<String, Object>> list = discussRepository.getDiscussList(pageable, referId);
        return list;
    }

    /*
     * 方法说明:   讨论列表总条数
     * Method:   getRequirementListCount
     * @param map
     * @return int
     * @date 2019/7/17 16:24
     * @author bxt
     * @version v 1.0.0
     */
    @Override
    public int getRequirementListCount(Map<String, Object> map) {
        String referId = (String) map.get("referId");
        return discussRepository.getRequirementListCount(referId);
    }

    /*
     * 方法说明:   置顶
     * Method:   updateTopping
     * @param request
     * @return void
     * @date 2019/7/17 16:24
     * @author bxt
     * @version v 1.0.0
     */
    @Override
    @Transactional
    public void updateTopping(String id) {
        Optional<Discuss> op = discussRepository.getDiscussById(id);
        Discuss discuss = op.get();
        discuss.setTop(1);
        //判断当前讨论是否已经加精
        if (discuss.getExcellent().equals(0)) {
            //未加精
            discuss.setSorting(2);
        }
        if (discuss.getExcellent().equals(1)) {
            //加精
            discuss.setSorting(3);
        }
        discussRepository.saveAndFlush(discuss);
    }

    /*
     * 方法说明:   取消置顶
     * Method:   updateCacleTopping
     * @param request
     * @return void
     * @date 2019/7/17 16:24
     * @author bxt
     * @version v 1.0.0
     */
    @Override
    @Transactional
    public void updateCacleTopping(String id) {
        Optional<Discuss> op = discussRepository.getDiscussById(id);
        Discuss discuss = op.get();
        discuss.setTop(0);
        // 排序方式  置顶加精 --> 加精 置顶--> 普通
        if (discuss.getSorting().equals(3)) {
            //未加精
            discuss.setSorting(1);
        }
        if (discuss.getSorting().equals(2)) {
            //未加精
            discuss.setSorting(0);
        }
        discussRepository.saveAndFlush(discuss);
    }

    /*
     * 方法说明:   加精
     * Method:   updateRefinement
     * @param request
     * @return void
     * @date 2019/7/17 16:25
     * @author bxt
     * @version v 1.0.0
     */
    @Override
    @Transactional
    public void updateRefinement(String id) {
        Optional<Discuss> op = discussRepository.getDiscussById(id);
        Discuss discuss = op.get();
        discuss.setExcellent(1);
        // 排序方式 置顶-->置顶加精  普通-->  加精
        if (discuss.getSorting().equals(2)) {
            //未加精
            discuss.setSorting(3);
        }
        if (discuss.getSorting().equals(0)) {
            //未加精
            discuss.setSorting(1);
        }
        discussRepository.saveAndFlush(discuss);
    }

    /*
     * 方法说明:   取消加精
     * Method:   updateCacleRefinement
     * @param request
     * @return void
     * @date 2019/7/17 16:25
     * @author bxt
     * @version v 1.0.0
     */
    @Override
    @Transactional
    public void updateCacleRefinement(String id) {
        Optional<Discuss> op = discussRepository.getDiscussById(id);
        Discuss discuss = op.get();
        discuss.setExcellent(0);
        // 排序方式 置顶-->置顶加精  普通-->  加精
        if (discuss.getSorting().equals(3)) {
            //未加精
            discuss.setSorting(2);
        }
        if (discuss.getSorting().equals(1)) {
            //未加精
            discuss.setSorting(0);
        }
        discussRepository.saveAndFlush(discuss);
    }

    /*
     * 方法说明:   显示
     * Method:   updateShow
     * @param request
     * @return void
     * @date 2019/7/17 16:25
     * @author bxt
     * @version v 1.0.0
     */
    @Override
    @Transactional
    public void updateShow(String id) {
        Optional<Discuss> op = discussRepository.getDiscussById(id);
        Discuss discuss = op.get();
        discuss.setShowHide(0);
        discussRepository.saveAndFlush(discuss);
    }

    /*
     * 方法说明:   隐藏
     * Method:   updateHide
     * @param request
     * @return void
     * @date 2019/7/17 16:26
     * @author bxt
     * @version v 1.0.0
     */
    @Override
    @Transactional
    public void updateHide(String id) {
        Optional<Discuss> op = discussRepository.getDiscussById(id);
        Discuss discuss = op.get();
        discuss.setShowHide(1);
        discussRepository.saveAndFlush(discuss);
    }

    /*
     * 方法说明:   删除
     * Method:   deleteDiscuss
     * @param request
     * @return void
     * @date 2019/7/17 16:25
     * @author bxt
     * @version v 1.0.0
     */
    @Override
    @Transactional
    public void deleteDiscuss(String id) {
        // 获取当前话题的评论id
        List<String> cIds = discussRepository.getCIdsById(id);
        if (cIds.size() > 0) {
            for (String cId : cIds) {
                // 通过评论id删除回复
                discussRepository.deleteReplyByCId(cId);
            }
        }
        // 通过讨论id删除评论
        discussRepository.deleteCommentByCId(id);
        // 删除讨论
        discussRepository.deleteDiscussById(id);
    }

    @Override
    @Transactional
    public void updateRemark(String id, JSONObject params) {
        Optional<Discuss> op = discussRepository.getDiscussById(id);
        Discuss discuss = op.get();
        discuss.setRemark(params.get("remark").toString());
        discussRepository.saveAndFlush(discuss);
    }
}
