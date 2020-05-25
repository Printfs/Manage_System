package com.hang.manage.system.security;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/*
拦截器组件
 */
public class JwtInterceptor extends HandlerInterceptorAdapter {

    @Override

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //拿到token
        final String key = request.getParameter("key");

        if (key == null) {
            throw new ServletException("未登陆过,请重新登陆");
        }


        try {
            JwtUtil.checkToken(key);     //检查token
            return true;
        } catch (Exception e) {
            throw new ServletException(e.getMessage());
        }
    }
}
