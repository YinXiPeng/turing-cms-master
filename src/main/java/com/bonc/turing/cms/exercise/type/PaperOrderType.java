package com.bonc.turing.cms.exercise.type;

import lombok.AllArgsConstructor;

/**
 * description: 试卷排序
 *
 * @author lxh
 * @date 2020/3/23 11:31
 */
@AllArgsConstructor
public enum PaperOrderType {
    /**
     * 顺序
     */
    SEQUENCE(0),

    /**
     * 随机
     */
    RANDOM(1);

    private int value;

    public int value() {
        return this.value;
    }

    /**
     * 通过 val 值索引 paperOrder
     *
     * @param val val
     * @return paperOrder
     */
    public static PaperOrderType indexOfVal(int val) {
        for (PaperOrderType paperOrderType : values()) {
            if (paperOrderType.value == val) {
                return paperOrderType;
            }
        }
        throw new IllegalArgumentException("param value " + val);
    }
}
