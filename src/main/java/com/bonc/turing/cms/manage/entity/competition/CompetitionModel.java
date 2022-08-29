package com.bonc.turing.cms.manage.entity.competition;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @ProjectName: cms
 * @Package: com.bonc.turing.cms.manage.entity.competition
 * @ClassName: CompetitionModel
 * @Author: bxt
 * @Description: 竞赛-模型
 * @Date: 2019/7/19 10:32
 * @Version: 1.0
 */
@Entity
@JsonIgnoreProperties(allowSetters = true, value = {"handler", "hibernateLazyInitializer"})
@Table(name = "notebook")
@Data
public class CompetitionModel {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    /**
     * 用户id
     */
    private String guid;

    /**
     * notebook唯一id
     */
    private String notebookId;

    /**
     * 创建的notebook名称
     */
    private String notebookName;

    /**
     * notebook描述
     */
    private String notebookDescription;

    /**
     * notebook存储路径
     */
    private String notebookPath;

    /**
     * parent_id
     */
    private String parentId;

    /**
     * version
     */
    private Integer version;

    /**
     * 上次修改时间
     */
    private Date lastModified;

    /**
     * kernelid
     */
    private String kernelId;

    /**
     * 代码执行时间，升为版本时生成
     */
    private Integer executeTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 是否公开 0：公开，1：不公开
     */
    private Integer isPublic;

    /**
     * 是否删除 0：删除 1：未删除
     */
    private Integer isDelete;

    /**
     * 类型id
     */
    private String typeId;

    /**
     * env_id
     */
    private String envId;

    private Integer imgNum;

    private String img;

    /**
     * 工具
     */
    private String notebookTools;

    private Integer codeNum;

    /**
     * 状态：stopped 、running、isready 保留字段
     */
    private String status;
}
