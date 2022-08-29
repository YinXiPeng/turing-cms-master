package com.bonc.turing.cms.manage.service.manage.impl;

import com.alibaba.fastjson.JSONObject;
import com.bonc.turing.cms.common.utils.HttpUtil;
import com.bonc.turing.cms.home.mapper.ManageHomeMapper;
import com.bonc.turing.cms.manage.dto.ProductInfoDTO;
import com.bonc.turing.cms.manage.entity.*;
import com.bonc.turing.cms.manage.entity.competition.Discuss;
import com.bonc.turing.cms.manage.entity.user.SysUserInfo;
import com.bonc.turing.cms.manage.repository.*;
import com.bonc.turing.cms.manage.service.manage.ManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author lky
 * @date 2019/7/19 10:14
 */
@Service
public class ManageServiceImpl implements ManageService {

    @Autowired
    private ProductInfoRepository productInfoRepository;
    @Autowired
    private OrderUserRepository orderUserRepository;
    @Autowired
    private OrderTryRepository orderTryRepository;
    @Autowired
    private OrderInfoRepository orderInfoRepository;
    @Autowired
    private CmsSpecialRepository cmsSpecialRepository;
    @Autowired
    private DiscussRepository discussRepository;
    @Autowired
    private NotebookRepository notebookRepository;
    @Resource
    private ManageHomeMapper manageHomeMapper;


    @Autowired
    private SysUserInfoRepository sysUserInfoRepository;

    @Value("${feed.sync}")
    private String feedSyncUrl;

    @Value("${user.task.url}")
    private String userTaskUrl;

    @Override
    public Object queryProduct() {
        JSONObject json = new JSONObject();
        List<ProductInfoDTO> list = new ArrayList<>();
        List<ProductInfo> all = productInfoRepository.findAll();
        for (int i = 0; i < all.size(); i++) {
            ProductInfo productInfo = all.get(i);
            String string = JSONObject.toJSONString(productInfo);
            JSONObject jsonObject = JSONObject.parseObject(string);
            String subscribeTime = productInfo.getSubscribeTime();
            String[] split = subscribeTime.split(",");
            jsonObject.put("subscribeTime", split);
            ProductInfoDTO productInfoDTO = JSONObject.toJavaObject(jsonObject, ProductInfoDTO.class);
            list.add(productInfoDTO);
        }
        json.put("productList", list);
        return json;
    }

    /**
     * 获取试用信息
     *
     * @param params
     * @return
     */
    @Override
    public Object queryProductPeopleList(JSONObject params) {
        String productId = params.getString("productId");
        List<OrderTry> byProductId = orderTryRepository.findByProductId(productId);
        Optional<ProductInfo> productInfo = productInfoRepository.findById(productId);
        for (OrderTry orderTry : byProductId) {
            String guid = orderTry.getGuid();
            Calendar calendar = Calendar.getInstance();
            Date exipreTime = null;
            Optional<OrderInfoUser> byId = orderUserRepository.findById(guid);
            SysUserInfo sysUserInfo = new SysUserInfo();
            if (byId.isPresent()) {
                sysUserInfo = sysUserInfoRepository.findByGuid(guid);
            }
            if (productInfo.isPresent()) {
                orderTry.setProductName(productInfo.get().getProductName());
                orderTry.setProductVersion(productInfo.get().getProductVersion());
            }
            calendar.setTime(orderTry.getCreatedTime());
            calendar.add(Calendar.DATE, 15);
            exipreTime = calendar.getTime();
            String phoneNumber = null;
            phoneNumber = sysUserInfo.getPhone();
            if (null != exipreTime) {
                orderTry.setExpireTime(exipreTime);
            }
            if (byId.isPresent()) {
                orderTry.setOrderInfoUser(byId.get());
            }
            if (null != phoneNumber) {
                orderTry.setPhoneNumber(phoneNumber);
            }
        }
        PageBean pageBean = new PageBean();
        PageBean pageResult = pageBean.paging(byProductId, params);
        return pageResult;
    }

