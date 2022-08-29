package com.bonc.turing.cms.exercise.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bonc.turing.cms.common.utils.StringUtils;
import com.bonc.turing.cms.exercise.dao.mapper.BjyLiveMapper;
import com.bonc.turing.cms.exercise.dao.mapper.LiveWatchRecordMapper;
import com.bonc.turing.cms.exercise.dao.repository.BjyRoomInfoRepository;
import com.bonc.turing.cms.exercise.domain.BjyRoomInfo;
import com.bonc.turing.cms.exercise.dto.BjLiveUseTimeDTO;
import com.bonc.turing.cms.exercise.dto.LiveRoomIdDTO;
import com.bonc.turing.cms.exercise.dto.PageImplDTO;
import com.bonc.turing.cms.exercise.service.LiveService;
import com.bonc.turing.cms.exercise.vo.BjLiveRoomAddVO;
import com.bonc.turing.cms.exercise.vo.SearchBjLiveWatchTimeVO;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.trtc.v20190722.TrtcClient;
import com.tencentcloudapi.trtc.v20190722.models.DismissRoomRequest;
import com.tencentcloudapi.trtc.v20190722.models.DismissRoomResponse;
import com.tencentyun.TLSSigAPIv2;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * description:
 *
 * @author lxh
 * @date 2020/4/21 19:42
 */
@Service
@Slf4j
public class LiveServiceImpl implements LiveService {

    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 直播房间URL前缀
     */
    @Value("${living.url}")
    private String livingUrl;

    @Autowired
    BjyLiveMapper bjyLiveMapper;

    @Autowired
    BjyRoomInfoRepository bjyRoomInfoRepository;

    @Autowired
    private LiveWatchRecordMapper liveWatchRecordMapper;
    /**
     * 所在区
     */
    @Value("${live.tencent.region}")
    private String region;
    /**
     * secretId
     */
    @Value("${live.tencent.SecretId}")
    private String secretId;
    /**
     * secretKey
     */
    @Value("${live.tencent.SecretKey}")
    private String secretKey;
    /**
     * sdkAppId
     */
    @Value("${live.tencent.sdkAppId}")
    private Long skdAppId;

    @Value("${live.tencent.key}")
    private String key;

    @Override
    public JSONObject addRoom(BjLiveRoomAddVO bjLiveRoomAddVO, String guid) {

        Integer type =  bjLiveRoomAddVO.getType();
        if (type == null ){
            //默认是janus
            type = 2;
        }
        //支持的srs
        if (type == 1 || type == 3 ){
            bjLiveRoomAddVO.setRoomId(String.valueOf(System.currentTimeMillis()/1000));
        }else{
            //janus 直播 而且没有房间号
            if (StringUtils.isEmpty(bjLiveRoomAddVO.getRoomId())){
                JSONObject data = new JSONObject();
                data.put("code", 1003);
                return data;
            }
        }

        LocalDateTime startTime = timestampToLocalDateTime(bjLiveRoomAddVO.getBeginTime());
        LocalDateTime endTime = startTime.plusMinutes(bjLiveRoomAddVO.getDuration());
        Long startTimeMillis = localDateTimeToTimestamp(startTime);
        Long endTimeMillis = localDateTimeToTimestamp(endTime);
        List<BjyRoomInfo> intersection = bjyLiveMapper.getIntersection(startTimeMillis, endTimeMillis);
        if (org.springframework.util.CollectionUtils.isEmpty(intersection)) {
            BjyRoomInfo bjyRoomInfo = new BjyRoomInfo();
            bjyRoomInfo.setType(bjLiveRoomAddVO.getType());
            bjyRoomInfo.setCreateTime(System.currentTimeMillis());
            bjyRoomInfo.setStartTime(startTimeMillis);
            bjyRoomInfo.setEndTime(endTimeMillis);
            bjyRoomInfo.setTitle(bjLiveRoomAddVO.getTitle());
            bjyRoomInfo.setDuration(bjLiveRoomAddVO.getDuration());
            bjyRoomInfo.setGuid(guid);
            bjyRoomInfo.setRoomId(bjLiveRoomAddVO.getRoomId());
            bjyRoomInfo.setNeedFillInfo(bjLiveRoomAddVO.getNeedFillInfo());
            bjyRoomInfo.setNeedLogin(bjLiveRoomAddVO.getNeedLogin());
            bjyRoomInfo.setRoomUrl(livingUrl + bjLiveRoomAddVO.getRoomId());
            bjyRoomInfo.setWhiteboardId(bjLiveRoomAddVO.getWhiteboardId());
            bjyRoomInfoRepository.save(bjyRoomInfo);
            JSONObject data = new JSONObject();
            data.put("code", 1000);
            data.put("roomId", bjyRoomInfo.getRoomId());
            return data;
        } else {
            JSONObject data = new JSONObject();
            data.put("code", 1002);
            return data;
        }
    }

