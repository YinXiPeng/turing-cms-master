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
 * @desc 后台-管理前台展示内容
 * @date 2020.03.18
 */
@Service
public class ManageHomeServiceImpl implements ManageHomeService {

    private static Logger logger = LoggerFactory.getLogger(ManageHomeService.class);

    @Value("${user.task.url}")
    private String userTaskUrl;

    @Value("${feed.sync}")
    private String feedSyncUrl;
    //随机数生成器
    private static RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
    //指定散列算法为md5
    private String algorithmName = "MD5";
    //散列迭代次数
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
     * 生成马甲号
     *
     * @param sysUserInfo
     * @return
     */
    @Override
    public Object createAccount(SysUserInfo sysUserInfo) {
        try {
            String phone = ""; //手机号
            while (true) { //找一个不重复的手机号,27开始是马甲号
                phone = "27" + getRandomNum();
                if (null == sysUserInfoRepository.findByPhone(phone)) {
                    break;
                }
            }
            sysUserInfo.setUsername(tocNamePre + sysUserInfo.getNickname());
            sysUserInfo.setPhone(phone);
            //生成salt
            sysUserInfo.setSalt(randomNumberGenerator.nextBytes().toHex());
            //生成加密密码
            String newPassword = new SimpleHash(algorithmName, sysUserInfo.getPassword(),
                    ByteSource.Util.bytes(sysUserInfo.getCredentialsSalt()), hashIterations).toHex();
            sysUserInfo.setPassword(newPassword);
            //生成guid
            sysUserInfo.setGuid(UUID.randomUUID().toString().replace("-", ""));
            sysUserInfo.setHeadimgurl(sysUserInfo.getHeadimgurl());
            sysUserInfo.setRegistts(System.currentTimeMillis());
            sysUserInfo.setManifesto("一句话介绍我自己");
            //用户注册默认新手引导为第一步
            sysUserInfo.setNoviceGuidanceSteps("1");
            sysUserInfo.setSource(6);
            sysUserInfo.setIsDeleted(0);
            sysUserInfoRepository.save(sysUserInfo);
            //用户受欢迎度 初始化
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
     * 删除马甲号
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
            return "不存在该guid对应的用户";
        } else {
            userInfo.setIsDeleted(1);
            sysUserInfoRepository.save(userInfo);
            return "success";
        }

    }

    /**
     * 判断用户名是否重复
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
     * 生成9位随机数字符串
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
     * 马甲号列表
     *
     * @param pageNum pageSize
     * @return
     */
    @Override
    public Object accountList(int pageNum, int pageSize) {
        JSONObject result = new JSONObject();
        Sort sort = new Sort(Sort.Direction.DESC, "registts"); //注册时间降序排序
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        Page<SysUserInfo> businessAccountList = sysUserInfoRepository.findAllByPhonePrefixPageable("27", pageable); //运营号全以27开头
        result.put("businessAccountList", businessAccountList);
        return result;
    }

    /**
     * 创建活动
     *
     * @return
     */
    @Override
    public Object createActivity(Activity activity) {
        try {
            activity.setCreateTime(new Date());
            activity.setStatus(0); //默认为待处理
            activityRepository.save(activity);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("createActivity exception{}", e);
            return "failure";
        }
    }

    /**
     * 活动列表
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
     * 删除活动
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
     * 上/下线活动
     *
     * @param status activityId
     * @return
     */
    @Override
    public Object changeActivityStatus(int status, String activityId) {
        try {
            Optional<Activity> activity = activityRepository.findById(activityId);
            if (!activity.isPresent()) {
                return "不存在该id对应的活动";
            }
            activity.get().setStatus(status);
            activityRepository.save(activity.get());
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("changeActivityStatus exception{}", e);
            return "操作时异常";
        }
    }

