package com.bonc.turing.cms.exercise.dao.repository;

import com.bonc.turing.cms.exercise.domain.HrDictionary;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @author lky
 * @desc
 * @date 2020/2/21 11:01
 */
public interface HrDictionaryRepository extends PagingAndSortingRepository<HrDictionary, String> {

    /**
     * 查询公司规模和公司类型
     *
     * @return
     */
    @Query("select hd from HrDictionary hd where hd.dictionaryName = '公司规模' or hd.dictionaryName = '公司类型' ")
    List<HrDictionary> findCompanySizeAndType();

    List<HrDictionary> findByDictionaryName(String dictionaryName);
}
