package com.bonc.turing.cms.exercise.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author yh
 * @desc 课程兑换码
 * @date 2020.04.17
 */
@Entity
@Data
public class CosFreeCode {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id; //主键id

    private String code; //兑换码

    private String courseId; //课程id

    private String guid; //用户id

    private String userName; //用户名

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime; //创建时间

}
