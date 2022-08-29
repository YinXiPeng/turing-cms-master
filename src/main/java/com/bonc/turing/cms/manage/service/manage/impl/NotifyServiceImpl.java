package com.bonc.turing.cms.manage.service.manage.impl;

import com.alibaba.fastjson.JSONObject;
import com.bonc.turing.cms.manage.entity.notify.Notify;
import com.bonc.turing.cms.manage.entity.notify.NotifyEnter;
import com.bonc.turing.cms.manage.entity.user.SysUserInfo;
import com.bonc.turing.cms.manage.repository.NotifyEntryRepository;
import com.bonc.turing.cms.manage.repository.NotifyRepository;
import com.bonc.turing.cms.manage.repository.SysUserInfoRepository;
import com.bonc.turing.cms.manage.service.manage.NotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.data.domain.Pageable;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author lky
 * @desc 消息通知
 * @date 2019/8/6 16:59
 */
@Service
public class NotifyServiceImpl implements NotifyService{

    @Autowired
    private NotifyRepository notifyRepository;
    @Autowired
    private SysUserInfoRepository sysUserInfoRepository;
    @Autowired
    private NotifyEntryRepository notifyEntryRepository;
    @Override
    public JSONObject getNotifyList(int pageNum, int pageSize, String keyWord) {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "notifyTime"));
        PageRequest pageRequest = new PageRequest(pageNum,pageSize,sort);
        Specification query = new Specification<NotifyEnter>() {

            @Override
            public Predicate toPredicate(Root<NotifyEnter> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (!StringUtils.isEmpty(keyWord)) {
                    predicates.add(criteriaBuilder.like(root.get("notifyTitle"), keyWord));
                    predicates.add(criteriaBuilder.like(root.get("notifyMsg"), keyWord));
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        Page all = notifyEntryRepository.findAll(query, pageRequest);
        JSONObject object = new JSONObject();
        List content = all.getContent();
        ArrayList<JSONObject> jsonObjects = new ArrayList<>();
        for (Object notify:content){
            String s = JSONObject.toJSONString(notify);
            JSONObject object1 = JSONObject.parseObject(s);
            String adminGuid = object1.getString("adminGuid");
            Optional<SysUserInfo> byId = sysUserInfoRepository.findById(adminGuid);
            if (byId.isPresent()){
                SysUserInfo sysUserInfo = byId.get();
                object1.put("userName",sysUserInfo.getUsername());
                object1.put("realName",sysUserInfo.getRealName());
            }else {
                object1.put("userName","");
                object1.put("realName","");
            }
            jsonObjects.add(object1);
        }
        object.put("notifyList",jsonObjects);
        object.put("totalElements",all.getTotalElements());
        object.put("totalPages",all.getTotalPages());
        return object;
    }
}
