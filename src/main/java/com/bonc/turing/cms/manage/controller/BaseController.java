package com.bonc.turing.cms.manage.controller;

import com.alibaba.fastjson.JSONObject;
import com.bonc.turing.cms.manage.constant.SystemConstants;
import com.bonc.turing.cms.manage.entity.user.CmsUserInfo;
import com.bonc.turing.cms.manage.service.login.LoginService;
import com.bonc.turing.cms.manage.service.user.CmsUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Optional;

public class BaseController {

    private HttpServletRequest request;
    private HttpSession session;
    private HttpServletResponse response;
    @Autowired
    private LoginService loginService;
    @Autowired
    private CmsUserService cmsUserService;

    @ModelAttribute
    public void beforeInvokingHttpHandlerMethod(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        this.response = response;
        this.request = request;
        this.session = session;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpSession getSession() {
        return session;
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }


    /**
     * 验证guid,token是否有效
     * @return
     */
    public JSONObject validateGuidAndToken() {
        JSONObject json = new JSONObject();
        JSONObject data = new JSONObject();
        int code = SystemConstants.CODE_SUCCESS;
        String msg = SystemConstants.MSG_200;
        boolean valid = false;
        String username = "";
        String guid = (String) session.getAttribute("guid");
        String token =  (String) session.getAttribute("token");
        if (StringUtils.isNoneBlank(guid, token)) {
            Optional<CmsUserInfo> userInfoOptional = cmsUserService.getByGuid(guid);
            if (userInfoOptional.equals(Optional.empty())) {
                code = SystemConstants.CODE_FAILURE;
                msg = "不存在该guid对应的用户";
            } else {
                username = userInfoOptional.get().getUserName();
                valid = loginService.isLoginUser(guid, token);
                if (!valid) {
                    code = SystemConstants.CODE_408;
                    msg = "无效token！";
                } else {
                    //重置token失效时间
                    loginService.updateUserToken(guid, token);
                }
            }
        } else {
            code = SystemConstants.CODE_FAILURE;
            msg = "guid,token不能为空";
        }
        json.put("code", code);
        json.put("msg", msg);
        json.put("valid", valid);
        json.put("username",username);
        return json;
    }
}
