package com.bonc.turing.cms.user.bean;
import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @desc 用户积分表
 * @author  yh
 * @date 2020.06.18
 */
@Data
@Entity
public class UserPoint {

    /**
     * 用户id
     */
    @Id
    private String userId;
    /**
     * 点数
     */
    private Integer point = 0;

    /**
     * 积分
     */
    private Integer credit = 0;


}
