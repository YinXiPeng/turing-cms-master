package com.bonc.turing.cms.exercise.vo;

import lombok.Data;

/**
 * description:
 *
 * @author lxh
 * @date 2020/4/21 9:41
 */
@Data
public class SearchBjLiveWatchTimeVO {

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 房间号
     */
    private String roomId;

    /**
     * 分页当前页数0起始页
     */
    private Long page;

    /**
     * 每页显示记录数
     */
    private Integer size;
}
