package com.lichao.impl;

import com.lichao.dao.AdminDao;
import com.lichao.domain.Admin;
import com.lichao.domain.Category;
import com.lichao.domain.Order;
import com.lichao.domain.Product;
import com.lichao.service.AdminService;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class AdminServiceImpl implements AdminService{
    public List<Category> findAllCategory() {
        AdminDao dao = new AdminDao();
        List<Category> categoryList = null;
        try {
            categoryList = dao.findAllCategory();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categoryList;
    }

    public void saveProduct(Product product) {
        AdminDao dao = new AdminDao();
        try {
            dao.savePtoduct(product);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Order> findAllOrders() {
        AdminDao dao = new AdminDao();
        List<Order> orderList = null;
        try {
            orderList = dao.findAllOrders();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderList;
    }

    @Override
    public List<Product> findAllProducts() {
        AdminDao dao = new AdminDao();
        List<Product> productList = null;
        try {
            productList = dao.findAllProducts();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productList;
    }

    public List<Map<String,Object>> findOrderInfoByOid(String oid) {
        AdminDao dao = new AdminDao();
        List<Map<String,Object>> mapList = null;
        try {
            mapList = dao.findOrderInfoByOid(oid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mapList;
    }

    @Override
    public void EditProduct(Product product) {
        AdminDao dao = new AdminDao();
        try {
            dao.EditProduct(product);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void delProduct(String pid) {
        AdminDao dao = new AdminDao();
        try {
            int row = dao.delProduct(pid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean adminAuth(String adminUsername, String adminPassword) {
        AdminDao dao = new AdminDao();
        Admin admin = null;
        try {
            admin = dao.adminAuth(adminUsername,adminPassword);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return admin==null?false:true;
    }


}
