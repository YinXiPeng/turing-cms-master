package com.bonc.turing.cms.manage.service.user;


import com.bonc.turing.cms.manage.entity.user.UserVisit;

public interface UserVisitService {
    /**通过guid查找信息;*/
    void findByGuid(String guid);

}
