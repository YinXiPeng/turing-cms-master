package com.bonc.turing.cms.manage.repository;

import com.bonc.turing.cms.manage.entity.Notebook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author lky
 * @date 2019/7/20 11:53
 */
public interface NotebookRepository extends JpaRepository<Notebook, String>, JpaSpecificationExecutor<Notebook> {

    List<Notebook> findByParentIdOrderByVersion(String parentid);
}
