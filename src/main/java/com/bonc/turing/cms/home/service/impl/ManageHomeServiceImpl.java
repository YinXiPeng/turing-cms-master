package com.bonc.turing.cms.home.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bonc.turing.cms.common.utils.HttpUtil;
import com.bonc.turing.cms.home.domain.*;
import com.bonc.turing.cms.home.mapper.ManageHomeMapper;
import com.bonc.turing.cms.home.repository.ActivityRepository;
import com.bonc.turing.cms.home.repository.DiscussUpRepository;
import com.bonc.turing.cms.home.repository.NotebookLikesRepository;
import com.bonc.turing.cms.home.service.ManageHomeService;
import com.bonc.turing.cms.manage.entity.CmsSpecial;
import com.bonc.turing.cms.manage.entity.Notebook;
import com.bonc.turing.cms.manage.entity.competition.Discuss;
import com.bonc.turing.cms.manage.entity.user.SysUserInfo;
import com.bonc.turing.cms.manage.repository.CmsSpecialRepository;
import com.bonc.turing.cms.manage.repository.DiscussRepository;
import com.bonc.turing.cms.manage.repository.NotebookRepository;
import com.bonc.turing.cms.manage.repository.SysUserInfoRepository;
import com.bonc.turing.cms.manage.service.user.UserVisitService;
import com.bonc.turing.cms.user.bean.TuringLike;
import com.bonc.turing.cms.user.bean.TuringResource;
import com.bonc.turing.cms.user.dao.repository.TuringLikeRepository;
import com.bonc.turing.cms.user.dao.repository.TuringResourceRepository;
import com.bonc.turing.cms.user.service.OperateLogService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.*;
/**
 * @desc ??????-????????????????????????
 * @date 2020.03.18
 */
@Service
public class ManageHomeServiceImpl implements ManageHomeService {

    private static Logger logger = LoggerFactory.getLogger(ManageHomeService.class);

    @Value("${user.task.url}")
    private String userTaskUrl;

    @Value("${feed.sync}")
    private String feedSyncUrl;
    //??????????????????
    private static RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
    //?????????????????????md5
    private String algorithmName = "MD5";
    //??????????????????
    private final int hashIterations = 2;
    @Autowired
    private SysUserInfoRepository sysUserInfoRepository;
    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private DiscussUpRepository discussUpRepository;

    @Autowired
    private DiscussRepository discussRepository;

    @Autowired
    private NotebookLikesRepository notebookLikesRepository;

    @Autowired
    private NotebookRepository notebookRepository;

    @Resource
    private ManageHomeMapper manageHomeMapper;

    @Autowired
    private CmsSpecialRepository cmsSpecialRepository;


    @Autowired
    private UserVisitService userVisitService;

    @Value("${toc.pre}")
    private String tocNamePre;

    @Autowired
    private OperateLogService operateLogService;

    @Autowired
    private TuringLikeRepository likeRepository;

    @Autowired
    private TuringResourceRepository resourceRepository;


    /**
     * ???????????????
     *
     * @param sysUserInfo
     * @return
     */
    @Override
    public Object createAccount(SysUserInfo sysUserInfo) {
        try {
            String phone = ""; //?????????
            while (true) { //??????????????????????????????,27??????????????????
                phone = "27" + getRandomNum();
                if (null == sysUserInfoRepository.findByPhone(phone)) {
                    break;
                }
            }
            sysUserInfo.setUsername(tocNamePre + sysUserInfo.getNickname());
            sysUserInfo.setPhone(phone);
            //??????salt
            sysUserInfo.setSalt(randomNumberGenerator.nextBytes().toHex());
            //??????????????????
            String newPassword = new SimpleHash(algorithmName, sysUserInfo.getPassword(),
                    ByteSource.Util.bytes(sysUserInfo.getCredentialsSalt()), hashIterations).toHex();
            sysUserInfo.setPassword(newPassword);
            //??????guid
            sysUserInfo.setGuid(UUID.randomUUID().toString().replace("-", ""));
            sysUserInfo.setHeadimgurl(sysUserInfo.getHeadimgurl());
            sysUserInfo.setRegistts(System.currentTimeMillis());
            sysUserInfo.setManifesto("????????????????????????");
            //??????????????????????????????????????????
            sysUserInfo.setNoviceGuidanceSteps("1");
            sysUserInfo.setSource(6);
            sysUserInfo.setIsDeleted(0);
            sysUserInfoRepository.save(sysUserInfo);
            //?????????????????? ?????????
            try {
                userVisitService.findByGuid(sysUserInfo.getGuid());
            } catch (Exception e) {
                logger.error("User popularity initialization failed", e);
            }
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("createAccount exception{}", e);
            return "failure";
        }
    }

