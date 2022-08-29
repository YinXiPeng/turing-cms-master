package com.bonc.turing.cms.user.bean;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;
/**
 * @desc 图灵平台点赞表
 * @author yh
 * @date 2020.07.10
 */
@Data
@Entity
public class TuringLike {

    /**
     * 主键id
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    /**
     * 点赞内容id
     */
    private String referId;


    /**
     * 用户id
     */
    private String userId;


    /**
     * 点赞类型  3：资源
     */
    private Integer type;


    /**
     * 创建时间
     */
    private Date createTime;


}
