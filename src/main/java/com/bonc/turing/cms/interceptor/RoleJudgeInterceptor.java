package com.bonc.turing.cms.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.bonc.turing.cms.annotation.RoleJudge;
import com.bonc.turing.cms.enums.UserRoleEnum;
import com.bonc.turing.cms.manage.ResultEntity;
import com.bonc.turing.cms.manage.entity.user.CmsPermissions;
import com.bonc.turing.cms.manage.entity.user.CmsUserInfo;
import com.bonc.turing.cms.manage.repository.CmsUserInfoRepository;
import com.bonc.turing.cms.manage.service.login.LoginService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Aspect
@Component
public class RoleJudgeInterceptor {

    @Autowired
    private CmsUserInfoRepository cmsUserInfoRepository;
    @Autowired
    private LoginService loginService;


    @Around("execution(public * *(..)) && @annotation(com.bonc.turing.cms.annotation.RoleJudge)")
    public Object interceptor(ProceedingJoinPoint pjp) {

        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        String guid = request.getParameter("guid");
        String token = request.getParameter("token");
        if (null==guid||"".equals(guid)){
            //返回权限不足
            return ResponseEntity.ok(new ResultEntity(ResultEntity.ResultStatus.NO_PERMISSION, new JSONObject(), ResultEntity.ResultStatus.NO_PERMISSION.getMsg()));
        }else {
            MethodSignature signature = (MethodSignature) pjp.getSignature();
            Method method = signature.getMethod();
            RoleJudge roleJudge = method.getAnnotation(RoleJudge.class);
            UserRoleEnum[] role = roleJudge.role();
            Optional<CmsUserInfo> byId = cmsUserInfoRepository.findById(guid);
            if (byId.isPresent()){
                CmsUserInfo cmsUserInfo = byId.get();
                List<CmsPermissions> permissions = cmsUserInfo.getPermissions();
                //判断是否有该权限
                for (UserRoleEnum userRoleEnum:role){
                    for (CmsPermissions cmsPermissions:permissions){
                        if (userRoleEnum.getRoleCode().equals(cmsPermissions.getId())){
                            try {
                                //有权限,放行
                                boolean loginUser = loginService.isLoginUser(guid, token);
                                if (loginUser){
                                    return pjp.proceed();
                                }else {
                                    //未登陆
                                    return ResponseEntity.ok(new ResultEntity(ResultEntity.ResultStatus.CODE_408, new JSONObject(), ResultEntity.ResultStatus.CODE_408.getMsg()));
                                }
                            } catch (Throwable throwable) {
                                throw new RuntimeException("系统异常");
                            }
                        }
                    }
                }
            }
            //权限不足
            return ResponseEntity.ok(new ResultEntity(ResultEntity.ResultStatus.NO_PERMISSION, new JSONObject(), ResultEntity.ResultStatus.NO_PERMISSION.getMsg()));
        }
    }
}
