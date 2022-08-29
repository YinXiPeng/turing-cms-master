package com.bonc.turing.cms.exercise.domain;

import com.alibaba.fastjson.JSONArray;
import com.bonc.turing.cms.exercise.utils.CorrectExercisesOptionConverter;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "cos_exercises")
public class CosExercises {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "section_id")
    private String sectionId;

    @Column(name = "sort")
    private Integer sort;

    @Column(name = "name")
    private String name;

    @Column(name = "content")
    private String content;

    @Column(name = "question_type")
    private CosExercisesType questionType;

    @Column(name = "correct_options")
    @Convert(converter = CorrectExercisesOptionConverter.class)
    private JSONArray correctOptions;

    @Column(name = "create_time")
    private Date createTime;
}
