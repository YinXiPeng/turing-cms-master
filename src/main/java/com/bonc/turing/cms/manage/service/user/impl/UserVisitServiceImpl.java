package com.bonc.turing.cms.manage.service.user.impl;

import com.bonc.turing.cms.manage.entity.user.UserVisit;
import com.bonc.turing.cms.manage.repository.UserVisitRepository;
import com.bonc.turing.cms.manage.service.user.UserVisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserVisitServiceImpl implements UserVisitService {
    @Autowired
    private UserVisitRepository userVisitRepository;


    @Override
    public void findByGuid(String guid) {
        Optional<UserVisit> byGuid = userVisitRepository.findByGuid(guid);
        if (!byGuid.isPresent()) {
            userVisitRepository.save(new UserVisit(guid));
        }
    }

}
