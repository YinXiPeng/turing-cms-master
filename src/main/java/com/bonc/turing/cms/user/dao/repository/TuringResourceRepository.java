package com.bonc.turing.cms.user.dao.repository;

import com.bonc.turing.cms.user.bean.TuringResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @desc 上传资源dao
 * @author yh
 * @date 2020.07.08
 */
public interface TuringResourceRepository extends JpaRepository<TuringResource,String>, JpaSpecificationExecutor<TuringResource> {




}
