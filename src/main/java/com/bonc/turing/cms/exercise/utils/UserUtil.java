package com.bonc.turing.cms.exercise.utils;

import com.bonc.turing.cms.exercise.dao.repository.QtUserAndTextBookRepository;
import com.bonc.turing.cms.exercise.domain.QtUserAndTextBook;
import com.bonc.turing.cms.manage.entity.user.SysUserInfo;
import com.bonc.turing.cms.manage.repository.SysUserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserUtil {
    private static SysUserInfoRepository sysUserInfoRepository;

    private static QtUserAndTextBookRepository qtUserAndTextBookRepository;

    @Autowired
    public  void setSysUserInfoRepository(SysUserInfoRepository sysUserInfoRepository) {
        UserUtil.sysUserInfoRepository = sysUserInfoRepository;
    }

    @Autowired
    public  void setQtUserAndTextBookRepository(QtUserAndTextBookRepository qtUserAndTextBookRepository) {
        UserUtil.qtUserAndTextBookRepository = qtUserAndTextBookRepository;
    }

    /**
     * @param guid
     * @param textBookId
     * @return 1：普通用户 2：导师
     * @desc 课程-获取当前用户身份
     */
    public static int getUserType(String guid, String textBookId) {
        int userType = 0; //默认游客身份
        QtUserAndTextBook teacher = qtUserAndTextBookRepository.findByTextBookIdAndGuidAndRole(textBookId, guid,0);
        Optional<SysUserInfo> userInfo = sysUserInfoRepository.findById(guid);
       if (null!=teacher) {
            userType = 2;
        } else if (userInfo.isPresent()) {
            userType = 1;
        }
        return userType;
    }

    /**
     * 当前用户是否学习过
     * @param guid
     * @param textBookId
     * @return
     */
    public static int checkIsStudy(String guid, String textBookId){
        int isStudy = 0;
        QtUserAndTextBook textBook = qtUserAndTextBookRepository.findByTextBookIdAndGuid(textBookId,guid);
        if(null!=textBook){
            if(1==textBook.getRole()){ //为学生
                isStudy = 1;
            }
            else if(0==textBook.getRole()&&textBook.getNum()>0){ //作为导师，学习过
                isStudy = 1;
            }
        }
        return isStudy;
    }
}