    /**
     * ???????????????
     *
     * @param params
     * @return
     */
    @Transactional
    @Override
    public Object deleteAccount(JSONObject params) {
        String guid = params.getString("guid");
        SysUserInfo userInfo = sysUserInfoRepository.findByGuid(guid);
        if (null == userInfo) {
            return "????????????guid???????????????";
        } else {
            userInfo.setIsDeleted(1);
            sysUserInfoRepository.save(userInfo);
            return "success";
        }

    }

    /**
     * ???????????????????????????
     *
     * @param username
     * @return
     */
    @Override
    public Object checkUserName(String username) {
        Integer isDuplicate = 0;
        if (null != sysUserInfoRepository.findByUsername(username)) {
            isDuplicate = 1;
        }
        return isDuplicate;
    }

    /**
     * ??????9?????????????????????
     *
     * @return
     */
    public String getRandomNum() {
        String randomStr = "";
        for (int i = 0; i < 9; i++) {
            int random = (int) (Math.random() * 9);
            randomStr += random;
        }
        return randomStr;
    }

    /**
     * ???????????????
     *
     * @param pageNum pageSize
     * @return
     */
    @Override
    public Object accountList(int pageNum, int pageSize) {
        JSONObject result = new JSONObject();
        Sort sort = new Sort(Sort.Direction.DESC, "registts"); //????????????????????????
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        Page<SysUserInfo> businessAccountList = sysUserInfoRepository.findAllByPhonePrefixPageable("27", pageable); //???????????????27??????
        result.put("businessAccountList", businessAccountList);
        return result;
    }

    /**
     * ????????????
     *
     * @return
     */
    @Override
    public Object createActivity(Activity activity) {
        try {
            activity.setCreateTime(new Date());
            activity.setStatus(0); //??????????????????
            activityRepository.save(activity);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("createActivity exception{}", e);
            return "failure";
        }
    }

    /**
     * ????????????
     *
     * @param pageNum
     * @param pageSize
     * @param title
     * @return
     */
    @Override
    public Object activityList(int pageNum, int pageSize, String title) {
        PageHelper.startPage(pageNum, pageSize);
        List<ActivityDTO> activityList = manageHomeMapper.findActivities(title);
        PageInfo<ActivityDTO> pageInfo = new PageInfo<>(activityList);
        return pageInfo;
    }


    /**
     * ????????????
     *
     * @param activityId
     * @return
     */
    @Override
    public Object deleteActivity(String activityId) {
        try {
            activityRepository.deleteById(activityId);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("deleteActivity exception{}", e);
            return "failure";
        }
    }

    /**
     * ???/????????????
     *
     * @param status activityId
     * @return
     */
    @Override
    public Object changeActivityStatus(int status, String activityId) {
        try {
            Optional<Activity> activity = activityRepository.findById(activityId);
            if (!activity.isPresent()) {
                return "????????????id???????????????";
            }
            activity.get().setStatus(status);
            activityRepository.save(activity.get());
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("changeActivityStatus exception{}", e);
            return "???????????????";
        }
    }

    /**
     * ?????????????????????????????????
     *
     * @param params(contentId,contentType,operateType)
     * @return
     */
    @Transactional
    @Override
    public Object deleteHomeContent(JSONObject params) {
        String contentId = params.getString("contentId");
        //???????????? 1:??????????????? 2????????? 3???????????? 4.??????????????????
        Integer contentType = params.getInteger("contentType");
        if (contentType == 1) { //???????????????,??????
            Optional<Discuss> discuss = discussRepository.findById(contentId);
            if (!discuss.isPresent()) {
                return "????????????id???????????????";
            }
            discuss.get().setDelFlag(1);
            if (discuss.get().getReferType().equals("dataSet")) {
                //?????????dataSet,?????????????????????
                if (null != discuss.get().getReferId() && !"".equals(discuss.get().getReferId())) {
                    manageHomeMapper.updateDataSourceDefDiscussNum(discuss.get().getReferId());
                }
            }
            discussRepository.save(discuss.get());
        } else if (contentType == 2) {
            //??????
            notebookRepository.deleteById(contentId);
        } else if (contentType == 3) {
            //????????? ?????????????????????-1
            manageHomeMapper.deleteSourceDefInfo(contentId);
        }else if(contentType == 4){
            //??????
            resourceRepository.deleteById(contentId);
        }
        cmsSpecialRepository.deleteBySpecialId(contentId);
        HttpUtil.sendGetRequest(feedSyncUrl);
        return "success";
    }