    /**
     * 获取购买信息
     *
     * @param params
     * @return
     */
    @Override
    public Object queryOrderList(JSONObject params) {
        String productId = params.getString("productId");
        List<OrderInfo> byProductId = orderInfoRepository.findByProductId(productId);
        Optional<ProductInfo> productInfo = productInfoRepository.findById(productId);
        for (OrderInfo orderInfo : byProductId) {
            Optional<OrderInfoUser> byId = orderUserRepository.findById(orderInfo.getUserId());
            SysUserInfo sysUserInfo = new SysUserInfo();
            if (byId.isPresent()) {
                sysUserInfo = sysUserInfoRepository.findByGuid(orderInfo.getUserId());
            }
            if (productInfo.isPresent()) {
                orderInfo.setProductName(productInfo.get().getProductName());
                orderInfo.setProductVersion(productInfo.get().getProductVersion());
            }
            String phoneNumber = null;
            phoneNumber = sysUserInfo.getPhone();

            if (phoneNumber != null) {
                orderInfo.setPhoneNumber(phoneNumber);
            }
            Calendar calendar = Calendar.getInstance();
            Date exipreTime = null;
            calendar.setTime(orderInfo.getCreateTime());
            calendar.add(Calendar.MONTH, orderInfo.getSubscribeTimeLimit());
            exipreTime = calendar.getTime();
            if (null != exipreTime) {
                orderInfo.setExpireTime(exipreTime);
            }
            if (byId.isPresent()) {
                orderInfo.setOrderInfoUser(byId.get());
            }
        }
        PageBean pageBean = new PageBean();
        PageBean pageResult = pageBean.paging(byProductId, params);
        return pageResult;
    }

    @Override
    public int changeState(String id, int type, int method, String guid) {
        //把id转成specialId
        String specialId = getSpecialId(id,type);
        List<CmsSpecial> bySpecialIdAndType = cmsSpecialRepository.findBySpecialIdAndType(specialId, type);
        int flag = 0;
        if (null != bySpecialIdAndType && 0 != bySpecialIdAndType.size()) {
            CmsSpecial cmsSpecial = bySpecialIdAndType.get(0);
            cmsSpecial.setModifyId(guid);
            flag = realChangeState(cmsSpecial, method);
        } else {
            //表中没有该id
            CmsSpecial cmsSpecial = new CmsSpecial();
            cmsSpecial.setSpecialId(specialId);
            cmsSpecial.setType(type);
            cmsSpecial.setModifyId(guid);
            cmsSpecial.setTopTime(new Date());
            cmsSpecial.setState(0);
            cmsSpecial.setIsElite(0);
            cmsSpecial.setIsHead(0);
            cmsSpecial.setIsTop(0);
            cmsSpecial.setSortMethod(0);
            cmsSpecial.setHeadTime(new Date());
            cmsSpecial.setIsDeleted(0);
            cmsSpecial.setIsHomeHide(0);
            cmsSpecial.setRemark("");
            flag = realChangeState(cmsSpecial, method);
        }
        return flag;
    }

    private String getSpecialId(String id, int type) {
        if (0==type){
            //模型
            Optional<Notebook> byId = notebookRepository.findById(id);
            if (byId.isPresent()) {
                Notebook notebook = byId.get();
                return notebook.getParentId();
            } else {
                return null;
            }
        }
        if (3==type){
            //数据集
            Map dataSet = manageHomeMapper.findDataSetMsgById(id);
            Object historyVersionId = dataSet.get("history_version_id");
            if (null == historyVersionId) {
                return null;
            }
            return historyVersionId.toString();
        }
        return id;
    }

    @Override
    public Map<String, Object> getNewsList(String keyWord, int pageNum, int pageSize) {
        Pageable pageRequest = new PageRequest(pageNum, pageSize);
        List<Map<String, Object>> newsList = cmsSpecialRepository.getNewsList(keyWord, pageRequest);
        int count = cmsSpecialRepository.getCountNewsList(keyWord);
        HashMap<String, Object> map = new HashMap<>();
        map.put("list", newsList);
        map.put("count", count);
        return map;

    }

