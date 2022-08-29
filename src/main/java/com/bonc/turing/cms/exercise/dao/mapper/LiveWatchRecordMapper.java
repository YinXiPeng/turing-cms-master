package com.bonc.turing.cms.exercise.dao.mapper;

import com.bonc.turing.cms.exercise.dto.BjLiveUseTimeDTO;
import com.bonc.turing.cms.exercise.vo.SearchBjLiveWatchTimeVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * description:
 *
 * @author lxh
 * @date 2020/4/20 20:29
 */
@Mapper
public interface LiveWatchRecordMapper {

    /**
     * 查询直播用户观看总数
     *
     * @param searchBjLiveWatchTimeVO
     * @return
     */
    Long pageCount(SearchBjLiveWatchTimeVO searchBjLiveWatchTimeVO);

    /**
     * 查询直播用户观看时长信息
     *
     * @param searchBjLiveWatchTimeVO
     * @return
     */
    List<BjLiveUseTimeDTO> pageList(SearchBjLiveWatchTimeVO searchBjLiveWatchTimeVO);

    /**
     * 查询房间总时长
     *
     * @param roomId
     * @return
     */

    Long findTotalTimeByRoomId(String roomId);
}
