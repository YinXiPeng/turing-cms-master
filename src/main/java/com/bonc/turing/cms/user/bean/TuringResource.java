package com.bonc.turing.cms.user.bean;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * @desc 上传资源
 * @author  yh
 * @date 2020.07.08
 */
@Entity
@Data
public class TuringResource {
    /**
     *主键id
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    /**
     * 用户id
     */
    private String guid;


    /**
     * 标题
     */
    private String title;


    /**
     * 资源描述
     */
    private String description;

    /**
     * 网盘地址
     */
    private String diskUrl;

    /**
     *提取码
     */
    private String accessCode;

    /**
     * 下载资源所需积分
     */
    private Integer needCredit;

    /**
     * 下载次数
     */
    private Integer downloadNum;

    /**
     * 点赞次数
     */
    private Integer likeNum;


    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
