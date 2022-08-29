package com.bonc.turing.cms.exercise.dto;

import lombok.Builder;
import lombok.Data;

/**
 * description:
 *
 * @author lxh
 * @date 2020/4/22 9:46
 */
@Data
@Builder
public class LiveRoomIdDTO {

    private Long id;

    private String roomId;

    private String title;

}
