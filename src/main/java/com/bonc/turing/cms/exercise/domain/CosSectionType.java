package com.bonc.turing.cms.exercise.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum CosSectionType {
    /**
     * pdf
     */
    PDF(0, "PDF"),

    /**
     * ppt
     */
    PPT(1, "PPT"),

    /**
     * 视频
     */
    VIDEO(2, "VIDEO");

    private int value;

    private String msg;

    public int value() {
        return this.value;
    }

    public static CosSectionType indexOfVal(int val) {
        for (CosSectionType questionType : values()) {
            if (questionType.value == val) {
                return questionType;
            }
        }
        throw new IllegalArgumentException("param value " + val);
    }

    public static CosSectionType indexOfMsg(String msg) {
        for (CosSectionType questionType : values()) {
            if (questionType.msg.equals(msg)) {
                return questionType;
            }
        }
        throw new IllegalArgumentException("param msg " + msg);
    }
}