    /**
     * 删除平台内容（全平台）
     *
     * @param params(contentId,contentType,operateType)
     * @return
     */
    @Transactional
    @Override
    public Object deleteHomeContent(JSONObject params) {
        String contentId = params.getString("contentId");
        //内容类型 1:问答或文章 2：模型 3：数据集 4.上传下载资源
        Integer contentType = params.getInteger("contentType");
        if (contentType == 1) { //问答或文章,假删
            Optional<Discuss> discuss = discussRepository.findById(contentId);
            if (!discuss.isPresent()) {
                return "不存在该id对应的内容";
            }
            discuss.get().setDelFlag(1);
            if (discuss.get().getReferType().equals("dataSet")) {
                //如果是dataSet,需要维护帖子数
                if (null != discuss.get().getReferId() && !"".equals(discuss.get().getReferId())) {
                    manageHomeMapper.updateDataSourceDefDiscussNum(discuss.get().getReferId());
                }
            }
            discussRepository.save(discuss.get());
        } else if (contentType == 2) {
            //模型
            notebookRepository.deleteById(contentId);
        } else if (contentType == 3) {
            //数据集 注意对应讨论数-1
            manageHomeMapper.deleteSourceDefInfo(contentId);
        }else if(contentType == 4){
            //资源
            resourceRepository.deleteById(contentId);
        }
        cmsSpecialRepository.deleteBySpecialId(contentId);
        HttpUtil.sendGetRequest(feedSyncUrl);
        return "success";
    }


    /**
     * 隐藏平台首页内容（只针对首页内容）
     *
     * @param params(contentId,contentType,operateType)
     * @return
     */
    @Override
    public Object hideHomeContent(JSONObject params) {
        try {
            String contentId = params.getString("contentId");
            Integer contentType = params.getInteger("contentType");
            //0取消隐藏 1：隐藏
            Integer flag = params.getInteger("flag");

            List<CmsSpecial> cmsSpecialList = new ArrayList<>();
            Integer type = -1;
            if (1 == contentType) {
                //文章或问答
                cmsSpecialList = cmsSpecialRepository.findBySpecialIdAndType(contentId, 1);
                type = 1;
            } else if (2 == contentType) {
                //模型
                Optional<Notebook> notebook = notebookRepository.findById(contentId);
                if ((notebook.isPresent())) {
                    if (null != notebook.get().getParentId() && !"".equals(notebook.get().getParentId())) {
                        cmsSpecialList = cmsSpecialRepository.findBySpecialIdAndType(notebook.get().getParentId(), 0);
                        contentId = notebook.get().getParentId();
                        type = 0;
                    } else {
                        return "该模型对应的parentId为空";
                    }
                }
            } else if (3 == contentType) {
                //数据集
                String historyVersionId = manageHomeMapper.findHistoryVersionId(contentId);
                if (null == historyVersionId || "".equals(historyVersionId)) {
                    return "该数据集historyVersionId为空";
                }
                cmsSpecialList = cmsSpecialRepository.findBySpecialIdAndType(historyVersionId, 3);
                contentId = historyVersionId;
                type = 3;
            } else if (4 == contentType) {
                //资源
                cmsSpecialList = cmsSpecialRepository.findBySpecialIdAndType(contentId, 4);
                type = 4;
            }
            if (cmsSpecialList.size() <= 0) {
                //没有就添加一条数据
                createCmsSpecialWithHide(contentId, type);
            } else {
                //修改
                updateCmsSpecialWithHide(cmsSpecialList, flag);
            }
            HttpUtil.sendGetRequest(feedSyncUrl);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("hideHomeContent exception{}", e);
            return "隐藏内容时异常";
        }
    }


    /**
     * 创建首页特殊处理操作
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
     * 更新首页特殊处理操作
     *
     * @param cmsSpecialList
     */
    public void updateCmsSpecialWithHide(List<CmsSpecial> cmsSpecialList, int flag) {
        CmsSpecial cmsSpecial = cmsSpecialList.get(0);
        cmsSpecial.setIsHomeHide(flag);
        cmsSpecialRepository.save(cmsSpecial);
    }