    /**
     * ???????????????????????????????????????????????????
     *
     * @param params(contentId,contentType,operateType)
     * @return
     */
    @Override
    public Object hideHomeContent(JSONObject params) {
        try {
            String contentId = params.getString("contentId");
            Integer contentType = params.getInteger("contentType");
            //0???????????? 1?????????
            Integer flag = params.getInteger("flag");

            List<CmsSpecial> cmsSpecialList = new ArrayList<>();
            Integer type = -1;
            if (1 == contentType) {
                //???????????????
                cmsSpecialList = cmsSpecialRepository.findBySpecialIdAndType(contentId, 1);
                type = 1;
            } else if (2 == contentType) {
                //??????
                Optional<Notebook> notebook = notebookRepository.findById(contentId);
                if ((notebook.isPresent())) {
                    if (null != notebook.get().getParentId() && !"".equals(notebook.get().getParentId())) {
                        cmsSpecialList = cmsSpecialRepository.findBySpecialIdAndType(notebook.get().getParentId(), 0);
                        contentId = notebook.get().getParentId();
                        type = 0;
                    } else {
                        return "??????????????????parentId??????";
                    }
                }
            } else if (3 == contentType) {
                //?????????
                String historyVersionId = manageHomeMapper.findHistoryVersionId(contentId);
                if (null == historyVersionId || "".equals(historyVersionId)) {
                    return "????????????historyVersionId??????";
                }
                cmsSpecialList = cmsSpecialRepository.findBySpecialIdAndType(historyVersionId, 3);
                contentId = historyVersionId;
                type = 3;
            } else if (4 == contentType) {
                //??????
                cmsSpecialList = cmsSpecialRepository.findBySpecialIdAndType(contentId, 4);
                type = 4;
            }
            if (cmsSpecialList.size() <= 0) {
                //???????????????????????????
                createCmsSpecialWithHide(contentId, type);
            } else {
                //??????
                updateCmsSpecialWithHide(cmsSpecialList, flag);
            }
            HttpUtil.sendGetRequest(feedSyncUrl);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("hideHomeContent exception{}", e);
            return "?????????????????????";
        }
    }


    /**
     * ??????????????????????????????
     *
     * @param contentId
     * @param type
     */
    public void createCmsSpecialWithHide(String contentId, int type) {
        CmsSpecial special = new CmsSpecial();
        special.setSpecialId(contentId);
        special.setType(type);
        special.setIsTop(0);
        special.setSortMethod(0);
        special.setIsElite(0);
        special.setIsHead(0);
        special.setState(0);
        special.setIsDeleted(0);
        special.setIsHomeHide(1);
        cmsSpecialRepository.save(special);
    }

    /**
     * ??????????????????????????????
     *
     * @param cmsSpecialList
     */
    public void updateCmsSpecialWithHide(List<CmsSpecial> cmsSpecialList, int flag) {
        CmsSpecial cmsSpecial = cmsSpecialList.get(0);
        cmsSpecial.setIsHomeHide(flag);
        cmsSpecialRepository.save(cmsSpecial);
    }

