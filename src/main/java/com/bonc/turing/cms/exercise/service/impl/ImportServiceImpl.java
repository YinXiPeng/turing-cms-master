package com.bonc.turing.cms.exercise.service.impl;

import com.bonc.turing.cms.common.utils.ImportExcelUtil;
import com.bonc.turing.cms.exercise.dao.repository.CosUserMsgRepository;
import com.bonc.turing.cms.exercise.domain.CosUserMsg;
import com.bonc.turing.cms.exercise.service.ImportService;
import com.bonc.turing.cms.manage.controller.login.RegisterController;
import com.bonc.turing.cms.manage.entity.user.SysUserInfo;
import com.bonc.turing.cms.manage.mapper.user.UserMapper;
import com.bonc.turing.cms.manage.repository.SysUserInfoRepository;
import com.bonc.turing.cms.manage.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ImportServiceImpl implements ImportService {

    private static String[] table = {"学校","学院","姓名","工号/学号","角色","手机号","邮箱","密码"};

    @Autowired
    private CosUserMsgRepository cosUserMsgRepository;
    @Autowired
    private SysUserInfoRepository sysUserInfoRepository;
    @Autowired
    private RegisterController registerController;
    @Autowired
    private UserMapper userMapper;

    @Value("${teacher.photo.url}")
    private String teacherPhoto;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String importPoi(MultipartFile file) throws Exception {
        String originalFilename = file.getOriginalFilename();
        InputStream inputStream = file.getInputStream();
        List<List<Object>> bankListByExcel = ImportExcelUtil.getBankListByExcel(inputStream, originalFilename);
        //检查并导入库
        String s = checkAndImportMsg(bankListByExcel);
        return s;
    }

    private String checkAndImportMsg(List<List<Object>> bankListByExcel) {
        for (int i=0;i<bankListByExcel.size();i++){
            List<Object> objects = bankListByExcel.get(i);
            if (0==i){
                for (int m=0;m<table.length;m++){
                    Object o = objects.get(m);
                    if (null==o){
                        return "列名为空(第"+m+"列名应为"+table[m]+")";
                    }
                    if (!table[m].equals(o)){
                        return "列名为错误(第"+m+"列名应为"+table[m]+")";
                    }
                }
            }else {
                SysUserInfo byPhone = sysUserInfoRepository.findByPhone(objects.get(5).toString());
                if (null==byPhone){
                    //注册
                    SysUserInfo sysUserInfo = registerController.registUserForSchool(objects.get(2).toString(),objects.get(5).toString(),objects.get(6).toString(),objects.get(7).toString());
                    //新的用户
                    addMsgAndSave(objects,sysUserInfo.getGuid());

                }else {
                    //已有用户
                    addMsgAndSave(objects,byPhone.getGuid());
                }
            }
        }
        return "success";
    }

    //在学生老师信息表中加信息
    private void addMsgAndSave(List<Object> objects, String guid) {
        List<CosUserMsg> byPhone = cosUserMsgRepository.findByPhone(objects.get(5).toString());
        CosUserMsg cosUserMsg;
        if (null == byPhone||0==byPhone.size()){
            //信息表中没有改用户
            cosUserMsg = new CosUserMsg();
            cosUserMsg.setCreatedTime(new Date());
        }else {
            cosUserMsg = byPhone.get(0);
        }
        String s = objects.get(0).toString();
        cosUserMsg.setSchoolName(s);
        //看是否有该学校
        List<CosUserMsg> bySchoolName = cosUserMsgRepository.findBySchoolName(s);
        String schoolId;
        if (null==bySchoolName||0==bySchoolName.size()){
            UUID uuid = UUID.randomUUID();
            schoolId = uuid.toString().replaceAll("-","");
        }else {
            schoolId = bySchoolName.get(0).getSchoolId();
        }
        cosUserMsg.setSchoolId(schoolId);
        cosUserMsg.setCollegeName(objects.get(1).toString());
        cosUserMsg.setPeopleId(objects.get(3).toString());
        cosUserMsg.setPhone(objects.get(5).toString());
        cosUserMsg.setMail(objects.get(6).toString());
        String s1 = objects.get(4).toString();
        if ("学生".equals(s1)){
            cosUserMsg.setRole(0);
        }else if ("老师".equals(s1)){
            cosUserMsg.setRole(1);
           userMapper.insertPermission(guid,3001);
        }
        cosUserMsg.setGuid(guid);
        cosUserMsg.setRealName(objects.get(2).toString());
        cosUserMsg.setModifyTime(new Date());
        cosUserMsg.setPhoto(teacherPhoto);
        cosUserMsgRepository.save(cosUserMsg);
    }
}
