package com.bonc.turing.cms.home.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
public class DiscussUp implements Serializable {
    /**
     * id
     *
     * @mbggenerated
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 帖子id
     *
     * @mbggenerated
     */
    private String discussId;

    /**
     * 用户id
     *
     * @mbggenerated
     */
    private String userId;

}