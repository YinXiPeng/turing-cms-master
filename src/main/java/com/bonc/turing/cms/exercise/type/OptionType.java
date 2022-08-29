package com.bonc.turing.cms.exercise.type;

import lombok.AllArgsConstructor;

/**
 * description:选择题类型
 *
 * @author lxh
 * @date 2020/3/23 11:39
 */
@AllArgsConstructor
public enum OptionType {
    /**
     * 单选题
     */
    SINGLE(0),

    /**
     * 多选题
     */
    MULTIPLE(1);

    private int value;

    public int value() {
        return this.value;
    }

    /**
     * 通过 val 值索引 optionType
     *
     * @param val val
     * @return questionType
     */
    public static OptionType indexOfVal(int val) {
        for (OptionType optionType : values()) {
            if (optionType.value == val) {
                return optionType;
            }
        }
        throw new IllegalArgumentException("param value " + val);
    }
}
