package com.lichao.web.servlet;

import com.lichao.domain.Category;
import com.lichao.domain.Product;
import com.lichao.service.AdminService;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "AdminAddProductServlet")
public class AdminAddProductServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Product product = new Product();
        Map<String,Object> map = new HashMap<String, Object>();

        try {
        //创建文件磁盘文件项工厂
        DiskFileItemFactory factory = new DiskFileItemFactory();
        //创建文件上传核心对象
        ServletFileUpload upload = new ServletFileUpload(factory);
        //解析request获得文件项对象集合
            System.out.println(upload);

            List<FileItem> parseRequest = upload.parseRequest(request);
            for(FileItem item : parseRequest){
//                System.out.println(item.getFieldName());
                //判断是否是普通表单项
                boolean formField = item.isFormField();
                if(formField){
                    //普通表单项封装到Product
                    String fieldName = item.getFieldName();
                    String fieldValue = item.getString("UTF-8");
                    System.out.println(fieldValue);
                    map.put(fieldName,fieldValue);
                }else{
                    //文件上传项 获得文件内容
                    String fileName = item.getName();
                    String path = this.getServletContext().getRealPath("upload");
                    InputStream in = item.getInputStream();
                    OutputStream out = new FileOutputStream(path+"/"+fileName);
                    //文件拷贝
                    IOUtils.copy(in,out);
                    in.close();
                    out.close();
                    item.delete();
                    map.put("pimage","upload/"+fileName);
                }
            }
            BeanUtils.populate(product,map);
            //private String pid;
            product.setPid(CommonsUtils.getUUID());
            //private Date pdate;
            product.setPdate(new Date());
            //private int pflag;
            product.setPflag(0);
            //private Category category;   //外键
            Category category = new Category();
            category.setCid(map.get("cid").toString());
            product.setCategory(category);

            AdminService service = (AdminService) BeanFactory.getBean("adminService");
            if(request.getParameter("pid")==null){
            service.saveProduct(product);
            }else{
                System.out.println("edit");
            }
        } catch (FileUploadException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        response.sendRedirect(request.getContextPath()+"/admin?method=findAllProducts");
    }
}
