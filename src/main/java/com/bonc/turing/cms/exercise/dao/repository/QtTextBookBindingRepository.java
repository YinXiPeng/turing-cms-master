package com.bonc.turing.cms.exercise.dao.repository;

import com.bonc.turing.cms.exercise.domain.QtTextBookBinding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author lky
 * @desc
 * @date 2019/12/26 19:08
 */
public interface QtTextBookBindingRepository extends JpaRepository<QtTextBookBinding,Integer>,JpaSpecificationExecutor<QtTextBookBinding> {

    List<QtTextBookBinding> findByPagingIdAndType(Integer pagingId, Integer type);

    int deleteByTypeAndPagingId(Integer type, Integer pagingId);

    int deleteByPagingId(Integer pagingId);

    int deleteByTitleNumberAndType(String titleNumber, Integer type);
}
