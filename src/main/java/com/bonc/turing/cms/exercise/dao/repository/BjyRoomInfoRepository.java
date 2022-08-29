package com.bonc.turing.cms.exercise.dao.repository;

import com.bonc.turing.cms.exercise.domain.BjyRoomInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author lxm
 */
public interface BjyRoomInfoRepository extends JpaRepository<BjyRoomInfo, Long> {

    /**
     * 根据guid 和分页查询数据
     *
     * @param guid
     * @param pageable
     * @return
     */
    Page<BjyRoomInfo> findByGuidOrderByStartTimeDesc(String guid, Pageable pageable);

    /**
     * 根据id 和guid 删除数据
     *
     * @param id
     * @param guid
     * @return
     */
    Integer deleteByIdAndGuid(Long id, String guid);

    /**
     * 获取关联直播课程信息
     *
     * @param guid
     * @return
     */
    List<BjyRoomInfo> findByGuidOrderByStartTimeDesc(String guid);

    /**
     * 根据房间id获取房间信息
     * @param roomId
     * @return
     */
    Optional<BjyRoomInfo> findByRoomId(String roomId);
}
