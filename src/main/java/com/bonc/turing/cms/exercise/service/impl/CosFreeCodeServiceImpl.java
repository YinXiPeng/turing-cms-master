package com.bonc.turing.cms.exercise.service.impl;

import com.bonc.turing.cms.common.utils.StringUtils;
import com.bonc.turing.cms.exercise.dao.repository.CosFreeCodeRepository;
import com.bonc.turing.cms.exercise.domain.CosFreeCode;
import com.bonc.turing.cms.exercise.service.CosFreeCodeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author yh
 * @Description 后台-生成课程兑换码
 * @Date  2020/4/17 17:54
 **/
@Service
public class CosFreeCodeServiceImpl implements CosFreeCodeService {

    @Autowired
    private CosFreeCodeRepository cosFreeCodeRepository;

    /**
     * 创建兑换码,并返回创建的code
     * @param courseId
     * @param num
     */
    @Override
    public Object createFreeCode(String courseId, Integer num) {
        List<String> codeList = new ArrayList<>();
        for (int i = 0; i < num; i++) {
             String code = StringUtils.generateShortUUid();
             CosFreeCode cosFreeCode = cosFreeCodeRepository.findByCourseIdAndCode(courseId,code);
             if(null==cosFreeCode){
                 CosFreeCode freeCode = new CosFreeCode();
                 freeCode.setCode(code);
                 freeCode.setCreateTime(new Date());
                 freeCode.setCourseId(courseId);
                 CosFreeCode save = cosFreeCodeRepository.save(freeCode);
                 codeList.add(save.getCode());
             }
        }
        return codeList;
    }

    /**
     * 显示已有兑换码名称
     * @param courseId
     * @return
     */
    @Override
    public Object showFreeCodeByCourseId(String courseId) {
        List<CosFreeCode> freeCodeList = cosFreeCodeRepository.findAllByCourseId(courseId);
        return freeCodeList;
    }
}
