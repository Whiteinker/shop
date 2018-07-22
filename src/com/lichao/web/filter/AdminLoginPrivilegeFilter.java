package com.lichao.web.filter;


import com.lichao.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.LogRecord;

public class AdminLoginPrivilegeFilter implements javax.servlet.Filter{


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        HttpSession session = req.getSession();
        //校验用户是否登陆---校验session是否有user
        Object adminUsername = session.getAttribute("adminUsername");

        if (adminUsername == null){
            resp.sendRedirect(req.getContextPath()+"/adminauth/index.jsp");
            return; //不让后面代码执行
        }
        filterChain.doFilter(req,resp);
    }

    @Override
    public void destroy() {

    }
}
