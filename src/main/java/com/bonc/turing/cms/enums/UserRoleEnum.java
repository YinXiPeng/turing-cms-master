package com.bonc.turing.cms.enums;

public enum UserRoleEnum {

    CODE_2001("后台教务系统",2001);


    private String roleName;
    private Integer roleCode;
    private UserRoleEnum(String roleName, int roleCode) {
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
