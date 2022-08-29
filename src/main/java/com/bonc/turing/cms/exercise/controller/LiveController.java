package com.bonc.turing.cms.exercise.controller;

import com.alibaba.fastjson.JSONObject;
import com.bonc.turing.cms.exercise.domain.BjyRoomInfo;
import com.bonc.turing.cms.exercise.dto.BjLiveUseTimeDTO;
import com.bonc.turing.cms.exercise.dto.LiveRoomIdDTO;
import com.bonc.turing.cms.exercise.service.LiveService;
import com.bonc.turing.cms.exercise.utils.ValidationResult;
import com.bonc.turing.cms.exercise.utils.ValidationUtils;
import com.bonc.turing.cms.exercise.vo.BjLiveRoomAddVO;
import com.bonc.turing.cms.exercise.vo.SearchBjLiveWatchTimeVO;
import com.bonc.turing.cms.manage.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * description:
 *
 * @author lxh
 * @date 2020/4/21 19:40
 */
@Slf4j
@RestController
@RequestMapping("/live")
public class LiveController {

    @Autowired
    private LiveService liveService;


    /**
     * 创建房间
     *
     * @param bjLiveRoomAddVO
     * @return
     */
    @PostMapping("/room")
    public Object addRoom(@RequestBody BjLiveRoomAddVO bjLiveRoomAddVO, @RequestParam String guid) {
        try {
            ValidationResult validationResult = ValidationUtils.validateEntity(bjLiveRoomAddVO);
            if (validationResult.isHasErrors()) {
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.ERROR_PARAMETER, validationResult.getKeyErrors(), "参数错误"));
            }
            JSONObject jsonObject = liveService.addRoom(bjLiveRoomAddVO, guid);
            if (Objects.nonNull(jsonObject)) {
                if (jsonObject.getInteger("code") == 1001) {
                    return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", jsonObject.getString("msg")));
                } else if (jsonObject.getInteger("code") == 1002) {
                    return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "同一时间存在相同直播"));
                }else if(jsonObject.getInteger("code") == 1003){
                    return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "房间id参数缺失"));
                }
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, jsonObject.getString("roomId"), "创建房间成功"));
            }
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "创建房间失败"));
        }catch (DataIntegrityViolationException e){
            log.error("addRoom is error", e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "房间号重复"));
        } catch (Exception e) {
            log.error("addRoom is error", e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "创建房间失败"));
        }
    }

    /**
     * 获取直播课程列表
     *
     * @return
     */
    @GetMapping("/room/page")
    public ResponseEntity roomPage(String guid, Pageable pageable) {
        if (StringUtils.isEmpty(guid)) {
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "参数传递错误"));
        }
        try {
            JSONObject data = liveService.roomPage(guid, pageable);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, data, "获取直播列表成功"));
        } catch (Exception e) {
            log.error("get room list error", e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "获取房间列表失败"));
        }
    }

    /**
     * 根据id 删除对应的房间信息
     *
     * @return
     */
    @GetMapping("/room/delete")
    public ResponseEntity deleteRoomById(Long id, String guid) {
        if (id == null || id == 0 || StringUtils.isEmpty(guid)) {
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "参数传递错误"));
        }
        try {
            Boolean flag = liveService.deleteRoomById(id, guid);
            if (flag) {
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, true, "删除直播成功"));
            } else {
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, false, "删除直播失败"));
            }

        } catch (Exception e) {
            log.error("delete room by roomId error", e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "删除失败"));
        }
    }

    /**
     * 获取房间用户观看时长列表
     *
     * @param pageable
     * @param searchBjLiveWatchTimeVO
     * @return
     */
    @GetMapping("/room/useTimePage")
    public ResponseEntity useTimeList(@PageableDefault Pageable pageable, SearchBjLiveWatchTimeVO searchBjLiveWatchTimeVO) {
        if (StringUtils.isBlank(searchBjLiveWatchTimeVO.getRoomId())) {
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.ERROR_PARAMETER, "", "参数错误"));
        }
        try {
            Page<BjLiveUseTimeDTO> bjLiveUseTimePage = liveService.useTimeList(pageable, searchBjLiveWatchTimeVO);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, bjLiveUseTimePage, "获取房间用户观看时长列表成功"));
        } catch (Exception e) {
            log.error("useTimeList error", e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "获取房间用户观看时长列表失败"));
        }
    }

    /**
     * 获取关联直播房间列表
     *
     * @param guid
     * @return
     */
    @GetMapping("/room/list")
    public ResponseEntity getRoomList(@RequestParam String guid) {
        try {
            List<LiveRoomIdDTO> roomIdList = liveService.getRoomList(guid);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, roomIdList, "获取关联直播房间列表成功"));
        } catch (Exception e) {
            log.error("get room list error", e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "获取关联直播房间列表失败"));
        }
    }

    /**
     * 获取直播房间信息
     *
     * @param id
     * @return
     */
    @GetMapping("/room/info")
    public ResponseEntity roomInfo(@RequestParam Long id) {
        try {
            BjyRoomInfo bjyRoomInfo = liveService.roomInfo(id);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, bjyRoomInfo, "获取直播房间信息成功"));
        } catch (Exception e) {
            log.error("get room list error", e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "获取直播房间信息失败"));
        }
    }

    /**
     * 获取直播房间信息 根据roomId
     *
     * @param roomId
     * @return
     */
    @GetMapping("/roomByRoomId")
    public ResponseEntity roomInfo(@RequestParam String  roomId) {
        try {
            BjyRoomInfo bjyRoomInfo = liveService.findRoomByRoomId(roomId);
            if (bjyRoomInfo != null){
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, bjyRoomInfo, "获取直播房间信息成功"));
            }else{
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "直播不存在"));
            }

        } catch (Exception e) {
            log.error("get room list error", e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "获取直播房间信息失败"));
        }
    }

    /**
     * 腾讯视频录制回调
     * @return
     */
    @PostMapping("/callback")
    public  ResponseEntity updateRecordUrl(@RequestBody JSONObject data){
        try {
            log.info("receive param[{}]",data);
            Boolean flag = liveService.callback(data);
            if (flag){
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, true, ""));
            }
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, false, ""));
        } catch (Exception e) {
            log.error("deal with flag error", e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, false, ""));
        }
    }

    /**
     * 获取开始直播签名
     * @return
     */
    @GetMapping("/signature")
    public  ResponseEntity getSignature(String tguid){
        try{
            String signature = liveService.signature(tguid);
            if (StringUtils.isNotEmpty(signature)){
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, signature, "获取签名成功"));
            }else{
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "获取签名失败"));
            }
        }catch (Exception e){
            log.error("get signature error", e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "获取签名失败"));
        }
    }

    /**
     * 结束直播
     * @param roomId
     * @return
     */
    @GetMapping("/dismiss")
    public  ResponseEntity end(Long roomId){

        if (roomId == null){
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.ERROR_PARAMETER, "", "参数错误"));
        }
        try{
            Boolean flag = liveService.end(roomId);
            if(flag) {
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, true, "解散房间成功"));
            }else{
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, false, "解散房间失败"));
            }
        }catch (Exception e){
            log.error("dismiss error",e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, false, "解散房间失败"));
        }

    }

}
