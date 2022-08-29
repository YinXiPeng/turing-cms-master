package com.bonc.turing.cms.manage.repository;

import com.bonc.turing.cms.manage.entity.CmsSpecial;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

/**
 * @author lky
 * @date 2019/7/20 10:16
 */
public interface CmsSpecialRepository extends JpaRepository<CmsSpecial, Integer>, JpaSpecificationExecutor<CmsSpecial> {

    List<CmsSpecial> findBySpecialIdAndType(String special,int type);

    void  deleteBySpecialId(String specialId);

    @Query(value = "select * from (select n.id,n.news_title as newsTitle,n.news_desc as newsDesc,n.logo_url as logoUrl,n.news_type as newsType,n.news_content as newsContent,n.news_source as newsSource,n.news_author as newsAuthor,n.news_picture_url as newsPictureUrl,n.read_amount as readAmount,n.give_like as giveLike,n.create_time as createTime,n.update_time as updateTime,n.news_source_url as newsSourceUrl,n.key_word as keyWord,n.add_method as addMethod,ifnull(c.type,2) as type,ifnull(c.is_top,0) as isTop,ifnull(c.top_time,0) as topTime,ifnull(c.sort_method,0) as sortMethod,ifnull(c.is_elite,0) as isElite,ifnull(c.is_head,0) as isHead,ifnull(c.state,0) as state,ifnull(c.head_time,0) as headTime,ifnull(c.is_deleted,0) as isDeleted,ifnull(c.remark,'') as remark from news_info n left join cms_special c on n.id=c.special_id order by sortMethod DESC,topTime DESC,headTime DESC,updateTime DESC) a where a.isDeleted=0 and type=2 and (newsTitle like concat('%',:keyWord,'%') OR newsAuthor like concat('%',:keyWord,'%') OR keyWord like concat('%',:keyWord,'%'))",nativeQuery = true)
    List<Map<String,Object>> getNewsList(@Param(value = "keyWord") String keyWord, Pageable pageRequest);

    @Query(value = "SELECT a.id, a.title, a.content,a.remark,a.sorting,a.excellent,a.top,a.show_hide as showHide, b.username  FROM  discuss a LEFT JOIN sys_user_info b ON a.user_id = b.guid  where  a.refer_id in (select salon_id from salon ) AND a.show_hide = 0 and a.title like concat('%',:keyWord,'%')",nativeQuery = true)
    List<Map<String,Object>> getSalonList(@Param(value = "keyWord") String keyWord,Pageable pageRequest);

    @Query(value = "SELECT N.notebook_id notebookId,N.guid,N.notebook_description notebookDescription,N.notebook_path notebookPath,N.is_public isPublic,N.notebook_name notebookName,N.execute_time executeTime,N.parent_id parentId,UNIX_TIMESTAMP( N.create_time ) * 1000 createTime,UNIX_TIMESTAMP( N.last_modified ) * 1000 lastModified,countbro,N.countfork,N.type_id typeId,N.kernel_id kernelId,N.env_id envId,N.img_num imgNum,N.img img,N.code_num codeNum,( N.countbro * 0.3 + N.countfork * 0.7 ) AS num,ifnull( c.type, 0 ) AS type,ifnull( c.top_time, 0 ) AS topTime,ifnull( c.state, 0 ) AS state,ifnull( c.sort_method, 0 ) sortMethod,ifnull( c.remark, '' ) AS remark,ifnull( c.modify_id, '0' ) AS modifyId,ifnull( c.is_top, 0 ) AS isTop,ifnull( c.is_head, 0 ) AS isHead,ifnull( c.is_elite, 0 ) AS isElite,ifnull( c.is_deleted, 0 ) AS isDeleted,ifnull( c.head_time, 0 ) AS headTime FROM (SELECT C.*,COUNT( D.browse_id ) countbro FROM (SELECT A.*,COUNT( B.fork_id ) countfork FROM (SELECT nb.notebook_id,nb.guid,nb.notebook_description,nb.notebook_path,nb.is_public,nb.notebook_name,nb.create_time,nb.last_modified,nb.type_id,nb.kernel_id,nb.execute_time,nb.parent_id,nb.env_id,nb.img_num,nb.img,nb.code_num FROM notebook nb LEFT JOIN (SELECT parent_id,count( * ) version_num FROM notebook n WHERE n.parent_id != '' AND n.is_delete = '1' AND n.is_public = '0' GROUP BY n.parent_id ) t ON nb.parent_id = t.parent_id WHERE nb.version = t.version_num AND nb.is_delete = '1' and nb.img IS NOT NULL and (notebook_name like concat('%',:keyWord,'%') OR notebook_description like concat('%',:keyWord,'%'))) A LEFT JOIN notebook_fork B ON B.notebook_id = A.notebook_id GROUP BY A.notebook_id ) C LEFT JOIN notebook_browse D ON D.notebook_id = C.notebook_id GROUP BY C.notebook_id,C.countfork ) N LEFT JOIN cms_special c ON N.parent_id = c.special_id ORDER BY sortMethod DESC,topTime DESC,num DESC",nativeQuery = true)
    List<Map<String,Object>> getModelList(@Param(value = "keyWord") String keyWord, Pageable pageRequest);

