package com.bonc.turing.cms.manage.service.user.impl;


import com.bonc.turing.cms.manage.entity.user.SysUserInfo;
import com.bonc.turing.cms.manage.repository.UserInfoRepository;
import com.bonc.turing.cms.manage.service.user.UserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.*;

/**
 * @author liuyunkai
 * @date 2018-11-6 10:40
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {
    private static final Logger logger = LoggerFactory.getLogger(UserInfoServiceImpl.class);

    @Autowired
    private UserInfoRepository userInfoRepository;


    /**
     * 通过username查找用户信息;用户身份验证
     */
    @Override
    public SysUserInfo getByUsername(String username) {
        return userInfoRepository.findByUsername(username);
    }

    @Override
    public Optional<SysUserInfo> getByUid(String guid) {
        return userInfoRepository.findByGuid(guid);
    }


    @Override
    public Optional<SysUserInfo> getByopenid(String openid) {
        return userInfoRepository.findByOpenid(openid);
    }

    /**
     * 添加用户信息
     */
    @Override
    @Transactional
    public SysUserInfo addUserInfo(SysUserInfo sysUserInfo) {
        SysUserInfo save = userInfoRepository.save(sysUserInfo);
        return save;
    }

    @Override
    @Transactional
    public void updateUserInfo(SysUserInfo sysUserInfo) {
        userInfoRepository.save(sysUserInfo);
    }

    @Override
    public SysUserInfo getByPhone(String phone) {
        System.out.println("UserInfoServiceImpl.findByPhone()");
        return userInfoRepository.findByPhone(phone);
    }

    @Override
    @Transactional
    public SysUserInfo updateSysUserInfo(SysUserInfo sysUserInfo) {
        Optional<SysUserInfo> sysUserInfoOpt = userInfoRepository.findByGuid(sysUserInfo.getGuid());
        if (sysUserInfoOpt.isPresent()) {
            SysUserInfo user = sysUserInfoOpt.get();
            if (sysUserInfo.getHeadimgurl() != null) {
                user.setHeadimgurl(sysUserInfo.getHeadimgurl());
            }
            if (sysUserInfo.getManifesto() != null) {
                user.setManifesto(sysUserInfo.getManifesto());
            }
            if (sysUserInfo.getOpenid() != null) {
                user.setOpenid(sysUserInfo.getOpenid());
            }
            if (sysUserInfo.getEmail() != null) {
                user.setEmail(sysUserInfo.getEmail());
            }
            if (sysUserInfo.getDirection() != null) {
                user.setDirection(sysUserInfo.getDirection());
            }
            if (sysUserInfo.getEducation() != null) {
                user.setEducation(sysUserInfo.getEducation());
            }
            if (sysUserInfo.getProfessiontag() != null) {
                user.setProfessiontag(sysUserInfo.getProfessiontag());
            }
            if (sysUserInfo.getPersontag() != null) {
                user.setPersontag(sysUserInfo.getPersontag());
            }
            if (sysUserInfo.getRealName() != null) {
                user.setRealName(sysUserInfo.getRealName());
            }

            if (sysUserInfo.getIdCard() != null) {
                user.setIdCard(sysUserInfo.getIdCard());
            }
            if (sysUserInfo.getQq() != null) {
                user.setQq(sysUserInfo.getQq());
            }

            user.setIsStu(sysUserInfo.getIsStu());

            if (sysUserInfo.getIdCard() != null) {
                user.setIdCard(sysUserInfo.getIdCard());
            }
            if (sysUserInfo.getQq() != null) {
                user.setQq(sysUserInfo.getQq());
            }
            if (sysUserInfo.getNoviceGuidanceSteps() != null) {
                user.setNoviceGuidanceSteps(sysUserInfo.getNoviceGuidanceSteps());
            }
            user.setLastModify(System.currentTimeMillis());
            SysUserInfo save = userInfoRepository.save(user);
            return save;
        }
        return new SysUserInfo();
    }

    @Override
    public int getDataSetCount(String guid) {

        return userInfoRepository.findAllByCreateId(guid);
    }


    @Override
    public int getSalonInfos(String userId) {

        return userInfoRepository.findAllByUserId(userId);
    }


    @Override
    public void deleteSysUserInfo(String id) {
        userInfoRepository.deleteById(id);
    }

    @Override
    public SysUserInfo save(SysUserInfo sysUserInfo) {
        return userInfoRepository.save(sysUserInfo);
    }

    @Override
    public Page<SysUserInfo> findSysUserInfoCriteria(Pageable pageable, String phone, Boolean isAdmin) {
        Page<SysUserInfo> SysUserInfoPage = userInfoRepository.findAll(new Specification<SysUserInfo>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<SysUserInfo> root, CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<Predicate>();
                if (null != phone && !"".equals(phone)) {
                    list.add(criteriaBuilder.equal(root.get("phone").as(String.class), phone));
                }
                if (null != isAdmin) {
                    list.add(criteriaBuilder.equal(root.get("isAdmin").as(Boolean.class), isAdmin));
                }
                Predicate[] p = new Predicate[list.size()];
                return criteriaBuilder.and(list.toArray(p));
            }
        }, pageable);
        return SysUserInfoPage;
    }

    @Override
    public boolean isJudgeByUserId(String userid) {
        return userInfoRepository.findJudgeByUserId(userid) == 0 ? false : true;
    }






    @Override
    @org.springframework.transaction.annotation.Transactional
    public int updateUserInfoByPhone(SysUserInfo sysUserInfo) {
        int i = -1;
        if (sysUserInfo != null && sysUserInfo.getGuid() != null) {
            Optional<SysUserInfo> userByGuid = userInfoRepository.findByGuid(sysUserInfo.getGuid());
            if (userByGuid.isPresent()) {
                SysUserInfo sysUser = userByGuid.get();
                i = userInfoRepository.updateUserInfo(sysUser.getGuid(), sysUser.getPassword(), sysUser.getSalt(), sysUser.getUsername());
            }
        }
        System.err.println(i);
        return i;
    }

    @Override
    public List<SysUserInfo> findUsers() {
        List<SysUserInfo> all = userInfoRepository.findAll();
        return all;
    }


    @Override
    public List<Map<String, Object>> getUserNounList(String guid, int page, int size) {
        List<Map<String, Object>> list = userInfoRepository.getUserNounList(guid, PageRequest.of(page, size));
        return list;
    }

    @Override
    public int getUserNounListCount(String guid) {
        int num = userInfoRepository.getUserNounListCount(guid);
        return num;
    }

    @Override
    public SysUserInfo getByNickName(String nickName) {
        return userInfoRepository.findByNicknameAndIsDeleted(nickName,0);
    }

    public static Map<String, Object> removeNullValue(Map<String, Object> map) {
        Set set = map.keySet();
        Map<String, Object> map1 = new HashMap<String, Object>();
        for (Iterator iterator = set.iterator(); iterator.hasNext(); ) {
            Object obj = iterator.next();
            Object value = map.get(obj);
            remove(value, iterator);
        }
        for (Iterator iterator = set.iterator(); iterator.hasNext(); ) {
            Object obj = iterator.next();
            Object value = map.get(obj);
            map1.put(obj.toString(), value);
        }
        return map1;
    }

    private static void remove(Object obj, Iterator iterator) {
        if (obj instanceof String) {
            String str = (String) obj;
            if (str == null || str.trim().isEmpty()) {
                iterator.remove();
            }
        } else if (obj instanceof Collection) {
            Collection col = (Collection) obj;
            if (col == null || col.isEmpty()) {
                iterator.remove();
            }

        } else if (obj instanceof Map) {
            Map temp = (Map) obj;
            if (temp == null || temp.isEmpty()) {
                iterator.remove();
            }

        } else if (obj instanceof Object[]) {
            Object[] array = (Object[]) obj;
            if (array == null || array.length <= 0) {
                iterator.remove();
            }
        } else {
            if (obj == null) {
                iterator.remove();
            }
        }
    }
}
