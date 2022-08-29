package com.bonc.turing.cms.exercise.dao.repository;

import com.bonc.turing.cms.exercise.domain.HrCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author lky
 * @desc
 * @date 2020/2/21 11:00
 */
public interface HrCompanyRepository extends JpaRepository<HrCompany, String>, JpaSpecificationExecutor<HrCompany> {

    /**
     * 判断公司名称是否重复,排除自己
     *
     * @param companyName
     * @param companyId
     * @return
     */
    @Query("select hc from HrCompany hc where hc.companyName = :companyName and hc.companyId <> :companyId")
    List<HrCompany> findByCompanyNameNotSelf(@Param("companyName") String companyName, @Param("companyId") String companyId);
}
