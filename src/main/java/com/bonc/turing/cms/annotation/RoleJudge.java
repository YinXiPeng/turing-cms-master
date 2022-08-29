package com.bonc.turing.cms.annotation;


import com.bonc.turing.cms.enums.UserRoleEnum;

import java.lang.annotation.*;

/**
 * @Author lky
 * @Description 判断用户角色(请求参数种必须带有guid),并判断用户是否登陆
 * @Date 10:48 2020/5/22
 * @Param role调改接口的用户需要拥有的角色
 * @return
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RoleJudge {

    UserRoleEnum[] role();
}
