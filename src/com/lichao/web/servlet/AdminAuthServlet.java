package com.lichao.web.servlet;

import com.lichao.service.AdminService;
import com.lichao.utils.BeanFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "AdminAuthServlet")
public class AdminAuthServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        String adminUsername = request.getParameter("adminUsername");
        String adminPassword = request.getParameter("adminPassword");
        AdminService service = (AdminService) BeanFactory.getBean("adminService");
        boolean isLogin = service.adminAuth(adminUsername,adminPassword);
        if (isLogin) {
            session.setAttribute("adminUsername", adminUsername);
            response.sendRedirect(request.getContextPath() + "/admin/home.jsp");
        } else {
            response.sendRedirect(request.getContextPath() + "/adminauth/index.jsp");
        }
    }
}
