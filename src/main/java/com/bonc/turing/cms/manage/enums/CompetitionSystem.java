package com.bonc.turing.cms.manage.enums;
/**
 * @author  LY
 * @desc 赛制枚举
 * @date 2019.01.26
 */
public enum CompetitionSystem {

    BEFORE_FINAL_COMPETITION("1","初赛决赛"),
    GENERAL_COMPETITION("2","普通赛"),
    NEWHAND_COMPETITION("5","新手赛");


    private String code;
    private String msg;

    CompetitionSystem(String code, String msg){
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
