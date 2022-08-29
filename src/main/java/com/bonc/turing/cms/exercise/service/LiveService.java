package com.bonc.turing.cms.exercise.service;

import com.alibaba.fastjson.JSONObject;
import com.bonc.turing.cms.exercise.domain.BjyRoomInfo;
import com.bonc.turing.cms.exercise.dto.BjLiveUseTimeDTO;
import com.bonc.turing.cms.exercise.dto.LiveRoomIdDTO;
import com.bonc.turing.cms.exercise.vo.BjLiveRoomAddVO;
import com.bonc.turing.cms.exercise.vo.SearchBjLiveWatchTimeVO;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * description:
 *
 * @author lxh
 * @date 2020/4/21 19:42
 */
public interface LiveService {

    /**
     * 创建房间
     *
     * @param bjLiveRoomAddVO
     * @param guid
     * @return
     */
    JSONObject addRoom(BjLiveRoomAddVO bjLiveRoomAddVO, String guid);

    /**
     * 获取房间列表
     *
     * @param guid
     * @param pageable
     * @return
     */
    JSONObject roomPage(String guid, Pageable pageable);

    /**
     * 删除房间
     *
     * @param id
     * @param guid
     * @return
     * @throws Exception
     */
    Boolean deleteRoomById(Long id, String guid) throws Exception;

    /**
     * 房间用户使用+时长列表
     *
     * @param pageable
     * @param searchBjLiveWatchTimeVO
     * @return
     */
    Page<BjLiveUseTimeDTO> useTimeList(Pageable pageable, SearchBjLiveWatchTimeVO searchBjLiveWatchTimeVO);

    /**
     * 获取关联直播房间列表
     *
     * @param guid
     * @return
     */
    List<LiveRoomIdDTO> getRoomList(String guid);

    /**
     * huoqu
     * @param id
     * @return
     */
    BjyRoomInfo roomInfo(Long id);

    /**
     * 根据房间id 获取房间信息
     * @param roomId
     * @return
     */
    BjyRoomInfo findRoomByRoomId(String roomId);

    /**
     * 直播录制的回调
     * @param data
     * @return
     */
    Boolean callback(JSONObject data);

    /**
     * 解散房间
     * @param roomId
     * @throws TencentCloudSDKException
     * @return
     */
    Boolean end(Long roomId) throws TencentCloudSDKException;

    /**
     * 获取签名
     * @param guid
     * @return
     */
    String signature(String guid);
}
