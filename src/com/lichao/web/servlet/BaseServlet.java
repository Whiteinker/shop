package com.lichao.web.servlet;

import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

//@WebServlet(name = "BaseServlet")
//@SuppressWarnings("all")
public class BaseServlet extends HttpServlet {


    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        System.out.println(req.getRemoteAddr()+":"+req.getLocalPort()+req.getContextPath()+
                req.getServletPath()+"?"+req.getQueryString());
        try {
        //获得请求的method名
        String methodName = req.getParameter("method");
        System.out.println("baseServlet:"+methodName);
        //获得当前被访问的对象的字节码对象
        Class clazz= this.getClass();  //Product---User
        //获得当前字节码对象中的指定方法
            Method method = null;

            method = clazz.getMethod(methodName,HttpServletRequest.class,HttpServletResponse.class);

            method.invoke(this,req,resp);
        } catch (Exception e) {
            e.printStackTrace();
        }


        }

}
