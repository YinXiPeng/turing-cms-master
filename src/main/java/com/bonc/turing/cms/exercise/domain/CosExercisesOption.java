package com.bonc.turing.cms.exercise.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "cos_exercises_option")
public class CosExercisesOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sort")
    private Integer sort;

    @Column(name = "question_id")
    private Long questionId;

    @Column(name = "`option`")
    private String option;

    @Column(name = "content")
    private String content;

    @Column(name = "create_time")
    private Date createTime;
}
