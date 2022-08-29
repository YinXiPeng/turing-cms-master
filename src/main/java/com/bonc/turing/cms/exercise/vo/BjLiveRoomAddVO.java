package com.bonc.turing.cms.exercise.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * description:
 *
 * @author lxh
 * @date 2020/4/3 15:54
 */
@Data
public class BjLiveRoomAddVO {

    /**
     * 直播课标题，不超过50个字符或汉字，超过部分将进行截取
     */
    @NotBlank(message = "课程名称不能为空")
    private String title;

    private Integer startTime;

    @NotNull(message = "开始时间不能为空")
    private Long beginTime;

    @NotNull(message = "课程时长不能为空")
    private Long duration;

    private Integer endTime;

    /**
     * 1:腾讯课堂
     * 2:janus
     */
    private Integer type;

    /**
     * 代表普通大班课最大人数, 不传或传0表示不限制。
     */
    private Integer maxUsers;

    /**
     * 生可提前进入的时间，单位为秒
     */
    private Integer preEnterTime;

    /**
     * 可选值，教育直播：doubleCamera(双摄像头)、
     * classic(经典模板)、triple(三分屏)、
     * single(单视频模板)、liveWall(视频墙)，
     * 默认triple;
     */
    private String templateName;

    /**
     * 老师是否启用设备检测 1:启用 2:不启用 默认不启用
     */
    private Integer teacherNeedDetectDevice;

    /**
     * 学生是否启用设备检测 1:启用 2:不启用 默认不启用
     */
    private Integer studentNeedDetectDevice;

    /**
     * 指定PC端是否以视频为主 1:以视频为主
     * 2:以PPT为主 （默认是以ppt为主，该选项只针对三分屏有效）
     */
    private Integer isVideoMain;

    /**
     * 课程预设的结束时间后可以拖堂的时间，到时间会强制下课，
     * 单位（秒），0不强制，大于0生效,最大不可超过3600秒（一小时），
     * 设置后课程不能修改结束时间
     */
    private Integer endDelayTime;

    /**
     * 是否是webrtc课程,1:是（需要联系百家云配置webrtc类型才可使用）
     */
    private Integer isWebrtcLive;

    /**
     * 有无学生上麦，仅在webrtc班型上使用此参数 0：无，1：有  默认0
     */
    private Integer hasStudentRaise;

    /**
     * 是否是长期房间，0:普通房间(注：普通房间时长小于24小时) 1:长期房间 默认为普通房间（注：需要给账号开通长期房间权限才可以创建长期房间）
     */
    private Integer isLongTerm;

    /**
     * 是否启用微信授权 1：启用，2：不启用 默认1
     */
    private Integer enableWeixinAuth;

    private String guid;

    @NotNull(message = "是否需要登录不能为空")
    private Integer needLogin;

    @NotNull(message = "是否需要完善个人信息不能为空")
    private Integer needFillInfo;
    /**
     * 房间id
     */
    private String roomId;
    /**
     * 白板id
     */
    private String whiteboardId;
}
