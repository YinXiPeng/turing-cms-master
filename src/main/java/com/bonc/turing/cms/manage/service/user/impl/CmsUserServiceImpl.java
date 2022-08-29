package com.bonc.turing.cms.manage.service.user.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bonc.turing.cms.common.utils.PageInfoEntity;
import com.bonc.turing.cms.enums.BCreateCourseEnum;
import com.bonc.turing.cms.manage.entity.notify.NotifySetting;
import com.bonc.turing.cms.manage.entity.user.CmsPermissions;
import com.bonc.turing.cms.manage.entity.user.CmsUserInfo;
import com.bonc.turing.cms.manage.entity.user.SysUserInfo;
import com.bonc.turing.cms.manage.mapper.user.UserMapper;
import com.bonc.turing.cms.manage.repository.CmsPermissionsRepository;
import com.bonc.turing.cms.manage.repository.CmsUserInfoRepository;
import com.bonc.turing.cms.manage.repository.NotifySettingRepository;
import com.bonc.turing.cms.manage.repository.UserInfoRepository;
import com.bonc.turing.cms.manage.service.user.CmsUserService;
import com.bonc.turing.cms.manage.service.user.UserInfoService;
import com.bonc.turing.cms.user.bean.UserPoint;
import com.bonc.turing.cms.user.dao.repository.UserPointRepository;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CmsUserServiceImpl implements CmsUserService {

    public static String[] chars = new String[]{"a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};


    @Autowired
    private CmsUserInfoRepository cmsUserInfoRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private CmsPermissionsRepository cmsPermissionsRepository;
    @Autowired
    private NotifySettingRepository notifySettingRepository;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserPointRepository userPointRepository;
    @Autowired
    private UserMapper userMapper;

    @Value("${tob.pre}")
    private String tobNamePre;
    @Value("${toc.pre}")
    private String tocNamePre;

    @Override
    public CmsUserInfo getByUsername(String username) {
        List<CmsUserInfo> byUserName = cmsUserInfoRepository.findByUserName(username);
        CmsUserInfo cmsUserInfo = null;
        if (0 != byUserName.size()) {
            cmsUserInfo = byUserName.get(0);
        }
        return cmsUserInfo;
    }

    @Override
    public Optional<CmsUserInfo> getByGuid(String guid) {

        Optional<CmsUserInfo> byId = cmsUserInfoRepository.findById(guid);
        return byId;
    }

    @Transactional
    @Override
    public void addUserInfo(CmsUserInfo cmsUserInfo,SysUserInfo sysUserInfo) {
        NotifySetting notifySetting = new NotifySetting();
        notifySetting.setGuid(cmsUserInfo.getGuid());
        notifySettingRepository.save(notifySetting);
        CmsUserInfo save = cmsUserInfoRepository.save(cmsUserInfo);
        SysUserInfo save1 = userInfoRepository.save(sysUserInfo);
    }

    @Transactional
    @Override
    public void updateUserInfo(CmsUserInfo cmsUserInfo, SysUserInfo sysUserInfo) {
        cmsUserInfoRepository.save(cmsUserInfo);
        userInfoRepository.save(sysUserInfo);
    }

    @Override
    public Optional<CmsUserInfo> findByGuid(String guid) {
        Optional<CmsUserInfo> byId = cmsUserInfoRepository.findById(guid);
        return byId;
    }

    @Override
    public Map findAll(int type, String keyWord, int pageNum, int pageSize) {
        List userList = new ArrayList();
        long total = 0;
        HashMap<Object, Object> map = new HashMap<>();
        if (1 == type) {
            //查超级管理员
            PageRequest pageable = null;
            if (0 == pageSize) {
                //默认大小,不进行分页
            } else {
                pageable = new PageRequest(pageNum - 1, pageSize);
            }
            List<CmsUserInfo> all = null;
            if (null == pageable) {
                all = cmsUserInfoRepository.findByKeyWordAndRole(keyWord, 2);
                total = all.size();
            } else {
                Page<CmsUserInfo> byKeyWordAndRole = cmsUserInfoRepository.findByKeyWordAndRole(keyWord, 2, pageable);
                all = byKeyWordAndRole.getContent();
                total = byKeyWordAndRole.getTotalElements();
            }
            for (CmsUserInfo cmsUserInfo : all) {
                cmsUserInfo.setPassword(null);
                cmsUserInfo.setSalt(null);
            }
            userList = all;
        } else if (0 == type) {
            //查普通管理员
            PageRequest pageable = null;
            if (0 == pageSize) {
                //默认大小,不进行分页
            } else {
                pageable = new PageRequest(pageNum - 1, pageSize);
            }
            List<CmsUserInfo> all = null;
            if (null == pageable) {
                all = cmsUserInfoRepository.findByKeyWordAndRole(keyWord, 1);
                total = all.size();
            } else {
                Page<CmsUserInfo> byKeyWordAndRole = cmsUserInfoRepository.findByKeyWordAndRole(keyWord, 1, pageable);
                all = byKeyWordAndRole.getContent();
                total = byKeyWordAndRole.getTotalElements();
            }
            for (CmsUserInfo cmsUserInfo : all) {
                List<CmsPermissions> permissions = cmsUserInfo.getPermissions();
                for (int i = permissions.size() - 1; i >= 0; i--) {
                    CmsPermissions cmsPermissions = permissions.get(i);
                    if (0 != cmsPermissions.getParentsPermissions()) {
                        permissions.remove(i);
                    }
                }
                //密码和盐不返回
                cmsUserInfo.setPassword(null);
                cmsUserInfo.setSalt(null);
            }
            userList = all;
        } else if (2 == type) {
            //查普通用户
            PageRequest pageable = null;
            if (0 == pageSize) {
                //默认大小,不进行分页
            } else {
                Sort sort = new Sort(Sort.Direction.ASC, "registts");
                pageable = new PageRequest(pageNum - 1, pageSize, sort);
            }
            List<SysUserInfo> all = null;
            ArrayList<Object> objects = new ArrayList<>();
            if (null == pageable) {
                all = userInfoRepository.findByUsernameOrPhone(keyWord);
                total = all.size();
            } else {
                Page<SysUserInfo> pageResult = userInfoRepository.findByUsernameOrPhone(keyWord, pageable);
                all = pageResult.getContent();
                total = pageResult.getTotalElements();
            }
            for (SysUserInfo sysUserInfo : all) {
                UserPoint byUserId = userPointRepository.findByUserId(sysUserInfo.getGuid());
                sysUserInfo.setPassword(null);
                sysUserInfo.setSalt(null);
                JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(sysUserInfo));
                if (null != byUserId) {
                    jsonObject.put("credit", byUserId.getCredit());
                    jsonObject.put("point", byUserId.getPoint());
                } else {
                    jsonObject.put("credit", 0);
                    jsonObject.put("point", 0);
                }
                objects.add(jsonObject);
            }
            userList = objects;
        }
        map.put("userList", userList);
        map.put("total", total);
        return map;
    }


    @Transactional
    @Override
    public void deleteByGuid(String userId, String guid) {
        Optional<CmsUserInfo> byId = cmsUserInfoRepository.findById(userId);
        if (byId.isPresent()) {
            CmsUserInfo cmsUserInfo = byId.get();
            cmsUserInfo.setIsDeleted(1);
            cmsUserInfo.setLastModifyTime(new Date());
            cmsUserInfo.setLastModifyId(guid);
            cmsUserInfoRepository.save(cmsUserInfo);
            userInfoRepository.deleteByGuid(cmsUserInfo.getGuid());
        }
    }

    @Override
    public List<CmsPermissions> getAllPermissions() {
        List<CmsPermissions> all = cmsPermissionsRepository.findAllParent();
        return all;
    }

    @Override
    public Optional<SysUserInfo> findSysByGuid(String id) {
        Optional<SysUserInfo> byGuid = userInfoRepository.findByGuid(id);
        return byGuid;
    }

    @Override
    public SysUserInfo getSysByUsername(String userName) {
        SysUserInfo byUsername = userInfoRepository.findByUsername(userName);
        return byUsername;
    }

    @Override
    public SysUserInfo findByPhone(String phone) {
        SysUserInfo byPhone = userInfoRepository.findByPhone(phone);
        return byPhone;
    }

    @Override
    public CmsUserInfo getByNickname(String username) {
        return cmsUserInfoRepository.findByNicknameAndIsDeleted(username, 0);
    }

    /**
     * tob 的用户名 确保生成的昵称是唯一的
     *
     * @return
     */
    @Override
    public String getTobUsername() {
        String s = generateShortUuid();
        String username = tobNamePre + s;
        SysUserInfo sysUserInfo = userInfoService.getByUsername(username);
        if (null == sysUserInfo) {
            return username;
        } else {
            return getTobUsername();
        }
    }

    @Override
    public boolean updateUserCredit(JSONObject jsonObject) {
        String userId = jsonObject.getString("userId");
        Integer remainCredit = jsonObject.getInteger("remainCredit");
        UserPoint byUserId = userPointRepository.findByUserId(userId);
        if (null == byUserId) {
            byUserId = new UserPoint();
            byUserId.setUserId(userId);
        }
        byUserId.setCredit(remainCredit);
        userPointRepository.save(byUserId);
        return true;
    }

    @Override
    public PageInfoEntity getUserListOfB(String keyWord, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Map> userList = userMapper.getUserListOfB(keyWord);
        for (Map user:userList){
            String guid = (String) user.get("guid");
            List<Integer> byGuid = cmsPermissionsRepository.findByGuid(guid);
            ArrayList<Integer> strings = new ArrayList<>();
            if (byGuid.contains(BCreateCourseEnum.CODE_3001.getRoleCode())){
                strings.add(BCreateCourseEnum.CODE_3001.getRoleCode());
            }
            if (byGuid.contains(BCreateCourseEnum.CODE_3002.getRoleCode())){
                strings.add(BCreateCourseEnum.CODE_3002.getRoleCode());
            }
            user.put("permissions",strings);
        }
        PageInfo<Map> objectPageInfo = new PageInfo<Map>(userList);
        PageInfoEntity pageInfoEntity = new PageInfoEntity(objectPageInfo);
        return pageInfoEntity;
    }

    @Transactional
    @Override
    public void updateUserPermissions(JSONObject jsonObject) {
        String userId = jsonObject.getString("userId");
        JSONArray permissions = jsonObject.getJSONArray("permissions");
        cmsPermissionsRepository.deleteByGuidAndPermissionId(userId,BCreateCourseEnum.CODE_3001.getRoleCode());
        cmsPermissionsRepository.deleteByGuidAndPermissionId(userId,BCreateCourseEnum.CODE_3002.getRoleCode());
        for (Object permission:permissions){
            if (permission.equals(BCreateCourseEnum.CODE_3001.getRoleCode())){
                cmsPermissionsRepository.addPermission(userId,BCreateCourseEnum.CODE_3001.getRoleCode());
            }
            if (permission.equals(BCreateCourseEnum.CODE_3002.getRoleCode())){
                cmsPermissionsRepository.addPermission(userId,BCreateCourseEnum.CODE_3002.getRoleCode());
            }
        }
    }

    @Override
    public List getUserPermissions(String guid) {
        return cmsPermissionsRepository.findByGuid(guid);
    }

    /**
     * toc 的用户名 确保生成的昵称是唯一的
     *
     * @return
     */
    @Override
    public String getTocUsername() {
        String s = generateShortUuid();
        String username = tocNamePre + s;
        SysUserInfo sysUserInfo = userInfoService.getByUsername(username);
        if (null == sysUserInfo) {
            return username;
        } else {
            return getTocUsername();
        }
    }


    /**
     * @return
     * @Author lky
     * @Description 生成8位短随机数
     * @Date 19:16 2020/4/14
     * @Param
     **/
    public static String generateShortUuid() {
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();

    }


}
