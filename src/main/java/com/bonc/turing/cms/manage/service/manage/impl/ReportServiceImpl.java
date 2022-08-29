package com.bonc.turing.cms.manage.service.manage.impl;

import com.bonc.turing.cms.common.utils.KafkaUtils;
import com.bonc.turing.cms.manage.entity.user.SysUserInfo;
import com.bonc.turing.cms.manage.repository.UserInfoRepository;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bonc.turing.cms.manage.mapper.report.ReportMapper;
import com.bonc.turing.cms.manage.service.manage.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author lky
 * @desc
 * @date 2019/8/7 14:46
 */
@Service
public class ReportServiceImpl implements ReportService {

    private static Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);

    @Value("${kafka.producer.topic.system}")
    private String topicSystem;
    @Resource
    private ReportMapper reportMapper;
    @Autowired
    private UserInfoRepository userInfoRepository;

    @Override
    public Map<String, Object> getReportList(int pageNum, int pageSize) {
        List<Map> reportList;
        long totle;
        if (-1==pageNum||-1==pageSize){
            reportList = reportMapper.getReportList();
            totle = reportList.size();
        }else {
            PageHelper.startPage(pageNum,pageSize);
            List<Map> reportList1 = reportMapper.getReportList();
            PageInfo<Map> mapPageInfo = new PageInfo<>(reportList1);
            reportList = mapPageInfo.getList();
            totle = mapPageInfo.getTotal();
        }
        for (Map report:reportList){
            String reportId = report.get("reportId").toString();
            String reportType = report.get("reportType").toString();
            if ("0".equals(reportType)){
                //查评论内容
                Map comment = reportMapper.getCommentById(reportId);
                if (null!=comment){
                    report.put("content",comment.get("content"));
                }else {
                    report.put("content","");
                }
            }else if ("1".equals(reportType)){
                //查回复内容
                Map reply = reportMapper.getReplyById(reportId);
                if (null!=reply){
                    report.put("content",reply.get("content"));
                }else {
                    report.put("content","");
                }
            }else {
                report.put("content","");
            }
            String adminGuid = null==report.get("adminGuid")?"":report.get("adminGuid").toString();
            Optional<SysUserInfo> byGuid = userInfoRepository.findByGuid(adminGuid);
            if (byGuid.isPresent()){
                SysUserInfo sysUserInfo = byGuid.get();
                report.put("adminUserName",sysUserInfo.getUsername());
                report.put("adminRealName",sysUserInfo.getRealName());
            }else {
                report.put("adminUserName","");
                report.put("adminRealName","");
            }
            String userId = null==report.get("userId")?"":report.get("userId").toString();
            Optional<SysUserInfo> byGuid1 = userInfoRepository.findByGuid(userId);
            if (byGuid1.isPresent()){
                SysUserInfo sysUserInfo = byGuid1.get();
                report.put("userIdUserName",sysUserInfo.getUsername());
                report.put("userIdRealName",sysUserInfo.getRealName());
            }else {
                report.put("userIdUserName","");
                report.put("userIdRealName","");
            }
            String informer = null==report.get("informer")?"":report.get("informer").toString();
            Optional<SysUserInfo> byGuid2 = userInfoRepository.findByGuid(informer);
            if (byGuid2.isPresent()){
                SysUserInfo sysUserInfo = byGuid2.get();
                report.put("informerUserName",sysUserInfo.getUsername());
                report.put("informerRealName",sysUserInfo.getRealName());
            }else {
                report.put("informerUserName","");
                report.put("informerRealName","");
            }

        }

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("reportList",reportList);
        hashMap.put("totle",totle);
        return hashMap;
    }

    @Override
    public int changeReportStatus(String guid, String type, String reportId,int status1) {
        if ("0".equals(type.trim())){
            Map comment = reportMapper.getCommentReportById(reportId);
            int status = (int) comment.get("status");
            if (1==status1){
                if (2==status||0==status){
                    //改为审核通过
                    int i = reportMapper.updateCommentStatusToPass(guid,reportId);
                    if (i>0){
                        Map informerNotify = setNotifyPassToInformer(comment,type);
                        Map userIdNotify = setNotifyPassToUserId(comment,type);
                        KafkaUtils.sendMessage(topicSystem,informerNotify);
                        KafkaUtils.sendMessage(topicSystem,userIdNotify);
                        logger.info("举报消息发送成功,informerNotify:{},userIdNotify:{}",informerNotify,userIdNotify);
                        return 1;
                    }else {
                        return 2;
                    }
                }else if (1==status){
                    //状态不需要改变
                    return 1;
                }
            }else if (0==status1){
                if (2==status||1==status){
                    //改为审核拒绝
                    int i = reportMapper.updateCommentStatusToRefuse(guid,reportId);
                    if (i>0){
                        Map informerNotify = setNotifyRefuseToInformer(comment,type);
                        KafkaUtils.sendMessage(topicSystem,informerNotify);
                        logger.info("举报消息发送成功,informerNotify:{}",informerNotify);
                        return 1;
                    }else {
                        return 2;
                    }
                }else if (0==status){
                    return 1;
                }
            }

        }else if ("1".equals(type.trim())){
            Map reply = reportMapper.getReplyReportById(reportId);
            int status = (int) reply.get("status");
            if (1==status1){
                if (2==status||0==status){
                    //改为审核通过
                    int i = reportMapper.updateReplyStatusToPass(guid,reportId);
                    if (i>0){
                        Map informerNotify = setNotifyPassToInformer(reply,type);
                        Map userIdNotify = setNotifyPassToUserId(reply,type);
                        KafkaUtils.sendMessage(topicSystem,informerNotify);
                        KafkaUtils.sendMessage(topicSystem,userIdNotify);
                        logger.info("举报消息发送成功,informerNotify:{},userIdNotify:{}",informerNotify,userIdNotify);
                        return 1;
                    }else {
                        return 2;
                    }
                }else if (1==status){
                    return 1;
                }
            }else if (0==status1){
                if (2==status||1==status){
                    //改为审核拒绝
                    int i = reportMapper.updateReplyStatusToRefuse(guid,reportId);
                    if (i>0){
                        Map informerNotify = setNotifyRefuseToInformer(reply,type);
                        KafkaUtils.sendMessage(topicSystem,informerNotify);
                        logger.info("举报消息发送成功,informerNotify:{}",informerNotify);
                        return 1;
                    }else {
                        return 2;
                    }
                }else if (0==status){
                    return 1;
                }
            }

        }else {
            return -1;
        }
        return -1;
    }

    /**
     * 编辑审核通过发送给被举报人的通知的格式
     * @param report
     * @param type
     * @return
     */
    private Map setNotifyPassToUserId(Map report, String type) {
        HashMap<Object, Object> map = new HashMap<>();
        if("0".equals(type)){
            //评论
            Map crId = reportMapper.getCommentById(report.get("crId").toString());
            map.put("notifyTitle","系统通知");
            map.put("notifyType",0);
            map.put("notifyMsg","您的<b>评论</b>:"+crId.get("content")+"被举报");
            String s="很遗憾，您的言论"+crId.get("content")+"因为";
            Integer type1 = Integer.valueOf(report.get("type").toString());
            if (0==type1){
                s=s+"淫秽信息";
            }else if (1==type1){
                s=s+"违法信息";
            }else if (2==type1){
                s=s+"营销广告";
            }else if (3==type1){
                s=s+"恶意攻击谩骂";
            }else if (4==type1){
                s=s+report.get("state");
            }
            s=s+"原因被举报了。经管理员审核后，您此留言已经被删除。请共同保护图灵联邦这个我们的交流学习家园。";
            map.put("notifyEmailMsg",s);
            map.put("notifyUserGuid",report.get("user_id"));
            map.put("notifyPhoneFlag",1);
            map.put("notifyEmailFlag",1);
            map.put("notifyWebFlag",1);
            map.put("readState",0);
            map.put("issend",0);
            return map;
        }else if ("1".equals(type)){
            //回复
            Map rrId = reportMapper.getReplyById(report.get("rrId").toString());
            map.put("notifyTitle","系统通知");
            map.put("notifyType",0);
            map.put("notifyMsg","您的<b>评论</b>:"+rrId.get("content")+"被举报");
            String s="很遗憾，您的言论"+rrId.get("content")+"因为";
            Integer type1 = Integer.valueOf(report.get("type").toString());
            if (0==type1){
                s=s+"淫秽信息";
            }else if (1==type1){
                s=s+"违法信息";
            }else if (2==type1){
                s=s+"营销广告";
            }else if (3==type1){
                s=s+"恶意攻击谩骂";
            }else if (4==type1){
                s=s+report.get("state");
            }
            s=s+"原因被举报了。经管理员审核后，您此留言已经被删除。请共同保护图灵联邦这个我们的交流学习家园。";
            map.put("notifyEmailMsg",s);
            map.put("notifyUserGuid",report.get("user_id"));
            map.put("notifyPhoneFlag",1);
            map.put("notifyEmailFlag",1);
            map.put("notifyWebFlag",1);
            map.put("readState",0);
            map.put("issend",0);
            return map;
        }else {
            return null;
        }
    }

    /**
     * 编辑审核拒绝发送的通知的格式
     * @param report
     * @param type
     * @return
     */
    private Map setNotifyRefuseToInformer(Map report, String type) {
        HashMap<Object, Object> map = new HashMap<>();
        if("0".equals(type)){
            //评论
            Map crId = reportMapper.getCommentById(report.get("crId").toString());
            map.put("notifyTitle","系统通知");
            map.put("notifyType",0);
            map.put("notifyMsg","您的举报:"+crId.get("content")+",经审查<b>举报不成立</b>");
            map.put("notifyEmailMsg","您举报的言论"+crId.get("content")+"经管理员审核未发现违规内容，虽然我们没有采纳您此次的意见，但也请继续关注图灵联邦并共同维护我们交流学习的家园。");
            map.put("notifyUserGuid",report.get("informer"));
            map.put("notifyPhoneFlag",1);
            map.put("notifyEmailFlag",1);
            map.put("notifyWebFlag",1);
            map.put("readState",0);
            map.put("issend",0);
            return map;
        }else if ("1".equals(type)){
            //回复
            Map rrId = reportMapper.getReplyById(report.get("rrId").toString());
            map.put("notifyTitle","系统通知");
            map.put("notifyType",0);
            map.put("notifyMsg","您的举报:"+rrId.get("content")+",经审查<b>举报不成立</b>");
            map.put("notifyEmailMsg","您举报的言论"+rrId.get("content")+"经管理员审核未发现违规内容，虽然我们没有采纳您此次的意见，但也请继续关注图灵联邦并共同维护我们交流学习的家园。");
            map.put("notifyUserGuid",report.get("informer"));
            map.put("notifyPhoneFlag",1);
            map.put("notifyEmailFlag",1);
            map.put("notifyWebFlag",1);
            map.put("readState",0);
            map.put("issend",0);
            return map;
        }else {
            return null;
        }
    }

    /**
     * 编辑审核通过发送的通知的格式
     * @param report
     * @param type
     * @return
     */
    private Map setNotifyPassToInformer(Map report, String type) {
        HashMap<Object, Object> map = new HashMap<>();
        if("0".equals(type)){
            //评论
            Map crId = reportMapper.getCommentById(report.get("crId").toString());
            map.put("notifyTitle","系统通知");
            map.put("notifyType",0);
            map.put("notifyMsg","您的举报:"+crId.get("content")+",经审查<b>举报成立</b>,该内容已删除。");
            map.put("notifyEmailMsg","您举报的言论"+crId.get("content")+"经管理员审核已经被删除。感谢您的关注，也请继续维护图灵联邦这个我们的交流学习家园。");
            map.put("notifyUserGuid",report.get("informer"));
            map.put("notifyPhoneFlag",1);
            map.put("notifyEmailFlag",1);
            map.put("notifyWebFlag",1);
            map.put("readState",0);
            map.put("issend",0);
            return map;
        }else if ("1".equals(type)){
            //回复
            Map rrId = reportMapper.getReplyById(report.get("rrId").toString());
            map.put("notifyTitle","系统通知");
            map.put("notifyType",0);
            map.put("notifyMsg","您的举报:"+rrId.get("content")+",经审查<b>举报成立</b>,该内容已删除。");
            map.put("notifyEmailMsg","您举报的言论"+rrId.get("content")+"经管理员审核已经被删除。感谢您的关注，也请继续维护图灵联邦这个我们的交流学习家园。");
            map.put("notifyUserGuid",report.get("informer"));
            map.put("notifyPhoneFlag",1);
            map.put("notifyEmailFlag",1);
            map.put("notifyWebFlag",1);
            map.put("readState",0);
            map.put("issend",0);
            return map;
        }else {
            return null;
        }
    }
}