    /**
     * ??????????????????
     *
     * @param params
     * @return
     */
    @Transactional
    @Override
    public Object likeHomeContent(JSONObject params) {
        try {
            //??????id
            String contentId = params.getString("contentId");
            //???????????? 1:??????????????? 2????????? 3????????????  4:??????????????????
            Integer contentType = params.getInteger("contentType");
            //???????????????
            List<SysUserInfo> userInfoList = sysUserInfoRepository.findAllByPhonePrefix("27");
            //??????????????????????????????
            int flag = 0;
            if (userInfoList.size() <= 0) {
                return "???????????????????????????";
            }
            //????????????????????????
            if (1 == contentType) {
                //???????????????
                Optional<Discuss> discuss = discussRepository.findById(contentId);
                for (SysUserInfo sysUserInfo : userInfoList) {
                    if (null == discussUpRepository.findByDiscussIdAndUserId(contentId, sysUserInfo.getGuid())) {
                        DiscussUp discussUp = new DiscussUp();
                        discussUp.setDiscussId(contentId);
                        discussUp.setUserId(sysUserInfo.getGuid());
                        discuss.get().setUpNum(discuss.get().getUpNum() + 1);
                        //??????????????????
                        String url = userTaskUrl + "guid=" + sysUserInfo.getGuid() + "&referId=" + contentId + "&referName=paper" + "&taskId=19";
                        HttpUtil.sendGetRequest(url);
                        //??????????????????
                        operateLogService.updateOperateLogByOperateType("likes", sysUserInfo.getGuid());
                        discussRepository.save(discuss.get());
                        discussUpRepository.save(discussUp);
                        flag = 1;
                        break;
                    }
                }
            } else if (2 == contentType) {
                //??????
                for (SysUserInfo sysUserInfo : userInfoList) {
                    if (null == notebookLikesRepository.findByNotebookIdAndGuid(contentId, sysUserInfo.getGuid())) {
                        NotebookLikes notebookLikes = new NotebookLikes();
                        notebookLikes.setNotebookId(contentId);
                        notebookLikes.setGuid(sysUserInfo.getGuid());
                        notebookLikes.setLikesTime(new Date().toString());

                        //??????????????????
                        String url = userTaskUrl + "guid=" + sysUserInfo.getGuid() + "&referId=" + contentId + "&referName=model" + "&taskId=19";
                        HttpUtil.sendGetRequest(url);

                        //??????????????????
                        operateLogService.updateOperateLogByOperateType("likes", sysUserInfo.getGuid());
                        notebookLikesRepository.save(notebookLikes);
                        flag = 1;
                        break;
                    }
                }
            } else if (3 == contentType) {
                //?????????
                for (SysUserInfo sysUserInfo : userInfoList) {
                    if (null == manageHomeMapper.findDsLikeUser(contentId, sysUserInfo.getGuid())) {
                        DSLikeUser dsLikeUser = new DSLikeUser();
                        dsLikeUser.setId(UUID.randomUUID().toString().replace("-", ""));
                        dsLikeUser.setGiveLike(true);
                        //??????????????????
                        String url = userTaskUrl + "guid=" + sysUserInfo.getGuid() + "&referId=" + contentId + "&referName=" + "&taskId=19";
                        HttpUtil.sendGetRequest(url);
                        //??????????????????
                        operateLogService.updateOperateLogByOperateType("likes", sysUserInfo.getGuid());
                        dsLikeUser.setDataSourceDefId(manageHomeMapper.findDataSourceDefId(contentId));
                        dsLikeUser.setGuid(dsLikeUser.getGuid());
                        manageHomeMapper.updateDataSourceDef(contentId);
                        manageHomeMapper.insertDsLikeUser(dsLikeUser);
                        flag = 1;
                        break;
                    }
                }
            } else if (4 == contentType) {
                //??????
                for (SysUserInfo sysUserInfo : userInfoList) {
                    if (null == likeRepository.findByUserIdAndReferIdAndType(sysUserInfo.getGuid(), contentId, 4)) {
                        TuringLike like = new TuringLike();
                        like.setUserId(sysUserInfo.getGuid());
                        like.setReferId(contentId);
                        like.setType(4);
                        like.setCreateTime(new Date());
                        likeRepository.save(like);
                        Optional<TuringResource> resource = resourceRepository.findById(contentId);
                        if (resource.isPresent()) {
                            resource.get().setLikeNum(resource.get().getLikeNum() + 1);
                            resourceRepository.save(resource.get());
                        }
                        flag = 1;
                        break;
                    }
                }
            }
            if (flag == 0) {
                return "??????????????????????????????????????????";
            }
            if (4 != contentType) {
                //???????????????feed
                HttpUtil.sendGetRequest(feedSyncUrl);
            }
            return "success";
        } catch (Exception e) {
            return "????????????";
        }
    }

    /**
     * feed??????????????????
     *
     * @return
     */
    @Override
    public Object feedTypeList() {
        List<Map> result = new ArrayList<>();
        result = manageHomeMapper.findByDictionaryName("feed??????");
        return result;
    }
}
