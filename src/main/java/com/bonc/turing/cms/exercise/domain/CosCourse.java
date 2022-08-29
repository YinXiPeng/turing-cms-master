package com.bonc.turing.cms.exercise.domain;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * @desc 课程表
 * @author yh
 */
@Data
@Entity
public class CosCourse {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    /**
     * 主键id
     */
    private String id;
    /**
     * 课程名
     */
    private String name;
    /**
     * 作者名称
     */
    private String authorName;
    /**
     * 封面url
     */
    private String coverUrl;
    /**
     * 封面描述
     */
    private String coverDesc;
    /**
     * 简介
     */
    private String briefTxt;
    /**
     * 教材方向
     */
    private String direction;
    /**
     * 价格
     */
    private double price;
    /**
     * 折扣
     */
    private double discount;
    /**
     * 课程类型（1：学校课程 2：平台课程 3：图灵严选）
     */
    private Integer type;
    /**
     * 课程形式(1：复合型课程 2：直播课程 3：图灵直播（预留） 4：视频课 5：实训课  6：浩泰课程 7: sas课程)
     */
    private Integer form=1;
    /**
     * 学校id
     */
    private String schoolId;
    /**
     *班级id
     */
    private String classId;
    /**
     * 二维码url
     */
    private String codeUrl;
    /**
     * 创建人id
     */
    private String createById;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 课程状态（0下架 1上架）
     */
    private Integer status;
    /**
     * 初始随机数
     */
    private Integer randomNum;
    /**
     * 是否删除
     */
    private Integer isDeleted;
    /**
     * 是否是优质课程
     */
    private Integer isSelected;

    /**
     * 外部课程链接
     */
    private String outCourseUrl;

}
