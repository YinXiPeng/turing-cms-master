package com.bonc.turing.cms.manage.config.shrio;

import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import com.bonc.turing.cms.manage.constant.SystemConstants;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理
 * @author liuyunkai
 * @date 2018-11-6 10:40
 */
public class MyExceptionHandler implements HandlerExceptionResolver {
    private static final Logger logger = LoggerFactory.getLogger(MyExceptionHandler.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception ex) {
        ModelAndView mv = new ModelAndView();
        FastJsonJsonView view = new FastJsonJsonView();
        Map<String, Object> attributes = new HashMap<>();
        if (ex instanceof UnauthenticatedException) {
            attributes.put("code", SystemConstants.CODE_FAILURE);
            attributes.put("msg", SystemConstants.MSG_ERROR);
        } else if (ex instanceof UnauthorizedException) {
            attributes.put("code",SystemConstants.CODE_401);
            attributes.put("msg", SystemConstants.MSG_401);
        } else {
            attributes.put("code", SystemConstants.CODE_ERROR);
            attributes.put("msg", ex.getMessage());
            logger.error("global exceptions:", ex.getMessage());
        }

        view.setAttributesMap(attributes);
        mv.setView(view);
        return mv;
    }
}