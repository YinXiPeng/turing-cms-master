package com.bonc.turing.cms.exercise.domain;

import com.alibaba.fastjson.JSONArray;
import com.bonc.turing.cms.exercise.utils.CorrectExercisesOptionConverter;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@Table(name = "cos_exercises_answer_record")
public class CosExercisesAnswerRecord {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid",strategy="uuid")
    private String id;

    @Column(name = "section_id")
    private String sectionId;

    @Column(name = "guid")
    private String guid;

    @Column(name = "options")
    @Convert(converter = CorrectExercisesOptionConverter.class)
    private JSONArray options;

    @Column(name = "correct_options")
    @Convert(converter = CorrectExercisesOptionConverter.class)
    private JSONArray correctOptions;

    @Column(name = "correct_percent")
    private int correctPercent;
}
