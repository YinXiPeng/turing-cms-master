package com.bonc.turing.cms.exercise.dao.mapper;

import com.bonc.turing.cms.exercise.domain.CosAnswerRecord;
import com.bonc.turing.cms.exercise.dto.CosAnswerRecordsDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 用户答题记录
 * @author lxm
 */
@Mapper
public interface CosAnswerRecordMapper {


    /**
     * 根据试卷id 获取所有的答题记录
     * @param paperId
     * @param statusArr
     * @return
     */
    @Select({
            "<script>",
            "select id, paper_name paperName,name,student_id studentId,score,`status` from cos_answer_record ",
            "where paper_id = #{paperId}",
            "and status in",
            "<foreach collection='statusArr' open='(' item='status' separator=',' close=')'> #{status}</foreach>",
            "ORDER BY `status`,score desc",
            "</script>"
    })
   List<CosAnswerRecordsDTO> getAllCosAnswerRecord(@Param("paperId") String paperId,@Param("statusArr") Integer[] statusArr);

    /**
     *  根据试卷id 查询对应的答题记录
     * @param paperId
     * @return
     */
    @Select("select * from  cos_answer_record where paper_id = #{paperId}")
    CosAnswerRecord getCosPaperById(String paperId);

    /**
     * 根据id 更新成绩和状态 还有评语
     * @param score
     * @param id
     * @param comment
     * @return
     */
    @Update("update cos_answer_record set score = #{score},comment = #{comment},status=3 where id = #{id}")
    int updateScoreAndStatusByAnswerRecordId(@Param("score") Float score,@Param("id") String id,@Param("comment") String comment);

}
