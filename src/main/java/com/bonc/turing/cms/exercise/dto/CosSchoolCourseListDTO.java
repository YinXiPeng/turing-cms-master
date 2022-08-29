package com.bonc.turing.cms.exercise.dto;

import lombok.Data;

/**
 * description:试卷新增或编辑绑定课程列表信息DTO
 *
 * @author lxh
 * @date 2020/3/24 10:32
 */
@Data
public class CosSchoolCourseListDTO {

    /**
     * 课程ID
     */
    private String id;

    /**
     * 课程名称
     */
    private String name;

}
