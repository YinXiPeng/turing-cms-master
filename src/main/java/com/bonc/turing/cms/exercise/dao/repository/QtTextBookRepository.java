package com.bonc.turing.cms.exercise.dao.repository;
import com.bonc.turing.cms.exercise.domain.QtTextBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface QtTextBookRepository extends JpaRepository<QtTextBook,String>,JpaSpecificationExecutor<QtTextBook> {

    @Query(value = "SELECT a.* FROM qt_text_book a \n" +
            "where a.is_deleted=0 \n" +
            "and a.id in (select product_id from order_info where user_id=:guid  and  status=1  ) \n" +
            "or a.id in (select text_book_id from  qt_user_and_text_book where guid = :guid) \n" +
            "group by  a.id \n" +
            "order by a.create_time",nativeQuery = true)
    List<QtTextBook> findAllByGuid(@Param("guid") String guid);

    QtTextBook findByBookNameAndIsDeleted(String bookName, int isDeleted);


    List<QtTextBook> findAllByIsDeletedAndStatusAndType(int isDeleted,int status,int type);

}
