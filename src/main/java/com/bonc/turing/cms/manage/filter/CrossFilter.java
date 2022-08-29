package com.bonc.turing.cms.manage.filter;


import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 解决跨域访问
 * Created by yuanwei on 2017-11-24.
 */

@Component
public class CrossFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse rep = (HttpServletResponse) servletResponse;
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        String origin = req.getHeader("Origin");
        rep.setCharacterEncoding("UTF-8");
        req.setCharacterEncoding("UTF-8");
        rep.setContentType("application/json;charset=UTF-8");
        rep.setHeader("Access-Control-Allow-Origin", "*");
        rep.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS");
        rep.setHeader("Access-Control-Allow-Headers", "content-type");
        rep.setHeader("Access-Control-Allow-Credentials", "true");
//        String guid = req.getParameter("guid");
//        String token = req.getParameter("token");


//        if(StringUtils.isEmpty(guid)){
//            servletResponse.getWriter().write(JSON.toJSONString(new ResultBean<>(ResultBean.FAIL,"guid为空")));
//            servletResponse.getWriter().flush();
//        }

        filterChain.doFilter(req, rep);

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}

