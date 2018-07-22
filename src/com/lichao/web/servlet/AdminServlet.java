package com.lichao.web.servlet;

import com.google.gson.Gson;
import com.lichao.domain.Category;
import com.lichao.domain.Order;
import com.lichao.domain.Product;
import com.lichao.service.AdminService;
import com.lichao.service.ProductService;
import com.lichao.utils.BeanFactory;
import com.lichao.utils.CommonsUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "AdminServlet")
public class AdminServlet extends BaseServlet {


    //根据订单id查询orderItem
    public void findOrderInfoByOid(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //获得oid
        String oid = request.getParameter("oid");
        AdminService service = (AdminService) BeanFactory.getBean("adminService");
        List<Map<String,Object>> mapList = service.findOrderInfoByOid(oid);
        Gson gson = new Gson();
        String json = gson.toJson(mapList);

        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(json);

    }

        //获得所有订单
    public void findAllOrders(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AdminService service = (AdminService) BeanFactory.getBean("adminService");
        List<Order> orderList = service.findAllOrders();
        request.setAttribute("orderList",orderList);
        request.getRequestDispatcher("/admin/order/list.jsp").forward(request,response);

    }

    public void findAllProducts(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AdminService service = (AdminService) BeanFactory.getBean("adminService");
        List<Product> productList = service.findAllProducts();
        request.setAttribute("productList",productList);
//        for(Product pro : productList){
//            System.out.println(pro.getPimage());3
//        }
        request.getRequestDispatcher("/admin/product/list.jsp").forward(request,response);


    }
    public void findAllCategorys(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AdminService service = (AdminService) BeanFactory.getBean("adminService");
        List<Category> categoryList = service.findAllCategory();
        request.setAttribute("categoryList",categoryList);
        request.getRequestDispatcher("/admin/category/list.jsp").forward(request,response);
    }

        public void findAllCategoryJson(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

            //提供一个集合List<Category>

        AdminService service = (AdminService) BeanFactory.getBean("adminService");
        List<Category> categoryList = service.findAllCategory();
        Gson gson = new Gson();
        String json = gson.toJson(categoryList);
        response.setContentType("text/json;charset=UTF-8");
        response.getWriter().write(json);

    }

    //进入后台商品管理编辑页面
    public void toEditPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String pid = request.getParameter("pid");
        //获得商品信息
        ProductService service = new ProductService();
        Product product = service.findProductByPid(pid);
        Category category = new Category();
        category = service.findCidByPid(pid);

        product.setCategory(category);
        request.setAttribute("product",product);
        request.getRequestDispatcher("/admin/product/edit.jsp").forward(request,response);
    }


    public void deleteProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String pid = request.getParameter("pid");
        AdminService service = (AdminService) BeanFactory.getBean("adminService");
        service.delProduct(pid);
        response.sendRedirect(request.getContextPath()+"/admin?method=findAllProducts");
    }

    public void adminLogout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.removeAttribute("adminUsername");
        response.sendRedirect(request.getContextPath() + "/adminauth/index.jsp");
    }


}