    @Override
    public JSONObject roomPage(String guid, Pageable pageable) {
        JSONObject data = new JSONObject();
        PageRequest request = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        Page<BjyRoomInfo> roomInfoPage = bjyRoomInfoRepository.findByGuidOrderByStartTimeDesc(guid, request);
        if (roomInfoPage != null) {
            data.put("total", roomInfoPage.getTotalElements());
            data.put("roomList", roomInfoPage.getContent());
        } else {
            data.put("total", 0);
            data.put("roomList", new ArrayList<>());
        }
        return data;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean deleteRoomById(Long id, String guid) throws Exception {
        Integer integer = bjyRoomInfoRepository.deleteByIdAndGuid(id, guid);
        if (integer == 1) {
            return true;
        } else {
            throw new Exception("delete data error");
        }
    }

    @Override
    public Page<BjLiveUseTimeDTO> useTimeList(Pageable pageable, SearchBjLiveWatchTimeVO searchBjLiveWatchTimeVO) {
        searchBjLiveWatchTimeVO.setPage(pageable.getOffset());
        searchBjLiveWatchTimeVO.setSize(pageable.getPageSize());
        Long aLong = liveWatchRecordMapper.pageCount(searchBjLiveWatchTimeVO);
        List<BjLiveUseTimeDTO> bjLiveUseTimes = liveWatchRecordMapper.pageList(searchBjLiveWatchTimeVO);
        if (CollectionUtils.isNotEmpty(bjLiveUseTimes)) {
            bjLiveUseTimes.forEach(bjLiveUseTimeDTO -> {
                String liveUserId = bjLiveUseTimeDTO.getLiveUserId();
                bjLiveUseTimeDTO.setNumber(liveUserId.substring(liveUserId.length() - 8));
            });
        }
        Long totalTimeByRoomId = liveWatchRecordMapper.findTotalTimeByRoomId(searchBjLiveWatchTimeVO.getRoomId());
        if (Objects.isNull(totalTimeByRoomId)) {
            totalTimeByRoomId = 0L;
        }
        return new PageImplDTO<>(bjLiveUseTimes, pageable, aLong, totalTimeByRoomId);
    }

    @Override
    public List<LiveRoomIdDTO> getRoomList(String guid) {
        List<BjyRoomInfo> bjyRoomInfoList = bjyRoomInfoRepository.findByGuidOrderByStartTimeDesc(guid);
        if (CollectionUtils.isNotEmpty(bjyRoomInfoList)) {
            return bjyRoomInfoList.stream().map(bjyRoomInfo -> LiveRoomIdDTO.builder().id(bjyRoomInfo.getId()).roomId(bjyRoomInfo.getRoomId()).title(bjyRoomInfo.getTitle()).build()).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public BjyRoomInfo roomInfo(Long id) {
        return bjyRoomInfoRepository.findById(id).orElse(null);
    }

    @Override
    public BjyRoomInfo findRoomByRoomId(String roomId) {
        return bjyRoomInfoRepository.findByRoomId(roomId).orElse(null);
    }

    private LocalDateTime timestampToLocalDateTime(Long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
    }

    private Long localDateTimeToSecondTimestamp(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
    }

    private Long localDateTimeToTimestamp(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * 处理回调参数 更新数据库
     * @param data
     * @return
     */
    @Override
    public Boolean callback(JSONObject data) {
       Integer eventType = data.getInteger("event_type");
       String streamId = data.getString("stream_id");
       String videoUrl = data.getString("video_url");
       if (eventType == 100){
           if (streamId != null){
               bjyRoomInfoRepository.findByRoomId(streamId).ifPresent(temp->{
                   temp.setRecordUrl(videoUrl);
                   bjyRoomInfoRepository.save(temp);
               });
           }
           return true;
       }
    return false;
    }

    /**
     * 解散房间
     * @param roomId
     * @return
     */
    @Override
    public Boolean end(Long roomId) throws TencentCloudSDKException {
        DismissRoomRequest dismissRoomRequest = new DismissRoomRequest();
        dismissRoomRequest.setRoomId(roomId);
        dismissRoomRequest.setSdkAppId(skdAppId);
        DismissRoomResponse dismissRoomResponse = new TrtcClient(new Credential(secretId,secretKey),region).DismissRoom(dismissRoomRequest);
        if (dismissRoomResponse != null){
            String requestId = dismissRoomResponse.getRequestId();
            if (!StringUtils.isEmpty(requestId)){
                log.info("response from tencent[{}]",requestId);
                return true;
            }
        }
        return false;
    }

    @Override
    public String signature(String guid) {
        return  new TLSSigAPIv2(skdAppId, key).genSig(guid, 3600 * 24 * 1000);
    }
}
