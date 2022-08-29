package com.bonc.turing.cms.manage.repository;

import com.bonc.turing.cms.manage.entity.OrderInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Auther: LY
 * @Data: 2019/4/13
 * @Description
 */
@Transactional
public interface OrderInfoRepository extends JpaRepository<OrderInfo, String>, JpaSpecificationExecutor<OrderInfo> {

    List<OrderInfo> findByProductId(String productId);
}
