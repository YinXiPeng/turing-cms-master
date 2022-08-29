package com.bonc.turing.cms.enums;

public enum BCreateCourseEnum {

    CODE_3001("创建复合课程",3001),
    CODE_3002("创建sas课程",3002);


    private String roleName;
    private Integer roleCode;
    private BCreateCourseEnum(String roleName, int roleCode) {
        this.roleName = roleName;
        this.roleCode = roleCode;
    }

    public String getRoleName(){
        return roleName;
    }

    public Integer getRoleCode(){
        return roleCode;
    }
}
