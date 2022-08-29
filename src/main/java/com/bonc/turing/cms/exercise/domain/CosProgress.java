package com.bonc.turing.cms.exercise.domain;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * @desc 学习进度表
 * @author yh
 * @date 2020.03.24
 */
@Entity
@Data
public class CosProgress {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid",strategy="uuid")
    /**
     * 主键id
     */
    private String id;
    /**
     * 用户guid
     */
    private String guid;
    /**
     * 课程id
     */
    private String courseId;
    /**
     * 小节id
     */
    private String sectionId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 外部平台用户唯一标识
     */
    private String unionId;
}
