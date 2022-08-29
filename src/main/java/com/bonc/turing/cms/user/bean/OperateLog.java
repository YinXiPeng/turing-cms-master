package com.bonc.turing.cms.user.bean;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * 用户操作信息.
 * @author liuyunkai
 * @date 2019-1-12 14:40
 * @updateBy yh
 */
@Data
@Entity
public class OperateLog implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    /**
     * 操作时间
     */
    private long operateTimestamp;
    /**
     * 登录用户id
     */
    private String userGuid;
    /**
     * 操作类型
     */
    private String operateType;
    /**
     * 资源所属guid
     */
    private String resourceGuid;

    /**
     * fork
     */
    private int fork=0;
    /**
     * 点赞
     */
    private int likes=0;
    /**
     * 发帖
     */
    private int posting=0;
    /**
     * 评论
     */
    private int comment=0;
    /**
     * 建立小组
     */
    private int groups=0;
    /**
     * notebook版本数
     */
    private int noteBookCommit=0;

    /**
     * 活跃度等级
     */
    private int activityLevel;

    /**
     * 自己salon新加入成员数量
     */
    private int members=0;

    /**
     * 创建新salon数量
     */
    private int salon=0;

    /**
     * 发布模型数量
     */
    private int model=0;

    /**
     * 数据集数量
     */
    private int dataSet=0;

}
