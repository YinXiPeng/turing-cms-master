package com.bonc.turing.cms.home.domain;

import lombok.Data;

@Data
public class DSLikeUser{

    private String id;
    /**
     * 点赞用户guid
     */
    private String guid;
    /**
     * 是否点赞
     */
    private boolean giveLike;

    private String dataSourceDefId;


}