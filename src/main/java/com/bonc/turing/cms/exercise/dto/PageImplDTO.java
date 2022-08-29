package com.bonc.turing.cms.exercise.dto;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * description:
 *
 * @author lxh
 * @date 2020/6/1 19:51
 */
public class PageImplDTO<T> extends PageImpl<T> {

    private Long roomTotalTime;

    public PageImplDTO(List<T> content, Pageable pageable, long total, Long roomTotalTime) {
        super(content, pageable, total);
        this.roomTotalTime = roomTotalTime;
    }

    public PageImplDTO(List<T> content) {
        super(content);
    }


    public Long getRoomTotalTime() {
        return roomTotalTime;
    }

    public void setRoomTotalTime(Long roomTotalTime) {
        this.roomTotalTime = roomTotalTime;
    }

    @Override
    public String toString() {
        return "PageImplDTO{" +
                "roomTotalTime=" + roomTotalTime +
                '}';
    }
}
