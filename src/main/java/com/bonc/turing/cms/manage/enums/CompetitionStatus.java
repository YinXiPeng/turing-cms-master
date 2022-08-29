package com.bonc.turing.cms.manage.enums;

/**
 * @author  LY
 * @desc 竞赛状态枚举
 * @date 2019.01.26
 */
public enum CompetitionStatus {

    PREPARING("1","准备中"),
    PUBLISHING("2","待发布"),
    GAMING("3","比赛中"),
    PRELIMINARY_GRADING("5","初赛评分中"),
    FINISHED("4","已结束"),
    AllSTATUS("6","全部"),
    PRELIMINARY_END("7","初赛结束"),
    FINAL_GRADING("8","决赛评分中"),
    COMMON_NEW_GRADING("9","评分中(普通赛，新手赛)");

    private String code;
    private String msg;

    CompetitionStatus(String code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
