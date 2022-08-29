package com.bonc.turing.cms.manage.repository;

import com.bonc.turing.cms.manage.entity.OrderInfoUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @Auther:lcd
 * @Data:2019/4/10
 * @Description
 */
public interface OrderUserRepository extends JpaRepository<OrderInfoUser, String>, JpaSpecificationExecutor<OrderInfoUser> {

    @Query(value = "select n from OrderInfoUser n where n.userId =?1")
    OrderInfoUser queryOrderInfoUserByUserId(@Param("userId") String userId);

}
