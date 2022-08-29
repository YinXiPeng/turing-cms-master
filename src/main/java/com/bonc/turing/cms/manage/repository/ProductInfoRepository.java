package com.bonc.turing.cms.manage.repository;


import com.bonc.turing.cms.manage.entity.ProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Auther: LY
 * @Data: 2019/4/13
 * @Description
 */
@Transactional
public interface ProductInfoRepository extends JpaRepository<ProductInfo, String>, JpaSpecificationExecutor<ProductInfo> {

    ProductInfo findByProductName(String productName);


}