    @Query(value = "select count(*) from (select n.id,n.news_title as newsTitle,n.news_desc as newsDesc,n.logo_url as logoUrl,n.news_type as newsType,n.news_content as newsContent,n.news_source as newsSource,n.news_author as newsAuthor,n.news_picture_url as newsPictureUrl,n.read_amount as readAmount,n.give_like as giveLike,n.create_time as createTime,n.update_time as updateTime,n.news_source_url as newsSourceUrl,n.key_word as keyWord,n.add_method as addMethod,ifnull(c.type,2) as type,ifnull(c.is_top,0) as isTop,ifnull(c.top_time,0) as topTime,ifnull(c.sort_method,0) as sortMethod,ifnull(c.is_elite,0) as isElite,ifnull(c.is_head,0) as isHead,ifnull(c.state,0) as state,ifnull(c.head_time,0) as headTime,ifnull(c.is_deleted,0) as isDeleted,ifnull(c.remark,'') as remark from news_info n left join cms_special c on n.id=c.special_id order by sortMethod DESC,topTime DESC,headTime DESC,updateTime DESC) a where a.isDeleted=0 and type=2 and (newsTitle like concat('%',:keyWord,'%') OR newsAuthor like concat('%',:keyWord,'%') OR keyWord like concat('%',:keyWord,'%'))",nativeQuery = true)
    int getCountNewsList(@Param(value = "keyWord")String keyWord);

    @Query(value = "SELECT count(a.id)  FROM  discuss a LEFT JOIN sys_user_info b ON a.user_id = b.guid  where  a.refer_id in (select salon_id from salon ) AND a.show_hide = 0 and a.title like concat('%',:keyWord,'%')",nativeQuery = true)
    int getCountSalonList(@Param(value = "keyWord")String keyWord);

    @Query(value = "SELECT count(N.notebook_id) FROM (SELECT C.*,COUNT( D.browse_id ) countbro FROM (SELECT A.*,COUNT( B.fork_id ) countfork FROM (SELECT nb.notebook_id,nb.guid,nb.notebook_description,nb.notebook_path,nb.is_public,nb.notebook_name,nb.create_time,nb.last_modified,nb.type_id,nb.kernel_id,nb.execute_time,nb.parent_id,nb.env_id,nb.img_num,nb.img,nb.code_num FROM notebook nb LEFT JOIN (SELECT parent_id,count( * ) version_num FROM notebook n WHERE n.parent_id != '' AND n.is_delete = '1' AND n.is_public = '0' GROUP BY n.parent_id ) t ON nb.parent_id = t.parent_id WHERE nb.version = t.version_num AND nb.is_delete = '1' and nb.img IS NOT NULL and (notebook_name like concat('%',:keyWord,'%') OR notebook_description like concat('%',:keyWord,'%'))) A LEFT JOIN notebook_fork B ON B.notebook_id = A.notebook_id GROUP BY A.notebook_id ) C LEFT JOIN notebook_browse D ON D.notebook_id = C.notebook_id GROUP BY C.notebook_id,C.countfork ) N LEFT JOIN cms_special c ON N.parent_id = c.special_id ",nativeQuery = true)
    int getCountModelList(@Param(value = "keyWord")String keyWord);


}
