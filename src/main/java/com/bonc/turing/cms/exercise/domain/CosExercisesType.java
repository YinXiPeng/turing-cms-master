package com.bonc.turing.cms.exercise.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum CosExercisesType {
    /**
     * 选择题
     */
    OPTION_QUESTION(0, "OPTION_QUESTION"),

    /**
     * 简答题
     */
    SHORT_QUESTION(1, "SHORT_QUESTION"),

    /**
     * 视频
     */
    VIDEO_QUESTION(2, "VIDEO_QUESTION"),

    /**
     * 编程题
     */
    PROGRAM_QUESTION(3, "PROGRAM_QUESTION");

    private int value;

    private String name;

    public int value() {
        return this.value;
    }

    public static CosExercisesType indexOfVal(int val) {
        for (CosExercisesType questionType : values()) {
            if (questionType.value == val) {
                return questionType;
            }
        }
        throw new IllegalArgumentException("param value " + val);
    }

    public static CosExercisesType indexOfName(String name) {
        for (CosExercisesType questionType : values()) {
            if (questionType.name.equals(name)) {
                return questionType;
            }
        }
        throw new IllegalArgumentException("param name " + name);
    }
}