    @Override
    public void updateRemark(JSONObject params, String guid) {
        String type = params.getString("type");
        String primaryId = params.getString("primaryId");
        String remark = params.getString("remark");
        if ("0".equals(type)) {
            //模型
            List<CmsSpecial> bySpecialIdAndType = cmsSpecialRepository.findBySpecialIdAndType(primaryId, 0);
            if (null == bySpecialIdAndType || 0 == bySpecialIdAndType.size()) {
                CmsSpecial cmsSpecial = new CmsSpecial();
                cmsSpecial.setSpecialId(primaryId);
                cmsSpecial.setType(Integer.valueOf(type));
                cmsSpecial.setModifyId(guid);
                cmsSpecial.setTopTime(new Date());
                cmsSpecial.setState(0);
                cmsSpecial.setIsElite(0);
                cmsSpecial.setIsHead(0);
                cmsSpecial.setIsTop(0);
                cmsSpecial.setSortMethod(0);
                cmsSpecial.setHeadTime(new Date());
                cmsSpecial.setIsDeleted(0);
                cmsSpecial.setRemark(remark);
                cmsSpecialRepository.save(cmsSpecial);
            } else {
                CmsSpecial cmsSpecial = bySpecialIdAndType.get(0);
                cmsSpecial.setRemark(remark);
                cmsSpecial.setModifyId(guid);
                cmsSpecialRepository.save(cmsSpecial);
            }
        } else if ("1".equals(type)) {
            //话题
            Optional<Discuss> byId = discussRepository.findById(primaryId);
            if (byId.isPresent()) {
                Discuss discuss = byId.get();
                discuss.setRemark(remark);
                discussRepository.save(discuss);
            }
        } else if ("2".equals(type)) {
            //新闻
            List<CmsSpecial> bySpecialIdAndType = cmsSpecialRepository.findBySpecialIdAndType(primaryId, 2);
            if (null == bySpecialIdAndType || 0 == bySpecialIdAndType.size()) {
                CmsSpecial cmsSpecial = new CmsSpecial();
                cmsSpecial.setSpecialId(primaryId);
                cmsSpecial.setType(Integer.valueOf(type));
                cmsSpecial.setModifyId(guid);
                cmsSpecial.setTopTime(new Date());
                cmsSpecial.setState(0);
                cmsSpecial.setIsElite(0);
                cmsSpecial.setIsHead(0);
                cmsSpecial.setIsTop(0);
                cmsSpecial.setSortMethod(0);
                cmsSpecial.setHeadTime(new Date());
                cmsSpecial.setIsDeleted(0);
                cmsSpecial.setRemark(remark);
                cmsSpecialRepository.save(cmsSpecial);
            } else {
                CmsSpecial cmsSpecial = bySpecialIdAndType.get(0);
                cmsSpecial.setRemark(remark);
                cmsSpecial.setModifyId(guid);
                cmsSpecialRepository.save(cmsSpecial);
            }
        }
    }

    @Override
    public Map<String, Object> getSalonList(String keyWord, int pageNum, int pageSize) {
        Pageable pageRequest = new PageRequest(pageNum, pageSize);
        List<Map<String, Object>> salonList = cmsSpecialRepository.getSalonList(keyWord, pageRequest);
        int count = cmsSpecialRepository.getCountSalonList(keyWord);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("list", salonList);
        hashMap.put("count", count);
        return hashMap;
    }

    @Override
    public Map<String, Object> getModelList(String keyWord, int pageNum, int pageSize) {
        Pageable pageRequest = new PageRequest(pageNum, pageSize);
        List<Map<String, Object>> getModelList = cmsSpecialRepository.getModelList(keyWord, pageRequest);
        int count = cmsSpecialRepository.getCountModelList(keyWord);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("list", getModelList);
        hashMap.put("count", count);
        return hashMap;
    }

    private int realChangeState(CmsSpecial cmsSpecial, int method) {
        String id = cmsSpecial.getSpecialId();
        int type = cmsSpecial.getType();
        if (0 == type) {
            //模型
            List<Notebook> notebooks = notebookRepository.findByParentIdOrderByVersion(id);
            if (null!=notebooks&&0!=notebooks.size()){
                cmsSpecial.setUserId(notebooks.get(0).getGuid());
            }else {
                return 1;
            }
        } else if (3 == type) {
            //数据集
        } else if (1 == type) {
            //帖子
            Optional<Discuss> discuss = discussRepository.findById(id);
            if (discuss.isPresent()) {
                cmsSpecial.setUserId(discuss.get().getUserId());
            }
        }
        changeAttributeAndSave(cmsSpecial, method);
        HttpUtil.sendGetRequest(feedSyncUrl);
        return 0;
    }

