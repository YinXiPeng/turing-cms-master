package com.bonc.turing.cms.enums;

/**
 * 课程形式枚举类(1：复合型课程 2：直播课程 3：图灵直播（预留）
 * 4：视频课 5：实训课 6：浩泰课程 7:sas课程 )
 */
public enum CourseFormEnum {
    COMPOSITE_COURSE("复合型课程",1),
    LIVE_COURSE("直播课程",2),
    TURING_LIVE_COURSE("图灵直播",3),
    VIDEO_COURSE("视频课",4),
    TRAINING_COURSE("实训课",5),
    HAO_TAI_COURSE("浩泰课程",6),
    SAS_COURSE("sas课程",7);

    private String formDesc;
    private Integer formValue;

    CourseFormEnum(String formDesc, Integer formValue) {
        this.formDesc = formDesc;
        this.formValue = formValue;
    }

    public String getFormDesc(){
        return this.formDesc;
    }
    public Integer getFormValue(){
        return this.formValue;
    }



}
