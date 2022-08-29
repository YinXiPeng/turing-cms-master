package com.bonc.turing.cms.home.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author xieTing
 * @description: 点赞
 * @date 上午 10:37 2019-02-14
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "`notebook_likes`")
public class NotebookLikes implements Serializable {
    @Id
    @GenericGenerator(name = "uuidGenerator", strategy = "uuid")
    @GeneratedValue(generator = "uuidGenerator")
    private String likesId;

    @Column(nullable = false)
    private String guid;

    @Column(nullable = false)
    private String notebookId;

    private String likesTime;
}
