package com.bonc.turing.cms.user.service.impl;
import com.bonc.turing.cms.user.bean.OperateLog;
import com.bonc.turing.cms.user.dao.repository.OperateLogRepository;
import com.bonc.turing.cms.user.service.OperateLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;
import static com.bonc.turing.cms.common.utils.DateUtil.*;

/**
 * 用户操作日志及积分相关
 */
@Service
public class OperateLogServiceImpl implements OperateLogService {
    @Autowired
    private OperateLogRepository operateLogRepository;

    @Override
    public List<OperateLog> findByGuid(String guid, long beginTime, long endTime) {
        List<OperateLog> operateLogForSearch = operateLogRepository.findByUserGuid(guid, beginTime, endTime);
        return operateLogForSearch;
    }

    /**
     * 更新用户操作日志
     * @param operateType
     * @param guid
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public void updateOperateLogByOperateType(String operateType, String guid) {
        long todayFirstTime = getTodayFirstTime();
        long todayLastTime = getTodayLastTime();
        long todayNowTime = getTodayNowTimeStamp();
        OperateLog operateLogForSearch = operateLogRepository.findByUserGuidAndOperateTimestampIsBetween(guid, todayFirstTime, todayLastTime);
        //如果今日日志是空先初始化数据
        if (operateLogForSearch == null) {
            operateLogForSearch = new OperateLog();
            operateLogForSearch.setUserGuid(guid);
            operateLogForSearch.setOperateType("");
            operateLogForSearch.setResourceGuid("");
            operateLogForSearch.setOperateTimestamp(todayNowTime);
            operateLogForSearch.setActivityLevel(1);
        }
        if (operateType.equalsIgnoreCase("fork")) {
            int count = operateLogForSearch.getFork();
            operateLogForSearch.setFork(count + 1);
        }
        if (operateType.equalsIgnoreCase("likes")) {
            int count = operateLogForSearch.getLikes();
            operateLogForSearch.setLikes(count + 1);
        }
        if (operateType.equalsIgnoreCase("posting")) {
            int count = operateLogForSearch.getPosting();
            operateLogForSearch.setPosting(count + 1);
        }
        if (operateType.equalsIgnoreCase("comment")) {
            int count = operateLogForSearch.getComment();
            operateLogForSearch.setComment(count + 1);
        }
        if (operateType.equalsIgnoreCase("groups")) {
            int count = operateLogForSearch.getGroups();
            operateLogForSearch.setGroups(count + 1);
        }
        if (operateType.equalsIgnoreCase("noteBookCommit")) {
            int count = operateLogForSearch.getNoteBookCommit();
            operateLogForSearch.setNoteBookCommit(count + 1);
        }
        if (operateType.equalsIgnoreCase("members")) {
            int count = operateLogForSearch.getMembers();
            operateLogForSearch.setMembers(count + 1);
        }
        if (operateType.equalsIgnoreCase("salon")) {
            int count = operateLogForSearch.getSalon();
            operateLogForSearch.setSalon(count + 1);
        }
        if (operateType.equalsIgnoreCase("model")) {
            int count = operateLogForSearch.getModel();
            operateLogForSearch.setModel(count + 1);
        }
        if (operateType.equalsIgnoreCase("dataSet")) {
            int count = operateLogForSearch.getDataSet();
            operateLogForSearch.setDataSet(count + 1);
        }
        operateLogRepository.save(operateLogForSearch);
    }


}
