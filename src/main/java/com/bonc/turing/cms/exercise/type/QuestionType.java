package com.bonc.turing.cms.exercise.type;

import lombok.AllArgsConstructor;

/**
 * description:试题类型
 *
 * @author lxh
 * @date 2020/3/23 11:31
 */
@AllArgsConstructor
public enum QuestionType {
    /**
     * 选择题
     */
    OPTION_QUESTION(0),

    /**
     * 简答题
     */
    SHORT_QUESTION(1),

    /**
     * 视频
     */
    VIDEO_QUESTION(2),

    /**
     * 编程题
     */
    PROGRAM_QUESTION(3);

    private int value;

    public int value() {
        return this.value;
    }

    /**
     * 通过 val 值索引 questionType
     *
     * @param val val
     * @return questionType
     */
    public static QuestionType indexOfVal(int val) {
        for (QuestionType questionType : values()) {
            if (questionType.value == val) {
                return questionType;
            }
        }
        throw new IllegalArgumentException("param value " + val);
    }
}