    /**
     * 用运营号点赞
     *
     * @param params
     * @return
     */
    @Transactional
    @Override
    public Object likeHomeContent(JSONObject params) {
        try {
            //内容id
            String contentId = params.getString("contentId");
            //内容类型 1:问答或文章 2：模型 3：数据集  4:上传下载资源
            Integer contentType = params.getInteger("contentType");
            //马甲号列表
            List<SysUserInfo> userInfoList = sysUserInfoRepository.findAllByPhonePrefix("27");
            //判断此次点赞是否成功
            int flag = 0;
            if (userInfoList.size() <= 0) {
                return "暂无运营号可供点赞";
            }
            //找一个马甲号点赞
            if (1 == contentType) {
                //文章或问答
                Optional<Discuss> discuss = discussRepository.findById(contentId);
                for (SysUserInfo sysUserInfo : userInfoList) {
                    if (null == discussUpRepository.findByDiscussIdAndUserId(contentId, sysUserInfo.getGuid())) {
                        DiscussUp discussUp = new DiscussUp();
                        discussUp.setDiscussId(contentId);
                        discussUp.setUserId(sysUserInfo.getGuid());
                        discuss.get().setUpNum(discuss.get().getUpNum() + 1);
                        //添加用户积分
                        String url = userTaskUrl + "guid=" + sysUserInfo.getGuid() + "&referId=" + contentId + "&referName=paper" + "&taskId=19";
                        HttpUtil.sendGetRequest(url);
                        //添加用户日志
                        operateLogService.updateOperateLogByOperateType("likes", sysUserInfo.getGuid());
                        discussRepository.save(discuss.get());
                        discussUpRepository.save(discussUp);
                        flag = 1;
                        break;
                    }
                }
            } else if (2 == contentType) {
                //模型
                for (SysUserInfo sysUserInfo : userInfoList) {
                    if (null == notebookLikesRepository.findByNotebookIdAndGuid(contentId, sysUserInfo.getGuid())) {
                        NotebookLikes notebookLikes = new NotebookLikes();
                        notebookLikes.setNotebookId(contentId);
                        notebookLikes.setGuid(sysUserInfo.getGuid());
                        notebookLikes.setLikesTime(new Date().toString());

                        //添加用户积分
                        String url = userTaskUrl + "guid=" + sysUserInfo.getGuid() + "&referId=" + contentId + "&referName=model" + "&taskId=19";
                        HttpUtil.sendGetRequest(url);

                        //添加用户日志
                        operateLogService.updateOperateLogByOperateType("likes", sysUserInfo.getGuid());
                        notebookLikesRepository.save(notebookLikes);
                        flag = 1;
                        break;
                    }
                }
            } else if (3 == contentType) {
                //数据集
                for (SysUserInfo sysUserInfo : userInfoList) {
                    if (null == manageHomeMapper.findDsLikeUser(contentId, sysUserInfo.getGuid())) {
                        DSLikeUser dsLikeUser = new DSLikeUser();
                        dsLikeUser.setId(UUID.randomUUID().toString().replace("-", ""));
                        dsLikeUser.setGiveLike(true);
                        //添加用户积分
                        String url = userTaskUrl + "guid=" + sysUserInfo.getGuid() + "&referId=" + contentId + "&referName=" + "&taskId=19";
                        HttpUtil.sendGetRequest(url);
                        //添加用户日志
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
                //资源
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
                return "运营号已用尽，请尝试重新建号";
            }
            if (4 != contentType) {
                //资源不刷新feed
                HttpUtil.sendGetRequest(feedSyncUrl);
            }
            return "success";
        } catch (Exception e) {
            return "点赞失败";
        }
    }

    /**
     * feed内容类型列表
     *
     * @return
     */
    @Override
    public Object feedTypeList() {
        List<Map> result = new ArrayList<>();
        result = manageHomeMapper.findByDictionaryName("feed类型");
        return result;
    }
}
