package com.bonc.turing.cms.manage.repository;

import com.bonc.turing.cms.manage.entity.OrderTry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author lky
 * @date 2019/7/19 14:46
 */
public interface OrderTryRepository extends JpaRepository<OrderTry, String>, JpaSpecificationExecutor<OrderTry> {

    List<OrderTry> findByGuidAndProductId(String guid, String productId);

    List<OrderTry> findByProductId(String productId);
}
