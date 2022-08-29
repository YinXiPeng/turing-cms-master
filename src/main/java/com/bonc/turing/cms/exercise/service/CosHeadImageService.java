package com.bonc.turing.cms.exercise.service;

import com.bonc.turing.cms.exercise.domain.CosHeadImage;

import java.util.List;

public interface CosHeadImageService {
    List getHeadImagesList(String guid, String schoolId);

    void setHeadImage(CosHeadImage cosHeadImage);

    void delHeadImage(String headImageId);

    void setSequence(CosHeadImage cosHeadImage);
}
