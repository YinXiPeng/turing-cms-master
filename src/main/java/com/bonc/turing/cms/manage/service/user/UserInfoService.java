package com.bonc.turing.cms.manage.service.user;


import com.bonc.turing.cms.manage.entity.user.SysUserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author liuyunkai
 * @date 2018-11-6 10:40
 */
public interface UserInfoService {



    /**通过username查找用户信息;用户身份验证*/
    SysUserInfo getByUsername(String username);
    /**通过guid查找用户信息;*/
    Optional<SysUserInfo> getByUid(String guid);
    /**通过openid查找用户信息;*/
    Optional<SysUserInfo> getByopenid(String openid);
    /**新增用户 */
    SysUserInfo addUserInfo(SysUserInfo sysUserInfo);
    /**更新用户 */
    void updateUserInfo(SysUserInfo sysUserInfo);
    /**通过phone查找用户信息;*/
    SysUserInfo getByPhone(String phone);
    /**
     * 查询我的数据集个数
     * @return JSONObject
     */
    int getDataSetCount(String guid);

    /**
     * 查询我的沙龙
     * @return JSONObject
     */
    int getSalonInfos(String userId);




    /**
     * 修改个人信息
     * @param
     */
    SysUserInfo updateSysUserInfo(SysUserInfo sysUserInfo);


    /**
     * 用户列表
     * @param pageable
     * @param phone
     * @param isAdmin
     * @return
     */
    Page<SysUserInfo> findSysUserInfoCriteria(Pageable pageable, String phone, Boolean isAdmin);

    /**
     * 删除用户
     * @param id
     */
    void deleteSysUserInfo(String id);

    /**
     * 保存管理员用户
     * @param sysUserInfo
     * @return
     */
    SysUserInfo save(SysUserInfo sysUserInfo);

    /**
     * 是否评委
     * @param userid
     * @return
     */
    boolean  isJudgeByUserId(String userid);



    /**
     * 更新用户信息
     * @param sysUserInfo
     */
    int updateUserInfoByPhone(SysUserInfo sysUserInfo);


    List<SysUserInfo> findUsers();


    List<Map<String,Object>> getUserNounList(String guid, int page, int size);

    int getUserNounListCount(String guid);

    SysUserInfo getByNickName(String nickName);
}