    //修改里面的字段并保存
    private void changeAttributeAndSave(CmsSpecial cmsSpecial, int method) {
        if (1 == method) {
            //置顶
            int isTop = cmsSpecial.getIsTop();
            if (0 == isTop) {
                //原来未置顶,改为置顶状态
                cmsSpecial.setIsTop(1);
                cmsSpecial.setTopTime(new Date());
                int sortMethod = cmsSpecial.getSortMethod();
                if (0 == sortMethod) {
                    cmsSpecial.setSortMethod(2);
                } else if (1 == sortMethod) {
                    cmsSpecial.setSortMethod(3);
                }
                cmsSpecialRepository.save(cmsSpecial);
            } else if (1 == isTop) {
                //原来置顶,改为未置顶状态
                //不是特殊的东西,删掉
                if (0 == cmsSpecial.getIsElite() && 0 == cmsSpecial.getIsHead() && 0 == cmsSpecial.getIsDeleted() && "".equals(cmsSpecial.getRemark()) && 0 == cmsSpecial.getIsDeleted()) {
                    cmsSpecialRepository.delete(cmsSpecial);
                } else {
                    cmsSpecial.setIsTop(0);
                    if (3 == cmsSpecial.getSortMethod()) {
                        //原来是置顶加精,改为加精
                        cmsSpecial.setSortMethod(1);
                    } else if (2 == cmsSpecial.getSortMethod()) {
                        //原来是置顶,改为普通
                        cmsSpecial.setSortMethod(0);
                    }
                    cmsSpecialRepository.save(cmsSpecial);
                }
            }

        } else if (2 == method) {
            //加精
            int isElite = cmsSpecial.getIsElite();
            if (0 == isElite) {
                //未加精,改为加精状态
                cmsSpecial.setIsElite(1);
                cmsSpecialRepository.save(cmsSpecial);
                //添加用户操作日志及积分
                Integer type = cmsSpecial.getType();
                if (0 == type) {
                    //模型
                    String url = userTaskUrl+"guid="+cmsSpecial.getUserId()+"&referId="+cmsSpecial.getSpecialId()+"&referName=model"+"&taskId=4";
                    HttpUtil.sendGetRequest(url);
                } else if (1 == type) {
                    //帖子
                    String url = userTaskUrl+"guid="+cmsSpecial.getUserId()+"&referId="+cmsSpecial.getSpecialId()+"&referName=model"+"&taskId=3";
                    HttpUtil.sendGetRequest(url);
                }
            } else if (1 == isElite) {
                if (0 == cmsSpecial.getIsTop() && 0 == cmsSpecial.getIsHead() && 0 == cmsSpecial.getIsDeleted() && "".equals(cmsSpecial.getRemark()) && 0 == cmsSpecial.getIsDeleted()) {
                    //不是特殊处理的,删掉
                    cmsSpecialRepository.delete(cmsSpecial);
                } else {
                    cmsSpecial.setIsElite(0);
                    if (3 == cmsSpecial.getSortMethod()) {
                        //原来是置顶加精,改为置顶
                        cmsSpecial.setSortMethod(2);
                    } else if (1 == cmsSpecial.getSortMethod()) {
                        //原来是加精,改为普通
                        cmsSpecial.setSortMethod(0);
                    }
                    cmsSpecialRepository.save(cmsSpecial);
                }
            }
        } else if (3 == method) {
            //公有私有
            if (0 == cmsSpecial.getType()) {
                //模型
                Optional<Notebook> byId = notebookRepository.findById(cmsSpecial.getSpecialId());
                if (byId.isPresent()) {
                    Notebook notebook = byId.get();
                    int isPublic = notebook.getIsPublic();
                    if (0 == isPublic) {
                        notebook.setIsPublic(1);
                    } else if (1 == isPublic) {
                        notebook.setIsPublic(0);
                    }
                    notebookRepository.save(notebook);
                }
            }

        }
    }


}
