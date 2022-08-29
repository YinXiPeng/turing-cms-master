package com.bonc.turing.cms.exercise.service.impl;

import com.bonc.turing.cms.exercise.dao.repository.CosHeadImageRepository;
import com.bonc.turing.cms.exercise.dao.repository.CosUserMsgRepository;
import com.bonc.turing.cms.exercise.domain.CosHeadImage;
import com.bonc.turing.cms.exercise.domain.CosUserMsg;
import com.bonc.turing.cms.exercise.service.CosHeadImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CosHeadImageServiceImpl implements CosHeadImageService {

    @Autowired
    private CosHeadImageRepository cosHeadImageRepository;
    @Autowired
    private CosUserMsgRepository cosUserMsgRepository;

    @Override
    public List getHeadImagesList(String guid, String schoolId) {
        List<CosHeadImage> headImageList;
        headImageList = cosHeadImageRepository.findBySchoolIdOrderByCreatedTimeDesc(schoolId);
        return headImageList;
    }

    @Override
    public void setHeadImage(CosHeadImage cosHeadImage) {
        String id = cosHeadImage.getId();
        if (null==id||"".equals(id)){
            cosHeadImage.setCreatedTime(new Date());
        }else {
            Optional<CosHeadImage> byId = cosHeadImageRepository.findById(id);
            if (byId.isPresent()){
                CosHeadImage cosHeadImage1 = byId.get();
                cosHeadImage.setSequence(cosHeadImage1.getSequence());
                cosHeadImage.setCreatedTime(cosHeadImage1.getCreatedTime());
            }
        }
        cosHeadImageRepository.save(cosHeadImage);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void delHeadImage(String headImageId) {
//        String schoolId = "";
//        Optional<CosHeadImage> byId = cosHeadImageRepository.findById(headImageId);
//        if (byId.isPresent()){
//            CosHeadImage cosHeadImage = byId.get();
//            schoolId = cosHeadImage.getSchoolId();
//        }
        cosHeadImageRepository.deleteById(headImageId);
//        //
//        List<CosHeadImage> cosHeadImages = cosHeadImageRepository.findBySchoolIdAndOrderNotOrderByOrder(schoolId,-1);
//        for (int i = 1;i<=cosHeadImages.size();i++){
//            if (i!=cosHeadImages.get(i).getOrder()){
//                CosHeadImage cosHeadImage = cosHeadImages.get(i);
//                cosHeadImage.setOrder(i);
//                cosHeadImageRepository.save(cosHeadImage);
//            }
//        }
    }

    @Override
    public void setSequence(CosHeadImage cosHeadImage) {
        Optional<CosHeadImage> byId = cosHeadImageRepository.findById(cosHeadImage.getId());
        if (byId.isPresent()){
            CosHeadImage cosHeadImage1 = byId.get();
            cosHeadImage1.setSequence(cosHeadImage.getSequence());
            cosHeadImageRepository.save(cosHeadImage1);
        }
    }
}
