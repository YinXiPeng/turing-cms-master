package com.bonc.turing.cms.manage.entity.competition;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @ProjectName: cms
 * @Package: com.bonc.turing.cms.manage.entity.competition
 * @ClassName: Discuss
 * @Author: bxt
 * @Description: 竞赛-评论
 * @Date: 2019/7/17 14:58
 * @Version: 1.0
 */
@Entity
@Data
@Table(name="discuss")
public class Discuss {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    /**
     * 主题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 排序（置顶加精 3；置顶 2； 加精 1 ；普通 0）
     */
    private Integer sorting;

    /**
     * 是否置顶，0为否，1为是
     */
    private Integer top;

    /**
     * 是否加精，0为否，1为是
     */
    private Integer excellent;

    /**
     * 获赞数
     */
    private Long upNum;

    /**
     * 浏览数
     */
    private Long viewNum;

    /**
     * _num bigint(11)
     */
    private Long commentNum;

    /**
     * 被举报数
     */
    private Long reportNum;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 最后回应时间
     */
    private Date updateTime;

    /**
     * 所属id
     */
    private String referId;

    /**
     * 所属模块
     */
    private String referType;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 类别id
     */
    private String categoryId;
    /**
     * 管理员标签
     */
    private String remark;

    /**
     * 隐藏/显示（0 显示 1 隐藏）
     */
    private Integer showHide;

    /**
     * 删除标识（0：否 1： 是）
     */
    private Integer delFlag;

    /**
     * 排序时间
     */
    private Date sortTime;

    private int isHomeTop;

    private int isHomeExcellent;

    private int homeSorting;

    private Date homeSortTime;

    public Discuss() {
    }
}
