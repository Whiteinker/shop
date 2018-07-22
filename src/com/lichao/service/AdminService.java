package com.lichao.service;

import com.lichao.dao.AdminDao;
import com.lichao.domain.Category;
import com.lichao.domain.Order;
import com.lichao.domain.Product;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface AdminService {
    public List<Category> findAllCategory();


    public void saveProduct(Product product);

    public List<Order> findAllOrders();

    public List<Product> findAllProducts();


    public List<Map<String,Object>> findOrderInfoByOid(String oid);


    void EditProduct(Product product);

    void delProduct(String pid);

    boolean adminAuth(String adminUsername, String adminPassword);
}
